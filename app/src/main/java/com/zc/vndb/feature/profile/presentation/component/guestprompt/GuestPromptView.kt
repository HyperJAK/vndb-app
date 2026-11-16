package com.zc.vndb.feature.profile.presentation.component.guestprompt

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val IconBoxSize = 72.dp
private val IconSize = 36.dp
private val MaxWidth = 420.dp

/** Profile for a guest: why to log in, and the Log in button. */
@Composable
fun GuestPromptView(onLogInClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .widthIn(max = MaxWidth)
            .padding(VndbTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(VndbTheme.spacing.lg),
    ) {
        val shape = MaterialTheme.shapes.large
        Box(
            modifier = Modifier
                .size(IconBoxSize)
                .clip(shape)
                .background(VndbTheme.colors.surfaceCard)
                .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = null,
                tint = VndbTheme.colors.accent,
                modifier = Modifier.size(IconSize),
            )
        }
        Text(
            text = UiText.StringResource(StringResource.profile_guest_title).asString(),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = UiText.StringResource(StringResource.profile_guest_body).asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = onLogInClick,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = VndbTheme.colors.accent,
                contentColor = VndbTheme.colors.onAccent,
            ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.AutoMirrored.Outlined.Login, contentDescription = null)
            Text(
                text = UiText.StringResource(StringResource.profile_log_in).asString(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(start = VndbTheme.spacing.sm),
            )
        }
        Text(
            text = UiText.StringResource(StringResource.profile_guest_note).asString(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@AdaptivePreviews
@Composable
private fun GuestPromptViewPreview() {
    VndbTheme {
        GuestPromptView(onLogInClick = {})
    }
}
