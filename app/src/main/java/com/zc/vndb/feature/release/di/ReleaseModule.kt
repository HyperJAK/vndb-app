package com.zc.vndb.feature.release.di

import com.zc.vndb.feature.release.data.remote.ReleaseService
import com.zc.vndb.feature.release.data.repository.ReleaseRepositoryImpl
import com.zc.vndb.feature.release.domain.repository.ReleaseRepository
import com.zc.vndb.feature.release.domain.usecase.GetReleaseUseCase
import com.zc.vndb.feature.release.presentation.ReleaseViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val releaseModule = module {
    // data
    singleOf(::ReleaseService)
    singleOf(::ReleaseRepositoryImpl) { bind<ReleaseRepository>() }

    // domain
    factoryOf(::GetReleaseUseCase)

    // presentation
    viewModelOf(::ReleaseViewModel) // releaseId comes from parametersOf
}
