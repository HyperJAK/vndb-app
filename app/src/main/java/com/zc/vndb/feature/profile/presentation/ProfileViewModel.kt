package com.zc.vndb.feature.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.Session
import com.zc.vndb.common.ui.component.error.mapper.toErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.profile.domain.model.ListEntry
import com.zc.vndb.feature.profile.domain.model.ListLabel
import com.zc.vndb.feature.profile.domain.model.UserProfile
import com.zc.vndb.feature.profile.domain.usecase.GetListEntriesUseCase
import com.zc.vndb.feature.profile.domain.usecase.GetProfileUseCase
import com.zc.vndb.feature.profile.domain.usecase.LogOutUseCase
import com.zc.vndb.feature.profile.domain.usecase.ObserveSessionUseCase
import com.zc.vndb.feature.profile.presentation.component.listentrycard.mapper.toCardUIModel
import com.zc.vndb.feature.profile.presentation.component.profileheader.mapper.toHeaderUIModel
import com.zc.vndb.feature.profile.presentation.mapper.pickSelected
import com.zc.vndb.feature.profile.presentation.mapper.toChips
import com.zc.vndb.feature.profile.presentation.model.EntriesUIState
import kotlin.time.Clock
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

/**
 * Follows the stored session: a guest sees the log-in prompt, a signed-in user their labels and
 * the entries of the selected one. Each label's entries load the first time it is selected and are
 * kept; a token VNDB rejects signs the user out (the repository clears it) with a message.
 */
@KoinViewModel
class ProfileViewModel(
    observeSession: ObserveSessionUseCase,
    private val getProfile: GetProfileUseCase,
    private val getListEntries: GetListEntriesUseCase,
    private val logOut: LogOutUseCase,
    private val clock: Clock,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())

    val state: StateFlow<ProfileState> = combine(
        _state,
        observeSession().onEach(::onSession),
    ) { state, _ -> state }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            ProfileState(),
        )

    private val _effect = Channel<ProfileEffect>(Channel.BUFFERED)
    val effect: Flow<ProfileEffect> = _effect.receiveAsFlow()

    private var session: Session? = null
    private var isSessionRead = false
    private var user: UserProfile? = null
    private var labels: List<ListLabel> = emptyList()
    private var selectedLabelId: Int? = null
    private val entriesByLabel = mutableMapOf<Int, LabelEntries>()
    private var overviewJob: Job? = null
    private var entriesJob: Job? = null

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LogInClicked -> sendEffect(ProfileEffect.NavigateToLogin)

            ProfileIntent.Retry -> loadOverview(isRefresh = false)

            ProfileIntent.Refresh -> refresh()

            is ProfileIntent.LabelSelected -> selectLabel(intent.id)

            ProfileIntent.EndReached -> if (!_state.value.entries.loadMoreFailed) loadNextPage()

            ProfileIntent.LoadMoreRetried -> loadNextPage()

            ProfileIntent.EntriesRetried -> selectedLabelId?.let {
                loadFirstPage(
                    it,
                    isRefresh = false,
                )
            }

            is ProfileIntent.EntryClicked -> sendEffect(
                ProfileEffect.NavigateToNovel(intent.novelId),
            )

            ProfileIntent.LogOutClicked -> _state.update { it.copy(isLogOutDialogVisible = true) }

            ProfileIntent.LogOutDismissed -> _state.update {
                it.copy(
                    isLogOutDialogVisible = false,
                )
            }

            ProfileIntent.LogOutConfirmed -> confirmLogOut()
        }
    }

    /** A new account (or a guest) starts over; the same account keeps what is loaded. */
    private fun onSession(newSession: Session?) {
        if (isSessionRead && newSession == session) return
        isSessionRead = true
        session = newSession
        overviewJob?.cancel()
        entriesJob?.cancel()
        user = null
        labels = emptyList()
        selectedLabelId = null
        entriesByLabel.clear()
        _state.value = ProfileState(isSessionKnown = true, header = newSession?.toHeaderUIModel())
        if (newSession != null) loadOverview(isRefresh = false)
    }

    private fun refresh() {
        val current = _state.value
        if (!current.isSignedIn || current.isLoading || current.isRefreshing) return
        if (labels.isEmpty()) return loadOverview(isRefresh = false)
        loadOverview(isRefresh = true)
    }

    /** Labels and profile; then the selected label's first page. */
    private fun loadOverview(isRefresh: Boolean) {
        val current = session ?: return
        overviewJob?.cancel()
        entriesJob?.cancel()
        _state.update { it.copy(isLoading = !isRefresh, isRefreshing = isRefresh, error = null) }
        overviewJob = viewModelScope.launch {
            getProfile(current.userId)
                .onSuccess { overview ->
                    user = overview.user
                    labels = overview.labels
                    entriesByLabel.clear()
                    selectedLabelId = labels.pickSelected(selectedLabelId)
                    _state.update {
                        it.copy(
                            header = current.toHeaderUIModel(user),
                            isLoading = false,
                            labels = labels.toChips(selectedLabelId),
                        )
                    }
                    val selected = selectedLabelId
                    if (selected != null) {
                        loadFirstPage(selected, isRefresh)
                    } else {
                        _state.update { it.copy(isRefreshing = false, entries = EntriesUIState()) }
                    }
                }
                .onFailure { error ->
                    if (onUnauthorized(error)) return@onFailure
                    val keepContent = isRefresh && labels.isNotEmpty()
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = if (keepContent) null else error.toErrorUIModel(),
                        )
                    }
                    if (keepContent) {
                        sendEffect(
                            ProfileEffect.ShowMessage(error.toErrorUIModel().message),
                        )
                    }
                }
        }
    }

    private fun selectLabel(id: Int) {
        if (id == selectedLabelId || labels.none { it.id == id }) return
        entriesJob?.cancel()
        selectedLabelId = id
        _state.update { it.copy(labels = labels.toChips(id)) }
        if (id in entriesByLabel) render(id) else loadFirstPage(id, isRefresh = false)
    }

    /** On refresh the current entries stay on screen until the new page lands. */
    private fun loadFirstPage(labelId: Int, isRefresh: Boolean) {
        val current = session ?: return
        entriesJob?.cancel()
        if (!isRefresh) _state.update { it.copy(entries = EntriesUIState(isLoading = true)) }
        entriesJob = viewModelScope.launch {
            getListEntries(current.userId, labelId, GetListEntriesUseCase.FIRST_PAGE)
                .onSuccess { page ->
                    entriesByLabel[labelId] = LabelEntries(
                        entries = page.entries,
                        nextPage = GetListEntriesUseCase.FIRST_PAGE + 1,
                        hasMore = page.hasMore,
                    )
                    render(labelId)
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure { error ->
                    if (onUnauthorized(error)) return@onFailure
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            entries = EntriesUIState(error = error.toErrorUIModel()),
                        )
                    }
                }
        }
    }

    /** Next page of the selected label; a failure keeps what is shown and offers a retry. */
    private fun loadNextPage() {
        val (userId, labelId, loaded) = nextPageTarget() ?: return
        _state.update {
            it.copy(
                entries = it.entries.copy(isLoadingMore = true, loadMoreFailed = false),
            )
        }
        entriesJob = viewModelScope.launch {
            getListEntries(userId, labelId, loaded.nextPage)
                .onSuccess { page ->
                    entriesByLabel[labelId] = LabelEntries(
                        entries = (loaded.entries + page.entries).distinctBy { it.novelId },
                        nextPage = loaded.nextPage + 1,
                        hasMore = page.hasMore,
                    )
                    render(labelId)
                }
                .onFailure { error ->
                    if (onUnauthorized(error)) return@onFailure
                    _state.update {
                        it.copy(
                            entries = it.entries.copy(isLoadingMore = false, loadMoreFailed = true),
                        )
                    }
                }
        }
    }

    /** Who, which label and what is loaded, when the selected label has another page to fetch. */
    private fun nextPageTarget(): Triple<String, Int, LabelEntries>? {
        val userId = session?.userId
        val labelId = selectedLabelId
        val loaded = labelId?.let(
            entriesByLabel::get,
        )?.takeIf { it.hasMore && entriesJob?.isActive != true }
        return if (userId != null && labelId != null && loaded != null) {
            Triple(
                userId,
                labelId,
                loaded,
            )
        } else {
            null
        }
    }

    private fun render(labelId: Int) {
        if (labelId != selectedLabelId) return
        val loaded = entriesByLabel[labelId] ?: return
        val now = clock.now()
        _state.update {
            it.copy(
                entries = EntriesUIState(
                    items = loaded.entries.map { entry ->
                        entry.toCardUIModel(
                            labelId,
                            now,
                        )
                    }.toImmutableList(),
                    hasMore = loaded.hasMore,
                ),
            )
        }
    }

    /** The repository already cleared a rejected token, so the session flow turns this into a guest. */
    private fun onUnauthorized(error: Throwable): Boolean {
        if (error != DomainError.Unauthorized) return false
        sendEffect(
            ProfileEffect.ShowMessage(
                UiText.StringResource(StringResource.profile_session_expired),
            ),
        )
        return true
    }

    private fun confirmLogOut() {
        _state.update { it.copy(isLogOutDialogVisible = false) }
        viewModelScope.launch { logOut() }
    }

    private fun sendEffect(effect: ProfileEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private data class LabelEntries(
        val entries: List<ListEntry>,
        val nextPage: Int,
        val hasMore: Boolean,
    )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
