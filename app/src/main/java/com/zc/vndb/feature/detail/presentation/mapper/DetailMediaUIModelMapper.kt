package com.zc.vndb.feature.detail.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.detail.domain.model.CoverArt
import com.zc.vndb.feature.detail.domain.model.Screenshot
import com.zc.vndb.feature.detail.presentation.model.ContentFilter
import com.zc.vndb.feature.detail.presentation.model.ContentToggleUIState
import com.zc.vndb.feature.detail.presentation.model.ImageViewerUIState
import com.zc.vndb.feature.detail.presentation.model.MediaImageUIModel
import com.zc.vndb.feature.detail.presentation.model.MediaUIModel
import com.zc.vndb.feature.detail.presentation.model.ScreenshotGroupUIModel
import com.zc.vndb.feature.detail.presentation.model.ViewerImageUIModel
import kotlin.math.roundToInt
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val PREVIEW_SIZE = 4
private const val MAX_LEVEL = 2

/**
 * Screenshots allowed by [filters]: a screenshot shows when both its sexual and its violence
 * level (VNDB's averages rounded to 0, 1 or 2) are switched on. Toggles carry per-level counts.
 */
fun List<Screenshot>.toMediaUIModel(filters: Set<ContentFilter>): MediaUIModel {
    val visible = filter { it.isAllowedBy(filters) }
    var index = 0
    val groups = visible.groupBy { it.releaseId }.map { (_, shots) ->
        ScreenshotGroupUIModel(
            title = shots.first().releaseTitle?.let(UiText::StringValue),
            screenshots = shots.map { it.toImageUIModel(index++) }.toImmutableList(),
        )
    }
    val hidden = size - visible.size
    return MediaUIModel(
        totalCount = size,
        sexualToggles = toggles(filters, sexual = true),
        violenceToggles = toggles(filters, sexual = false),
        groups = groups.toImmutableList(),
        preview = groups.flatMap { it.screenshots }.take(PREVIEW_SIZE).toImmutableList(),
        visibleCount = visible.size,
        hiddenLabel = hidden.takeIf { it > 0 }
            ?.let { UiText.StringPlural(R.plurals.detail_hidden_by_filters, it, listOf(it)) },
    )
}

/** The viewer over the screenshots [filters] allow, opened at [index]. */
fun List<Screenshot>.toViewer(filters: Set<ContentFilter>, index: Int): ImageViewerUIState =
    ImageViewerUIState(
        images = filter { it.isAllowedBy(filters) }
            .map {
                ViewerImageUIModel(
                    url = it.image.url,
                    caption = it.releaseTitle?.let(UiText::StringValue),
                )
            }
            .toImmutableList(),
        startIndex = index,
    )

fun List<CoverArt>.toCoverUIModels(): ImmutableList<MediaImageUIModel> = mapIndexed {
        index,
        cover,
    ->
    MediaImageUIModel(
        url = cover.image.url,
        thumbnailUrl = cover.image.thumbnailUrl,
        caption = UiText.StringValue(cover.releaseTitle),
        isSensitive = cover.image.isSensitive,
        index = index,
    )
}.toImmutableList()

fun List<CoverArt>.toViewer(index: Int): ImageViewerUIState = ImageViewerUIState(
    images = map {
        ViewerImageUIModel(
            url = it.image.url,
            caption = UiText.StringValue(it.releaseTitle),
        )
    }.toImmutableList(),
    startIndex = index,
)

private fun List<Screenshot>.toggles(
    filters: Set<ContentFilter>,
    sexual: Boolean,
): ImmutableList<ContentToggleUIState> =
    ContentFilter.entries.filter { it.isSexual == sexual }.map { filter ->
        val count = count { (if (sexual) it.sexualLevel else it.violenceLevel) == filter.level }
        ContentToggleUIState(
            filter = filter,
            label = UiText.StringResource(filter.labelRes(), listOf(count)),
            isSelected = filter in filters,
        )
    }.toImmutableList()

private fun Screenshot.toImageUIModel(index: Int) = MediaImageUIModel(
    url = image.url,
    thumbnailUrl = image.thumbnailUrl,
    caption = releaseTitle?.let(UiText::StringValue),
    // Shown only because the reader allowed its level, so it is not blurred.
    isSensitive = false,
    index = index,
)

private fun Screenshot.isAllowedBy(filters: Set<ContentFilter>): Boolean =
    filters.any { it.isSexual && it.level == sexualLevel } &&
        filters.any { !it.isSexual && it.level == violenceLevel }

private val Screenshot.sexualLevel: Int get() = image.sexual.roundToInt().coerceIn(0, MAX_LEVEL)

private val Screenshot.violenceLevel: Int get() = image.violence.roundToInt().coerceIn(0, MAX_LEVEL)

private fun ContentFilter.labelRes(): StringResource = when (this) {
    ContentFilter.Safe -> R.string.detail_filter_safe
    ContentFilter.Suggestive -> R.string.detail_filter_suggestive
    ContentFilter.Explicit -> R.string.detail_filter_explicit
    ContentFilter.Tame -> R.string.detail_filter_tame
    ContentFilter.Violent -> R.string.detail_filter_violent
    ContentFilter.Brutal -> R.string.detail_filter_brutal
}
