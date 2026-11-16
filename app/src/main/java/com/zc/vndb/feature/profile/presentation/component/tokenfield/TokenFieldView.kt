package com.zc.vndb.feature.profile.presentation.component.tokenfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentPaste
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.zc.vndb.R
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val NoteIconSize = 16.dp

/** The token input: masked by default, with show/hide and Paste, an error line and a storage note. */
@Composable
fun TokenFieldView(
    token: String,
    isTokenVisible: Boolean,
    error: UiText?,
    isEnabled: Boolean,
    onTokenChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit,
    onPasteClick: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCardView(modifier = modifier) {
        TokenHeader(isEnabled = isEnabled, onPasteClick = onPasteClick)
        OutlinedTextField(
            value = token,
            onValueChange = onTokenChange,
            enabled = isEnabled,
            singleLine = true,
            isError = error != null,
            placeholder = { Text(stringResource(R.string.login_token_placeholder)) },
            visualTransformation = if (isTokenVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { onDone() }),
            trailingIcon = {
                IconButton(onClick = onVisibilityToggle) {
                    Icon(
                        if (isTokenVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = stringResource(
                            if (isTokenVisible) R.string.login_hide_token else R.string.login_show_token,
                        ),
                    )
                }
            },
            supportingText = error?.let { message -> { Text(message.asString()) } },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VndbTheme.colors.accent,
                unfocusedBorderColor = VndbTheme.colors.borderSubtle,
                cursorColor = VndbTheme.colors.accent,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        StorageNote()
    }
}

@Composable
private fun TokenHeader(isEnabled: Boolean, onPasteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Outlined.Key, contentDescription = null, tint = VndbTheme.colors.accent)
        Text(
            text = stringResource(R.string.login_token_label),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onPasteClick, enabled = isEnabled) {
            Icon(
                Icons.Outlined.ContentPaste,
                contentDescription = null,
                tint = VndbTheme.colors.accent,
            )
            Text(
                text = stringResource(R.string.login_paste),
                color = VndbTheme.colors.accent,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun StorageNote() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Outlined.Lock,
            contentDescription = null,
            tint = VndbTheme.colors.spoilerNone.content,
            modifier = Modifier.size(NoteIconSize),
        )
        Text(
            text = stringResource(R.string.login_storage_note),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@AdaptivePreviews
@Composable
private fun TokenFieldViewPreview() {
    VndbTheme {
        TokenFieldView(
            token = "hsoo-ybws4",
            isTokenVisible = true,
            error = UiText.StringValue("VNDB didn't accept this token."),
            isEnabled = true,
            onTokenChange = {},
            onVisibilityToggle = {},
            onPasteClick = {},
            onDone = {},
        )
    }
}
