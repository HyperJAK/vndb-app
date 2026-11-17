package com.zc.vndb.feature.detail.presentation.component.detailtabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zc.vndb.common.ui.component.sectioncard.SectionCardView
import com.zc.vndb.common.ui.component.selectablechip.SelectableChipView
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.TintedColors
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.presentation.model.DetailTab
import com.zc.vndb.feature.detail.presentation.model.DetailTabUIModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** Scrollable chip-style tabs inside a card. */
@Composable
fun DetailTabRowView(
    tabs: ImmutableList<DetailTabUIModel>,
    selected: DetailTab,
    onSelect: (DetailTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColors = TintedColors(
        container = VndbTheme.colors.accent,
        content = VndbTheme.colors.onAccent,
        border = VndbTheme.colors.accent,
    )
    SectionCardView(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
        ) {
            items(items = tabs, key = { it.tab.name }) { tab ->
                SelectableChipView(
                    label = tab.label,
                    isSelected = tab.tab == selected,
                    onClick = { onSelect(tab.tab) },
                    selectedColors = selectedColors,
                )
            }
        }
    }
}

@AdaptivePreviews
@Composable
private fun DetailTabRowViewPreview() {
    VndbTheme {
        DetailTabRowView(
            tabs = DetailTab.entries.map {
                DetailTabUIModel(
                    it,
                    UiText.StringValue(it.name),
                )
            }.toImmutableList(),
            selected = DetailTab.Overview,
            onSelect = {},
        )
    }
}
