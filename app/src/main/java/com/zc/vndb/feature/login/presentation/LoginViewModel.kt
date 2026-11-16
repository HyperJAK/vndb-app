package com.zc.vndb.feature.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.component.error.mapper.toErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.login.domain.model.InvalidTokenFormatException
import com.zc.vndb.feature.login.domain.usecase.LogInUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val logIn: LogInUseCase) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>(Channel.BUFFERED)
    val effect: Flow<LoginEffect> = _effect.receiveAsFlow()

    private var submitJob: Job? = null

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.TokenChanged -> _state.update {
                it.copy(
                    token = intent.token,
                    error = null,
                )
            }

            LoginIntent.TokenVisibilityToggled -> _state.update {
                it.copy(
                    isTokenVisible = !it.isTokenVisible,
                )
            }

            LoginIntent.OpenTokensPageClicked -> sendEffect(LoginEffect.OpenUrl(TOKENS_URL))

            LoginIntent.SubmitClicked -> submit()

            LoginIntent.ContinueAsGuestClicked,
            LoginIntent.BackClicked,
            -> sendEffect(LoginEffect.NavigateBack)
        }
    }

    private fun submit() {
        val current = _state.value
        if (!current.canSubmit || submitJob?.isActive == true) return
        _state.update { it.copy(isSubmitting = true, error = null) }
        submitJob = viewModelScope.launch {
            logIn(current.token)
                .onSuccess {
                    _state.update { it.copy(isSubmitting = false) }
                    sendEffect(LoginEffect.LoggedIn)
                }
                .onFailure { error ->
                    _state.update { it.copy(isSubmitting = false, error = error.toLoginMessage()) }
                }
        }
    }

    private fun Throwable.toLoginMessage(): UiText = when (this) {
        is InvalidTokenFormatException -> UiText.StringResource(StringResource.login_error_format)
        DomainError.Unauthorized -> UiText.StringResource(StringResource.login_error_invalid)
        else -> toErrorUIModel().message
    }

    private fun sendEffect(effect: LoginEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    companion object {
        /** VNDB redirects this to the signed-in user's Applications tab. */
        const val TOKENS_URL = "https://vndb.org/u/tokens"
    }
}
