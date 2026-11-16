package com.zc.vndb.feature.detail.domain.repository

import com.zc.vndb.feature.detail.domain.model.CoverArt
import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.model.NovelCredits
import com.zc.vndb.feature.detail.domain.model.NovelDetail
import com.zc.vndb.feature.detail.domain.model.Quote
import com.zc.vndb.feature.detail.domain.model.ReleaseSummary
import com.zc.vndb.feature.detail.domain.model.ResultPage
import com.zc.vndb.feature.detail.domain.model.Screenshot

/** Each call backs one section of the page and is only made when that section is needed. */
interface DetailRepository {

    /** The Overview fields. */
    suspend fun getNovel(id: String): Result<NovelDetail>

    /** Every character of the VN, with their role in it (voice actors not filled in). */
    suspend fun getCharacters(novelId: String): Result<List<NovelCharacter>>

    /** Staff, staff editions and voice actors. */
    suspend fun getCredits(novelId: String): Result<NovelCredits>

    /** The VN's releases (up to 100), oldest first; a release on several platforms yields one edition each. */
    suspend fun getEditions(novelId: String): Result<List<Edition>>

    suspend fun getScreenshots(novelId: String): Result<List<Screenshot>>

    /** Covers from the VN's releases. */
    suspend fun getCovers(novelId: String): Result<List<CoverArt>>

    /** One page (1-based) of the VN's releases, oldest first. */
    suspend fun getReleases(
        novelId: String,
        page: Int,
        pageSize: Int,
    ): Result<ResultPage<ReleaseSummary>>

    /** One page (1-based) of the VN's quotes, highest score first. */
    suspend fun getQuotes(novelId: String, page: Int, pageSize: Int): Result<ResultPage<Quote>>
}
