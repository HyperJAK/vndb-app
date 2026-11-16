package com.zc.vndb.feature.release.presentation.component.releasehero.mapper

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
            UiText.StringResource(StringResource.release_official),
            ReleaseFlagTone.Accent,
        )
    } else {
        flag(UiText.StringResource(StringResource.release_unofficial), ReleaseFlagTone.Caution)
    },
    flag(
        UiText.StringResource(StringResource.release_patch),
        ReleaseFlagTone.Caution,
    ).takeIf { isPatch },
    flag(
        UiText.StringResource(StringResource.release_freeware),
        ReleaseFlagTone.Safe,
    ).takeIf { isFreeware },
    flag(
        UiText.StringResource(StringResource.release_uncensored),
        ReleaseFlagTone.Neutral,
    ).takeIf { isUncensored == true },
    flag(
        UiText.StringResource(StringResource.release_adult_content),
        ReleaseFlagTone.Adult,
    ).takeIf { hasAdultContent },
    minAge?.let { age ->
        if (age == ALL_AGES) {
            ReleaseFlagUIModel(
                UiText.StringResource(StringResource.detail_age_all),
                ReleaseFlagTone.Safe,
            )
        } else {
            ReleaseFlagUIModel(
                UiText.StringResource(StringResource.detail_age_min, listOf(age)),
                ReleaseFlagTone.Neutral,
            )
        }
    },
)

private fun flag(label: UiText, tone: ReleaseFlagTone) = ReleaseFlagUIModel(
    label,
    tone,
)

private fun ReleaseDetail.facts(): List<FactUIModel> = listOfNotNull(
    FactUIModel(UiText.StringResource(StringResource.release_released), releaseDate.toIsoLabel()),
    platforms.takeIf { it.isNotEmpty() }?.let { codes ->
        FactUIModel(
            UiText.StringResource(StringResource.release_platforms),
            UiText.StringValue(codes.joinToString(", ") { platformName(it) }),
        )
    },
    media.firstOrNull()?.let {
        FactUIModel(
            UiText.StringResource(StringResource.release_media),
            it.toLabel(),
        )
    },
    engine?.let {
        FactUIModel(
            UiText.StringResource(StringResource.release_engine),
            UiText.StringValue(it),
        )
    },
    resolution?.let {
        FactUIModel(
            UiText.StringResource(StringResource.release_resolution),
            UiText.StringResource(
                StringResource.release_resolution_value,
                listOf(it.width, it.height),
            ),
        )
    },
    voicing?.let {
        FactUIModel(
            UiText.StringResource(StringResource.release_voiced),
            it.toLabel(),
        )
    },
    catalog?.let {
        FactUIModel(
            UiText.StringResource(StringResource.release_catalog),
            UiText.StringValue(it),
        )
    },
    gtin?.let {
        FactUIModel(
            UiText.StringResource(StringResource.release_barcode),
            UiText.StringValue(it),
        )
    },
)

/** "DVD", "Blu-ray disc × 2", "Internet download". */
private fun ReleaseMedium.toLabel(): UiText {
    val name = toMediumLabel()
    return if (quantity <= 1) {
        name
    } else {
        UiText.StringResource(
            StringResource.release_medium_quantity,
            listOf(name, quantity),
        )
    }
}

private fun ReleaseMedium.toMediumLabel(): UiText = UiText.StringResource(
    when (code) {
        "blr" -> StringResource.release_medium_blr
        "mrt" -> StringResource.release_medium_mrt
        "cas" -> StringResource.release_medium_cas
        "cd" -> StringResource.release_medium_cd
        "dc" -> StringResource.release_medium_dc
        "dvd" -> StringResource.release_medium_dvd
        "flp" -> StringResource.release_medium_flp
        "gdr" -> StringResource.release_medium_gdr
        "in" -> StringResource.release_medium_in
        "mem" -> StringResource.release_medium_mem
        "nod" -> StringResource.release_medium_nod
        "umd" -> StringResource.release_medium_umd
        else -> StringResource.release_medium_other
    },
)

private fun Voicing.toLabel(): UiText = UiText.StringResource(
    when (this) {
        Voicing.NotVoiced -> StringResource.release_voiced_none
        Voicing.EroScenesOnly -> StringResource.release_voiced_ero
        Voicing.Partial -> StringResource.release_voiced_partial
        Voicing.Full -> StringResource.release_voiced_full
    },
)
