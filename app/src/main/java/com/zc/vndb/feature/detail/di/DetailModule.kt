package com.zc.vndb.feature.detail.di

import com.zc.vndb.feature.detail.data.remote.DetailService
import com.zc.vndb.feature.detail.data.repository.DetailRepositoryImpl
import com.zc.vndb.feature.detail.domain.repository.DetailRepository
import com.zc.vndb.feature.detail.domain.usecase.GetNovelCharactersUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelDetailUseCase
import com.zc.vndb.feature.detail.domain.usecase.GetNovelEditionsUseCase
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
    factoryOf(::GetNovelCharactersUseCase)
    factoryOf(::GetNovelEditionsUseCase)

    // presentation
    viewModelOf(::DetailViewModel) // novelId comes from parametersOf
}
