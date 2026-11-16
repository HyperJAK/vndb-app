package com.zc.vndb.feature.browse.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.common.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BrowseRoute(
    onOpenNovel: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BrowseViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            is BrowseEffect.NavigateToNovel -> onOpenNovel(effect.id)
        }
    }

    BrowseScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
