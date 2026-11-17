package com.zc.vndb.feature.detail.presentation

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
import com.zc.vndb.feature.detail.presentation.navigation.DetailNavActions
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailRoute(
    novelId: String,
    actions: DetailNavActions,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = koinViewModel { parametersOf(novelId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            DetailEffect.NavigateBack -> actions.onNavigateBack()

            is DetailEffect.NavigateToNovel -> actions.onOpenNovel(effect.id)

            is DetailEffect.NavigateToCharacter -> actions.onOpenCharacter(effect.id)

            is DetailEffect.NavigateToTag -> actions.onOpenTag(effect.id)

            is DetailEffect.NavigateToProducer -> actions.onOpenProducer(effect.id)

            is DetailEffect.NavigateToStaff -> actions.onOpenStaff(effect.id)

            is DetailEffect.NavigateToRelease -> actions.onOpenRelease(effect.id)

            is DetailEffect.OpenListEditor -> actions.onOpenListEditor(effect.novelId, effect.title)

            is DetailEffect.OpenUrl -> uriHandler.openUri(effect.url)

            is DetailEffect.Share -> {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, effect.title)
                    putExtra(Intent.EXTRA_TEXT, effect.url)
                }
                context.startActivity(
                    Intent.createChooser(
                        send,
                        UiText.StringResource(
                            StringResource.detail_share_chooser,
                        ).asString(context),
                    ),
                )
            }
        }
    }

    DetailScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
