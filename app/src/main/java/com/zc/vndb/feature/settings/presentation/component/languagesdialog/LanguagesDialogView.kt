package com.zc.vndb.feature.settings.presentation.component.languagesdialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.settings.presentation.model.SettingOptionUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/** Tall enough to space the options apart and give each a full-width touch target. */
private val OptionRowMinHeight = 48.dp

/** Multi choice for New & Trending: the picks apply on OK, which needs at least one. */
@Composable
fun LanguagesDialogView(
    options: ImmutableList<SettingOptionUIModel>,
    onConfirm: (Set<String>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // A List (an ArrayList at runtime) so it survives configuration changes.
    var selected: List<String> by rememberSaveable {
        mutableStateOf(
            options.filter { it.isSelected }.map { it.key },
        )
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(
                UiText.StringResource(StringResource.settings_trending_languages).asString(),
            )
        },
        text = {
            Column {
                Text(
                    text = UiText.StringResource(StringResource.settings_languages_hint).asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                LanguageList(options, selected) { selected = it }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selected.toSet()) }, enabled = selected.isNotEmpty()) {
                Text(UiText.StringResource(StringResource.action_ok).asString())
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(UiText.StringResource(StringResource.action_cancel).asString())
            }
        },
        containerColor = VndbTheme.colors.surfaceCard,
    )
}

@Composable
private fun LanguageList(
    options: ImmutableList<SettingOptionUIModel>,
    selected: List<String>,
    onChange: (List<String>) -> Unit,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        options.forEach { option ->
            val isChecked = option.key in selected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = OptionRowMinHeight)
                    .toggleable(
                        value = isChecked,
                        role = Role.Checkbox,
                        onValueChange = { checked ->
                            onChange(if (checked) selected + option.key else selected - option.key)
                        },
                    ),
                horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = VndbTheme.colors.accent,
                    ),
                )
                Text(option.label.asString())
            }
        }
    }
}

@AdaptivePreviews
@Composable
private fun LanguagesDialogViewPreview() {
    VndbTheme {
        LanguagesDialogView(
            options = persistentListOf(
                SettingOptionUIModel("en", UiText.StringValue("English"), isSelected = true),
                SettingOptionUIModel("ja", UiText.StringValue("Japanese"), isSelected = true),
                SettingOptionUIModel("ko", UiText.StringValue("Korean"), isSelected = false),
            ),
            onConfirm = {},
            onDismiss = {},
        )
    }
}
