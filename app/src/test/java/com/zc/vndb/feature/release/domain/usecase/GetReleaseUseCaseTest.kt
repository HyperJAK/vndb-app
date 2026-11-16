package com.zc.vndb.feature.release.domain.usecase

import com.zc.vndb.common.domain.model.DomainError
import com.zc.vndb.common.domain.model.ExternalLink
import com.zc.vndb.feature.release.domain.model.ReleaseImage
import com.zc.vndb.feature.release.domain.model.ReleaseImageType
import com.zc.vndb.feature.release.domain.model.ReleaseLanguage
import com.zc.vndb.feature.release.domain.model.ReleaseSamples.cover
import com.zc.vndb.feature.release.domain.model.ReleaseSamples.novel
import com.zc.vndb.feature.release.domain.model.ReleaseSamples.release
import com.zc.vndb.feature.release.domain.repository.FakeReleaseRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class GetReleaseUseCaseTest {

    private val repository = FakeReleaseRepository()
    private val useCase = GetReleaseUseCase(repository)

    @Test
    fun `orders languages, images and VNs and keeps one link per site`() = runTest {
        repository.releases["r1"] = release(
            languages = listOf(
                ReleaseLanguage("en", null, isMain = false, isMachineTranslated = false),
                ReleaseLanguage("ja", "Title", isMain = true, isMachineTranslated = false),
            ),
            images = listOf(
                ReleaseImage(ReleaseImageType.Digital, cover("d")),
                ReleaseImage(ReleaseImageType.PackageBack, cover("b")),
                ReleaseImage(ReleaseImageType.PackageFront, cover("f")),
            ),
            novels = listOf(novel("v-new", year = 2015), novel("v-old", year = 2009)),
            links = listOf(
                ExternalLink("PlayAsia", "a"),
                ExternalLink("PlayAsia", "b"),
                ExternalLink("Official website", "c"),
            ),
        )

        val result = useCase("r1").getOrThrow()

        assertEquals(listOf("ja", "en"), result.languages.map { it.code })
        assertEquals(listOf("f", "b", "d"), result.images.map { it.image.url })
        assertEquals(listOf("v-old", "v-new"), result.novels.map { it.id })
        assertEquals(listOf("a", "c"), result.links.map { it.url })
    }

    @Test
    fun `propagates NotFound`() = runTest {
        assertEquals(DomainError.NotFound, useCase("missing").exceptionOrNull())
    }
}
