package com.zc.vndb.feature.discover.di

import com.zc.vndb.feature.discover.data.remote.DiscoverService
import com.zc.vndb.feature.discover.data.repository.DiscoverRepositoryImpl
import com.zc.vndb.feature.discover.domain.repository.DiscoverRepository
import com.zc.vndb.feature.discover.domain.usecase.GetDatabaseStatsUseCase
import com.zc.vndb.feature.discover.domain.usecase.GetFeaturedNovelUseCase
import com.zc.vndb.feature.discover.domain.usecase.GetTopRatedNovelsUseCase
import com.zc.vndb.feature.discover.domain.usecase.GetTrendingReleasesUseCase
import com.zc.vndb.feature.discover.domain.usecase.RollRandomNovelUseCase
import com.zc.vndb.feature.discover.presentation.DiscoverViewModel
import kotlin.random.Random
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val discoverModule = module {
    // data
    singleOf(::DiscoverService)
    singleOf(::DiscoverRepositoryImpl) { bind<DiscoverRepository>() }

    // domain
    single<Random> { Random.Default }
    factoryOf(::GetTopRatedNovelsUseCase)
    factoryOf(::GetFeaturedNovelUseCase)
    factoryOf(::GetTrendingReleasesUseCase)
    factoryOf(::GetDatabaseStatsUseCase)
    factoryOf(::RollRandomNovelUseCase)

    // presentation
    viewModelOf(::DiscoverViewModel)
}
