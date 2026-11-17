package com.zc.vndb.feature.profile.presentation.component.listsortmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.domain.model.ListSort
import com.zc.vndb.common.ui.component.sortdirection.SortDirectionButtonView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.profile.presentation.model.SortOptionUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val SortIconSize = 16.dp

/**
 * The current list order as a pill (same look as Browse's sort menu) that opens the choices, and a
 * round button showing the direction (up = ascending) that reverses it.
 */
@Composable
fun ListSortMenuView(
    options: ImmutableList<SortOptionUIModel>,
    isAscending: Boolean,
    onSelect: (ListSort) -> Unit,
    onDirectionToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SortMenu(options, onSelect)
        SortDirectionButtonView(isAscending, onDirectionToggle)
    }
}

@Composable
private fun SortMenu(options: ImmutableList<SortOptionUIModel>, onSelect: (ListSort) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        SortAnchor(
            label = options.firstOrNull { it.isSelected }?.label,
            onClick = { expanded = true },
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = VndbTheme.colors.surfaceElevated,
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option.label.asString(),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    trailingIcon = if (option.isSelected) {
                        {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                tint = VndbTheme.colors.accent,
                            )
                        }
                    } else {
                        null
                    },
                    onClick = {
                        expanded = false
                        onSelect(option.sort)
                    },
                )
            }
        }
    }
}

@Composable
private fun SortAnchor(label: UiText?, onClick: () -> Unit) {
    val menuDescription = UiText.StringResource(StringResource.cd_sort_list).asString()
    val shape = VndbTheme.shapes.pill
    Row(
        modifier = Modifier
            .heightIn(min = VndbTheme.dimens.filterChipHeight)
            .clip(shape)
            .background(VndbTheme.colors.surfaceCard)
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, shape)
            .clickable(role = Role.DropdownList, onClick = onClick)
            .semantics { contentDescription = menuDescription }
            .padding(start = VndbTheme.spacing.md, end = VndbTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Sort,
            contentDescription = null,
            tint = VndbTheme.colors.accent,
            modifier = Modifier.size(SortIconSize),
        )
        Text(
            text = label?.asString().orEmpty(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Icon(
            Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@AdaptivePreviews
@Composable
private fun ListSortMenuViewPreview() {
    VndbTheme {
        ListSortMenuView(
            options = persistentListOf(
                SortOptionUIModel(ListSort.Title, UiText.StringValue("Title"), isSelected = true),
                SortOptionUIModel(
                    ListSort.Updated,
                    UiText.StringValue("Recently updated"),
                    isSelected = false,
                ),
            ),
            isAscending = true,
            onSelect = {},
            onDirectionToggle = {},
        )
    }
}
