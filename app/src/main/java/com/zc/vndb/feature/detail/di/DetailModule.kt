package com.zc.vndb.feature.detail.di

import com.zc.vndb.feature.detail.data.remote.DetailService
import com.zc.vndb.feature.detail.data.repository.DetailRepositoryImpl
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import com.zc.vndb.feature.detail.domain.usecase.GetNovelCastUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelCoversUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelDetailUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelEditionsUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelQuotesUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelReleasesUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelScreenshotsUseCase
import com.zc.vndb.feature.detail.presentation.DetailViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailModule = module {
    // data
    singleOf(::DetailService)
    singleOf(::DetailRepositoryImpl) { bind<DetailRepository>() }

    // domain
    factoryOf(::GetNovelDetailUseCase)
    factoryOf(::GetNovelEditionsUseCase)
    factoryOf(::GetNovelCastUseCase)
    factoryOf(::GetNovelScreenshotsUseCase)
    factoryOf(::GetNovelCoversUseCase)
    factoryOf(::GetNovelReleasesUseCase)
    factoryOf(::GetNovelQuotesUseCase)

    // presentation
    viewModelOf(::DetailViewModel) // novelId comes from parametersOf
}
