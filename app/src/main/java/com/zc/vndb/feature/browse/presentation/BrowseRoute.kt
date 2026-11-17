package com.zc.vndb.feature.browse.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.common.ui.util.ObserveAsEvents
import com.zc.vndb.feature.browse.domain.model.BrowseScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun BrowseRoute(
    onOpenNovel: (String) -> Unit,
    modifier: Modifier = Modifier,
    scope: BrowseScope = BrowseScope.None,
    onNavigateBack: () -> Unit = {},
    viewModel: BrowseViewModel = koinViewModel { parametersOf(scope) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is BrowseEffect.NavigateToNovel -> onOpenNovel(effect.id)

            BrowseEffect.NavigateBack -> onNavigateBack()

            is BrowseEffect.ShowError -> snackbarHostState.showSnackbar(
                effect.error.message.asString(context),
            )
        }
    }

    BrowseScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
    )
}
