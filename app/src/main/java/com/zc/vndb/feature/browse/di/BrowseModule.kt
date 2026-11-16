package com.zc.vndb.feature.browse.di

import com.zc.vndb.feature.browse.data.remote.BrowseService
import com.zc.vndb.feature.browse.data.repository.BrowseRepositoryImpl
import com.zc.vndb.feature.browse.domain.repository.BrowseRepository
import com.zc.vndb.feature.browse.domain.usecase.GetTagUseCase
import com.zc.vndb.feature.browse.domain.usecase.SearchNovelsUseCase
import com.zc.vndb.feature.browse.presentation.BrowseViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val browseModule = module {
    // data
    singleOf(::BrowseService)
    singleOf(::BrowseRepositoryImpl) { bind<BrowseRepository>() }

    // domain
    factoryOf(::SearchNovelsUseCase)
    factoryOf(::GetTagUseCase)

    // presentation
    // tagId is null for the Browse tab, so it is read with getOrNull.
    viewModel { params ->
        BrowseViewModel(
            tagId = params.getOrNull(),
            searchNovels = get(),
            getTag = get(),
        )
    }
}
