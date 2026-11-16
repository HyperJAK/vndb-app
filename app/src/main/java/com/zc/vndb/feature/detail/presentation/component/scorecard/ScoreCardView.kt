package com.zc.vndb.feature.detail.presentation.component.scorecard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zc.vndb.R
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.common.ui.theme.VndbTheme
import com.zc.vndb.common.ui.util.AdaptivePreviews
import com.zc.vndb.feature.detail.presentation.component.scorecard.model.ScoreUIModel
import com.zc.vndb.feature.detail.presentation.component.sectioncard.SectionCardView

/** Bayesian score, vote count and plain average. */
@Composable
fun ScoreCardView(model: ScoreUIModel, modifier: Modifier = Modifier) {
    SectionCardView(modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(VndbTheme.spacing.sm),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = model.ratingLabel.asString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = VndbTheme.colors.score,
                    )
                    Text(
                        text = stringResource(R.string.detail_score_out_of),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = VndbTheme.spacing.xs),
                    )
                }
                Text(
                    text = model.votesLabel.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            model.averageLabel?.let { average ->
                Text(
                    text = average.asString(),
                    style = MaterialTheme.typography.labelMedium,
                    color = VndbTheme.colors.accent,
                )
            }
        }
    }
}

@AdaptivePreviews
@Composable
private fun ScoreCardViewPreview() {
    VndbTheme {
        ScoreCardView(
            model = ScoreUIModel(
                ratingLabel = UiText.StringValue("9.02"),
                votesLabel = UiText.StringValue("17,205 recorded votes"),
                averageLabel = UiText.StringValue("Average 9.02"),
            ),
            modifier = Modifier.padding(VndbTheme.spacing.lg),
        )
    }
}
