package com.zc.vndb.common.ui.component.factline

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zc.vndb.common.ui.component.factline.model.FactUIModel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews

private val DefaultLabelWidth = 88.dp

/** A "Label: value" line with the labels aligned in a column. */
@Composable
fun FactLineView(
    fact: FactUIModel,
    modifier: Modifier = Modifier,
    labelWidth: Dp = DefaultLabelWidth,
) {
    Row(modifier = modifier) {
        Text(
            text = fact.label.asString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(labelWidth),
        )
        Text(
            text = fact.value.asString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@AdaptivePreviews
@Composable
private fun FactLineViewPreview() {
    VndbTheme {
        FactLineView(
            fact = FactUIModel(
                UiText.StringResource(StringResource.character_height),
                UiText.StringValue("160 cm"),
            ),
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
