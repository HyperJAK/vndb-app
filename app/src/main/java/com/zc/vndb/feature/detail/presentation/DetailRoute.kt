package com.zc.vndb.feature.detail.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.R
import com.zc.vndb.common.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailRoute(
    novelId: String,
    onNavigateBack: () -> Unit,
    onOpenNovel: (String) -> Unit,
    onOpenCharacter: (String) -> Unit,
    onOpenTag: (String) -> Unit,
    onOpenRelease: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel { parametersOf(novelId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            DetailEffect.NavigateBack -> onNavigateBack()

            is DetailEffect.NavigateToNovel -> onOpenNovel(effect.id)

            is DetailEffect.NavigateToCharacter -> onOpenCharacter(effect.id)

            is DetailEffect.NavigateToTag -> onOpenTag(effect.id)

            is DetailEffect.NavigateToRelease -> onOpenRelease(effect.id)

            is DetailEffect.OpenUrl -> uriHandler.openUri(effect.url)

            is DetailEffect.Share -> {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, effect.title)
                    putExtra(Intent.EXTRA_TEXT, effect.url)
                }
                context.startActivity(
                    Intent.createChooser(send, context.getString(R.string.detail_share_chooser)),
                )
            }
        }
    }

    DetailScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
