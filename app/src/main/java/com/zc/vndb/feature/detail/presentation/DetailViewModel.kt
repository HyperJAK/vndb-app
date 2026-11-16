package com.zc.vndb.feature.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.R
import com.zc.vndb.common.domain.model.CharacterRole
import com.zc.vndb.common.ui.component.error.mapper.toErrorUIModel
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.domain.model.NovelEditions
import com.zc.vndb.feature.detail.domain.usecase.GetNovelCharactersUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelDetailUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelEditionsUseCase
import com.zc.vndb.feature.detail.presentation.component.charactercard.mapper.toCardUIModel
import com.zc.vndb.feature.detail.presentation.component.detailhero.mapper.toHeroUIModel
import com.zc.vndb.feature.detail.presentation.component.editionrow.mapper.toRowUIModel
import com.zc.vndb.feature.detail.presentation.component.relationcard.mapper.toCardUIModel
import com.zc.vndb.feature.detail.presentation.component.scorecard.mapper.toScoreUIModel
import com.zc.vndb.feature.detail.presentation.component.taggroups.mapper.toContentNotes
import com.zc.vndb.feature.detail.presentation.component.taggroups.mapper.toHiddenTagsLabel
import com.zc.vndb.feature.detail.presentation.component.taggroups.mapper.toTagGroups
import com.zc.vndb.feature.detail.presentation.mapper.toLinks
import com.zc.vndb.feature.detail.presentation.mapper.toSynopsis
import com.zc.vndb.feature.detail.presentation.mapper.vndbUrl
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val novelId: String,
    private val getNovelDetail: GetNovelDetailUseCase,
    private val getNovelCharacters: GetNovelCharactersUseCase,
    private val getNovelEditions: GetNovelEditionsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState(novelId = novelId))
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _effect = Channel<DetailEffect>(Channel.BUFFERED)
    val effect: Flow<DetailEffect> = _effect.receiveAsFlow()

    // Domain data kept to re-map when the spoiler level changes.
    private var novel: NovelDetail? = null
    private var characters: List<NovelCharacter> = emptyList()
    private var loadJob: Job? = null

    init {
        onIntent(DetailIntent.Load)
    }

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            DetailIntent.Load,
            DetailIntent.Retry,
            -> load()

            DetailIntent.BackClicked -> sendEffect(DetailEffect.NavigateBack)

            DetailIntent.ShareClicked -> sendEffect(
                DetailEffect.Share(novel?.title.orEmpty(), vndbUrl(novelId)),
            )

            DetailIntent.OpenInBrowserClicked -> sendEffect(DetailEffect.OpenUrl(vndbUrl(novelId)))

            is DetailIntent.LinkClicked -> sendEffect(DetailEffect.OpenUrl(intent.url))

            is DetailIntent.SpoilerLevelSelected -> {
                _state.update { it.copy(spoilerLevel = intent.level) }
                render()
            }

            DetailIntent.SynopsisToggled -> _state.update {
                it.copy(
                    isSynopsisExpanded = !it.isSynopsisExpanded,
                )
            }

            is DetailIntent.RelationClicked -> sendEffect(DetailEffect.NavigateToNovel(intent.id))

            is DetailIntent.CharacterClicked -> sendEffect(
                DetailEffect.NavigateToCharacter(intent.id),
            )

            is DetailIntent.TagClicked -> sendEffect(DetailEffect.NavigateToTag(intent.id))

            is DetailIntent.EditionClicked -> sendEffect(
                DetailEffect.NavigateToRelease(intent.releaseId),
            )
        }
    }

    /**
     * The VN and its editions load in parallel; characters follow, since their voice actors come
     * from the VN. Only the VN is required: failed characters or editions just hide their section.
     */
    private fun load() {
        loadJob?.cancel()
        _state.update { it.copy(isLoading = true, error = null) }
        loadJob = viewModelScope.launch {
            val editions = async { getNovelEditions(novelId).getOrNull() }
            val loaded = getNovelDetail(novelId).getOrElse { error ->
                editions.cancel()
                _state.update { it.copy(isLoading = false, error = error.toErrorUIModel()) }
                return@launch
            }
            novel = loaded
            characters = getNovelCharacters(novelId, loaded.voiceActors).getOrNull().orEmpty()
            showStatic(loaded, editions.await())
            render()
            _state.update { it.copy(isLoading = false) }
        }
    }

    /** Sections that do not depend on the spoiler level. */
    private fun showStatic(novel: NovelDetail, editions: NovelEditions?) {
        _state.update {
            it.copy(
                hero = novel.toHeroUIModel(editions?.ageRange),
                score = novel.toScoreUIModel(),
                links = novel.toLinks(),
                contentNotes = novel.toContentNotes(),
                editions = editions?.editions.orEmpty().map { edition ->
                    edition.toRowUIModel()
                }.toImmutableList(),
                relations = novel.relations.map { relation ->
                    relation.toCardUIModel()
                }.toImmutableList(),
            )
        }
    }

    /** Sections filtered by the spoiler level. */
    private fun render() {
        val current = novel ?: return
        val level = _state.value.spoilerLevel
        val visible = characters.filter { it.spoilerLevel <= level.maxLevel }
        val hiddenCharacters = characters.size - visible.size
        val mainCount = visible.count {
            it.role == CharacterRole.Main ||
                it.role == CharacterRole.Primary
        }
        _state.update {
            it.copy(
                synopsis = current.toSynopsis(level),
                tagGroups = current.toTagGroups(level),
                hiddenTagsLabel = current.toHiddenTagsLabel(level),
                characters = visible.map { character ->
                    character.toCardUIModel()
                }.toImmutableList(),
                mainCharactersLabel = mainCount.takeIf { count -> count > 0 }
                    ?.let { count ->
                        UiText.StringPlural(
                            R.plurals.detail_main_characters,
                            count,
                            listOf(count),
                        )
                    },
                hiddenCharactersLabel = hiddenCharacters.takeIf { count -> count > 0 }
                    ?.let { count ->
                        UiText.StringPlural(
                            R.plurals.detail_hidden_by_spoiler,
                            count,
                            listOf(count),
                        )
                    },
            )
        }
    }

    private fun sendEffect(effect: DetailEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }
}
