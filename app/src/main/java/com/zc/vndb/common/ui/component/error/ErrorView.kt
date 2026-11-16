package com.zc.vndb.common.ui.component.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.error.model.ErrorUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val IconSize = 40.dp

/** Full-area error placeholder with an optional Retry button. */
@Composable
fun ErrorView(model: ErrorUIModel, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(VndbTheme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(
            VndbTheme.spacing.lg,
            Alignment.CenterVertically,
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(IconSize),
        )
        Text(
            text = model.message.asString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        if (model.isRetryable) {
            Button(onClick = onRetry) {
                Text(text = UiText.StringResource(StringResource.action_retry).asString())
            }
        }
    }
}

@AdaptivePreviews
@Composable
private fun ErrorViewPreview() {
    VndbTheme {
        Surface {
            ErrorView(
                model = ErrorUIModel(
                    UiText.StringResource(StringResource.error_network),
                    isRetryable = true,
                ),
                onRetry = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
