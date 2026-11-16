package com.zc.vndb.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.common.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is LoginEffect.OpenUrl -> uriHandler.openUri(effect.url)

            // The Profile tab follows the session, so going back shows the signed-in profile.
            LoginEffect.LoggedIn,
            LoginEffect.NavigateBack,
            -> onNavigateBack()
        }
    }

    LoginScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
