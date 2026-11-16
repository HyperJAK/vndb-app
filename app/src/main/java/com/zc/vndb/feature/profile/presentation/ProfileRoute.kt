package com.zc.vndb.feature.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.common.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoute(
    onOpenLogin: () -> Unit,
    onOpenNovel: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            ProfileEffect.NavigateToLogin -> onOpenLogin()

            is ProfileEffect.NavigateToNovel -> onOpenNovel(effect.id)

            is ProfileEffect.ShowMessage -> snackbarHostState.showSnackbar(
                effect.message.asString(context),
            )
        }
    }

    ProfileScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    )
}
