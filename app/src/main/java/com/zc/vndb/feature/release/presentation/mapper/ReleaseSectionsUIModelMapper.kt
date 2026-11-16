package com.zc.vndb.feature.release.presentation.mapper

import com.zc.vndb.R
import com.zc.vndb.common.ui.component.linksrow.model.LinkUIModel
import com.zc.vndb.common.ui.mapper.bbCodeToPlainText
import com.zc.vndb.common.ui.resource.StringResource
import com.zc.vndb.common.ui.text.UiText
import com.zc.vndb.feature.release.domain.model.ReleaseDetail
import com.zc.vndb.feature.release.domain.model.ReleaseImageType
import com.zc.vndb.feature.release.presentation.model.ReleaseImageUIModel
import com.zc.vndb.feature.release.presentation.model.ReleaseLanguageUIModel
import com.zc.vndb.feature.release.presentation.model.ReleaseProducerUIModel
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** The release's page on vndb.org. */
fun releaseUrl(id: String): String = "https://vndb.org/$id"

fun ReleaseDetail.toLinks(): ImmutableList<LinkUIModel> = buildList {
    add(
        LinkUIModel(
            UiText.StringResource(R.string.detail_vndb_link, listOf(id)),
            releaseUrl(id),
            isVndb = true,
        ),
    )
    links.forEach { add(LinkUIModel(UiText.StringValue(it.label), it.url, isVndb = false)) }
}.toImmutableList()

/** Each language, with its own title only when it differs from the titles the hero already shows. */
fun ReleaseDetail.toLanguageUIModels(): ImmutableList<ReleaseLanguageUIModel> =
    languages.map { language ->
        ReleaseLanguageUIModel(
            code = language.code,
            name = UiText.StringValue(
                Locale.forLanguageTag(language.code).displayLanguage.ifBlank { language.code },
            ),
            title = language.title?.takeIf { it != title && it != altTitle }?.let(
                UiText::StringValue,
            ),
            isMachineTranslated = language.isMachineTranslated,
        )
    }.toImmutableList()

fun ReleaseDetail.toProducerUIModels(): ImmutableList<ReleaseProducerUIModel> =
    producers.map { producer ->
        ReleaseProducerUIModel(
            name = UiText.StringValue(producer.name),
            roles = listOfNotNull(
                UiText.StringResource(R.string.release_developer).takeIf { producer.isDeveloper },
                UiText.StringResource(R.string.release_publisher).takeIf { producer.isPublisher },
            ).toImmutableList(),
        )
    }.toImmutableList()

fun ReleaseDetail.toImageUIModels(): ImmutableList<ReleaseImageUIModel> = images.map { image ->
    ReleaseImageUIModel(
        url = image.image.url,
        thumbnailUrl = image.image.thumbnailUrl,
        typeLabel = UiText.StringResource(image.type.labelRes()),
        isSensitive = image.image.isSensitive,
    )
}.toImmutableList()

fun ReleaseDetail.toNotes(): UiText? = notes
    ?.bbCodeToPlainText(keepParagraphs = true, showSpoilers = true)
    ?.takeIf { it.isNotEmpty() }
    ?.let(UiText::StringValue)

private fun ReleaseImageType.labelRes(): StringResource = when (this) {
    ReleaseImageType.PackageFront -> R.string.release_image_front
    ReleaseImageType.PackageBack -> R.string.release_image_back
    ReleaseImageType.PackageContents -> R.string.release_image_contents
    ReleaseImageType.PackageSide -> R.string.release_image_side
    ReleaseImageType.PackageMedia -> R.string.release_image_media
    ReleaseImageType.Digital -> R.string.release_image_digital
    ReleaseImageType.Other -> R.string.release_image_other
}
