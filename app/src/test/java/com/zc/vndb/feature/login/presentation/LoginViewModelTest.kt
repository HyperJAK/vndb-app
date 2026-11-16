package com.zc.vndb.feature.login.presentation

import app.cash.turbine.test
import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.util.MainDispatcherRule
import com.zc.vndb.feature.login.domain.model.LoginSamples
import com.zc.vndb.feature.login.domain.repository.FakeLoginRepository
import com.zc.vndb.feature.login.domain.usecase.LogInUseCase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeLoginRepository().apply {
        acceptedSession =
            LoginSamples.session()
    }
    private val viewModel = LoginViewModel(LogInUseCase(repository))

    @Test
    fun `connect is enabled once something is typed`() {
        assertFalse(viewModel.state.value.canSubmit)
        viewModel.onIntent(LoginIntent.TokenChanged("x"))
        assertTrue(viewModel.state.value.canSubmit)
        viewModel.onIntent(LoginIntent.TokenVisibilityToggled)
        assertTrue(viewModel.state.value.isTokenVisible)
    }

    @Test
    fun `a valid token logs in and leaves the screen`() = runTest {
        viewModel.effect.test {
            viewModel.onIntent(LoginIntent.TokenChanged(LoginSamples.TOKEN))
            viewModel.onIntent(LoginIntent.SubmitClicked)
            assertTrue(viewModel.state.value.isSubmitting)
            assertEquals(LoginEffect.LoggedIn, awaitItem())
        }
        assertFalse(viewModel.state.value.isSubmitting)
        assertEquals(LoginSamples.session(), repository.stored)
    }

    @Test
    fun `a malformed token shows a format hint without calling VNDB`() = runTest {
        viewModel.onIntent(LoginIntent.TokenChanged("my password"))
        viewModel.onIntent(LoginIntent.SubmitClicked)
        advanceUntilIdle()

        assertEquals(
            UiText.StringResource(StringResource.login_error_format),
            viewModel.state.value.error,
        )
        assertTrue(repository.loggedInTokens.isEmpty())
    }

    @Test
    fun `a rejected token explains it and editing clears the error`() = runTest {
        repository.logInError = DomainError.Unauthorized
        viewModel.onIntent(LoginIntent.TokenChanged(LoginSamples.TOKEN))
        viewModel.onIntent(LoginIntent.SubmitClicked)
        advanceUntilIdle()

        assertEquals(
            UiText.StringResource(StringResource.login_error_invalid),
            viewModel.state.value.error,
        )
        viewModel.onIntent(LoginIntent.TokenChanged("${LoginSamples.TOKEN} "))
        assertNull(viewModel.state.value.error)
    }

    @Test
    fun `links and navigation become effects`() = runTest {
        viewModel.effect.test {
            viewModel.onIntent(LoginIntent.OpenTokensPageClicked)
            assertEquals(LoginEffect.OpenUrl("https://vndb.org/u/tokens"), awaitItem())
            viewModel.onIntent(LoginIntent.ContinueAsGuestClicked)
            assertEquals(LoginEffect.NavigateBack, awaitItem())
        }
    }
}
