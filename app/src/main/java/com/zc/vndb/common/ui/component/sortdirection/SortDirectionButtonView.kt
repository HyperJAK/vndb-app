package com.zc.vndb.common.ui.component.sortdirection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val ButtonSize = 32.dp
private val IconSize = 16.dp

/** A round button showing the sort direction (up = ascending); tapping it reverses the order. */
@Composable
fun SortDirectionButtonView(
    isAscending: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val description = UiText.StringResource(
        if (isAscending) StringResource.cd_sort_ascending else StringResource.cd_sort_descending,
    ).asString()
    Box(
        modifier = modifier
            .size(ButtonSize)
            .clip(CircleShape)
            .background(VndbTheme.colors.surfaceCard)
            .border(VndbTheme.dimens.borderWidth, VndbTheme.colors.borderSubtle, CircleShape)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (isAscending) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
            contentDescription = null,
            tint = VndbTheme.colors.accent,
            modifier = Modifier.size(IconSize),
        )
    }
}

@AdaptivePreviews
@Composable
private fun SortDirectionButtonViewPreview() {
    VndbTheme { SortDirectionButtonView(isAscending = true, onClick = {}) }
}
