package com.zc.vndb.feature.browse.presentation.component.filterbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.SwapVert
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
import com.zc.vndb.common.ui.component.selectablechip.SelectableChipView
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.browse.domain.model.AgeRating
import com.zc.vndb.feature.browse.domain.model.BrowseSort
import com.zc.vndb.feature.browse.presentation.model.SortOptionUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private val SortIconSize = 16.dp

/** All Ages / 18+ / Both chips on the left, sort menu on the right. */
@Composable
fun FilterBarView(
    ageRating: AgeRating,
    sort: BrowseSort,
    sortOptions: ImmutableList<SortOptionUIModel>,
    onAgeRatingSelect: (AgeRating) -> Unit,
    onSortSelect: (BrowseSort) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val ageSelected = TintedColors(
            container = VndbTheme.colors.accent,
            content = VndbTheme.colors.onAccent,
            border = VndbTheme.colors.accent,
        )
        SelectableChipView(
            label = UiText.StringResource(StringResource.browse_age_all),
            isSelected = ageRating == AgeRating.AllAges,
            onClick = { onAgeRatingSelect(AgeRating.AllAges) },
            selectedColors = ageSelected,
        )
        SelectableChipView(
            label = UiText.StringResource(StringResource.browse_age_adult),
            isSelected = ageRating == AgeRating.Adult,
            onClick = { onAgeRatingSelect(AgeRating.Adult) },
            selectedColors = ageSelected,
        )
        SelectableChipView(
            label = UiText.StringResource(StringResource.browse_age_any),
            isSelected = ageRating == AgeRating.Any,
            onClick = { onAgeRatingSelect(AgeRating.Any) },
            selectedColors = ageSelected,
        )
        Spacer(Modifier.weight(1f))
        SortMenu(sort = sort, options = sortOptions, onSelect = onSortSelect)
    }
}

@Composable
private fun SortMenu(
    sort: BrowseSort,
    options: ImmutableList<SortOptionUIModel>,
    onSelect: (BrowseSort) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        SortMenuAnchor(
            label = options.firstOrNull { it.sort == sort }?.label,
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
                    trailingIcon = if (option.sort == sort) {
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

/** The pill that shows the current sort and opens the menu. */
@Composable
private fun SortMenuAnchor(label: UiText?, onClick: () -> Unit) {
    val menuDescription = UiText.StringResource(StringResource.browse_sort_menu).asString()
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
            imageVector = Icons.Outlined.SwapVert,
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
private fun FilterBarViewPreview() {
    VndbTheme {
        FilterBarView(
            ageRating = AgeRating.AllAges,
            sort = BrowseSort.Popularity,
            sortOptions = persistentListOf(
                SortOptionUIModel(BrowseSort.Popularity, UiText.StringValue("Popularity")),
            ),
            onAgeRatingSelect = {},
            onSortSelect = {},
            modifier = Modifier.fillMaxWidth().padding(VndbTheme.spacing.lg),
        )
    }
}
