package com.zc.vndb.feature.release.presentation.component.releasehero.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.component.factline.model.FactUIModel
import com.zc.vndb.common.ui.mapper.platformName
import com.zc.vndb.common.ui.mapper.toIsoLabel
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.domain.model.ReleaseDetail
import com.zc.vndb.feature.release.domain.model.ReleaseImageType
import com.zc.vndb.feature.release.domain.model.ReleaseMedium
import com.zc.vndb.feature.release.domain.model.Voicing
import com.zc.vndb.feature.release.presentation.component.releasehero.model.ReleaseFlagTone
import com.zc.vndb.feature.release.presentation.component.releasehero.model.ReleaseFlagUIModel
import com.zc.vndb.feature.release.presentation.component.releasehero.model.ReleaseHeroUIModel
import kotlinx.collections.immutable.toImmutableList

private const val ALL_AGES = 0

fun ReleaseDetail.toHeroUIModel(): ReleaseHeroUIModel {
    val cover = (
        images.firstOrNull { it.type == ReleaseImageType.PackageFront }
            ?: images.firstOrNull()
        )?.image
    return ReleaseHeroUIModel(
        title = UiText.StringValue(title),
        altTitle = altTitle?.let(UiText::StringValue),
        imageUrl = cover?.url,
        isImageSensitive = cover?.isSensitive ?: false,
        flags = flags().toImmutableList(),
        facts = facts().toImmutableList(),
    )
}

private fun ReleaseDetail.flags(): List<ReleaseFlagUIModel> = listOfNotNull(
    if (isOfficial) {
        flag(
            R.string.release_official,
            ReleaseFlagTone.Accent,
        )
    } else {
        flag(R.string.release_unofficial, ReleaseFlagTone.Caution)
    },
    flag(R.string.release_patch, ReleaseFlagTone.Caution).takeIf { isPatch },
    flag(R.string.release_freeware, ReleaseFlagTone.Safe).takeIf { isFreeware },
    flag(R.string.release_uncensored, ReleaseFlagTone.Neutral).takeIf { isUncensored == true },
    flag(R.string.release_adult_content, ReleaseFlagTone.Adult).takeIf { hasAdultContent },
    minAge?.let { age ->
        if (age == ALL_AGES) {
            ReleaseFlagUIModel(UiText.StringResource(R.string.detail_age_all), ReleaseFlagTone.Safe)
        } else {
            ReleaseFlagUIModel(
                UiText.StringResource(R.string.detail_age_min, listOf(age)),
                ReleaseFlagTone.Neutral,
            )
        }
    },
)

private fun flag(label: StringResource, tone: ReleaseFlagTone) = ReleaseFlagUIModel(
    UiText.StringResource(label),
    tone,
)

private fun ReleaseDetail.facts(): List<FactUIModel> = listOfNotNull(
    FactUIModel(R.string.release_released, releaseDate.toIsoLabel()),
    platforms.takeIf { it.isNotEmpty() }?.let { codes ->
        FactUIModel(
            R.string.release_platforms,
            UiText.StringValue(codes.joinToString(", ") { platformName(it) }),
        )
    },
    media.firstOrNull()?.let { FactUIModel(R.string.release_media, it.toLabel()) },
    engine?.let { FactUIModel(R.string.release_engine, UiText.StringValue(it)) },
    resolution?.let {
        FactUIModel(
            R.string.release_resolution,
            UiText.StringResource(R.string.release_resolution_value, listOf(it.width, it.height)),
        )
    },
    voicing?.let { FactUIModel(R.string.release_voiced, UiText.StringResource(it.labelRes())) },
    catalog?.let { FactUIModel(R.string.release_catalog, UiText.StringValue(it)) },
    gtin?.let { FactUIModel(R.string.release_barcode, UiText.StringValue(it)) },
)

/** "DVD", "Blu-ray disc × 2", "Internet download". */
private fun ReleaseMedium.toLabel(): UiText {
    val name = UiText.StringResource(mediumRes())
    return if (quantity <= 1) {
        name
    } else {
        UiText.StringResource(
            R.string.release_medium_quantity,
            listOf(name, quantity),
        )
    }
}

private fun ReleaseMedium.mediumRes(): StringResource = when (code) {
    "blr" -> R.string.release_medium_blr
    "mrt" -> R.string.release_medium_mrt
    "cas" -> R.string.release_medium_cas
    "cd" -> R.string.release_medium_cd
    "dc" -> R.string.release_medium_dc
    "dvd" -> R.string.release_medium_dvd
    "flp" -> R.string.release_medium_flp
    "gdr" -> R.string.release_medium_gdr
    "in" -> R.string.release_medium_in
    "mem" -> R.string.release_medium_mem
    "nod" -> R.string.release_medium_nod
    "umd" -> R.string.release_medium_umd
    else -> R.string.release_medium_other
}

private fun Voicing.labelRes(): StringResource = when (this) {
    Voicing.NotVoiced -> R.string.release_voiced_none
    Voicing.EroScenesOnly -> R.string.release_voiced_ero
    Voicing.Partial -> R.string.release_voiced_partial
    Voicing.Full -> R.string.release_voiced_full
}
