package com.zc.vndb.feature.detail.presentation.component.lazysection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.presentation.model.SectionState

/**
 * Renders a lazily loaded section. Asks for its data ([onLoad]) the first time it is composed —
 * i.e. when its tab opens or it scrolls into view — then shows a spinner, an inline error with
 * Retry, or [content].
 */
@Composable
fun <T> LazySectionView(
    state: SectionState<T>,
    onLoad: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    val currentOnLoad by rememberUpdatedState(onLoad)
    LaunchedEffect(Unit) { currentOnLoad() }
    when (state) {
        SectionState.Idle, SectionState.Loading -> Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(VndbTheme.spacing.lg),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = VndbTheme.colors.accent)
        }

        is SectionState.Failed -> SectionFailedView(state.error, onRetry, modifier)

        is SectionState.Loaded -> content(state.data)
    }
}

@Composable
private fun SectionFailedView(
    error: ErrorUIModel,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(VndbTheme.spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = UiText.StringResource(StringResource.detail_section_failed).asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = error.message.asString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (error.isRetryable) {
            TextButton(onClick = onRetry) {
                Text(
                    text = UiText.StringResource(StringResource.action_retry).asString(),
                    color = VndbTheme.colors.accent,
                )
            }
        }
    }
}

@AdaptivePreviews
@Composable
private fun LazySectionViewPreview() {
    VndbTheme {
        LazySectionView<String>(
            state = SectionState.Failed(
                ErrorUIModel(
                    UiText.StringResource(StringResource.error_network),
                    isRetryable = true,
                ),
            ),
            onLoad = {},
            onRetry = {},
        ) { Text(it) }
    }
}
