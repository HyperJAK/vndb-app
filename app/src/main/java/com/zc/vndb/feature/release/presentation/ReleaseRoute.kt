package com.zc.vndb.feature.release.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReleaseRoute(
    releaseId: String,
    onNavigateBack: () -> Unit,
    onOpenNovel: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReleaseViewModel = koinViewModel { parametersOf(releaseId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            ReleaseEffect.NavigateBack -> onNavigateBack()

            is ReleaseEffect.NavigateToNovel -> onOpenNovel(effect.id)

            is ReleaseEffect.OpenUrl -> uriHandler.openUri(effect.url)

            is ReleaseEffect.Share -> {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, effect.title)
                    putExtra(Intent.EXTRA_TEXT, effect.url)
                }
                context.startActivity(
                    Intent.createChooser(
                        send,
                        UiText.StringResource(
                            StringResource.release_share_chooser,
                        ).asString(context),
                    ),
                )
            }
        }
    }

    ReleaseScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
