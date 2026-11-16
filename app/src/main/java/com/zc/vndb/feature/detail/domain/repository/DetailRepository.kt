package com.zc.vndb.feature.detail.domain.repository

import com.zc.vndb.feature.detail.domain.model.Edition
import com.zc.vndb.feature.detail.domain.model.NovelCharacter
import com.zc.vndb.feature.detail.domain.model.NovelDetail

interface DetailRepository {

    suspend fun getNovel(id: String): Result<NovelDetail>

    /** Every character of the VN, with their role in it (voice actors not filled in). */
    suspend fun getCharacters(novelId: String): Result<List<NovelCharacter>>

    /** The VN's releases (up to 100), oldest first; a release on several platforms yields one edition each. */
    suspend fun getEditions(novelId: String): Result<List<Edition>>
}
