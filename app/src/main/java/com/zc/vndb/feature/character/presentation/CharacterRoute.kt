package com.zc.vndb.feature.character.presentation

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
fun CharacterRoute(
    characterId: String,
    onNavigateBack: () -> Unit,
    onOpenNovel: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = koinViewModel { parametersOf(characterId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    ObserveAsEvents(viewModel.effect) { effect ->
        when (effect) {
            CharacterEffect.NavigateBack -> onNavigateBack()

            is CharacterEffect.NavigateToNovel -> onOpenNovel(effect.id)

            is CharacterEffect.OpenUrl -> uriHandler.openUri(effect.url)

            is CharacterEffect.Share -> {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, effect.name)
                    putExtra(Intent.EXTRA_TEXT, effect.url)
                }
                context.startActivity(
                    Intent.createChooser(
                        send,
                        UiText.StringResource(
                            StringResource.character_share_chooser,
                        ).asString(context),
                    ),
                )
            }
        }
    }

    CharacterScreen(state = state, onIntent = viewModel::onIntent, modifier = modifier)
}
