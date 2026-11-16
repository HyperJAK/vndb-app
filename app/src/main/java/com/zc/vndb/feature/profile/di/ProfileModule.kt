package com.zc.vndb.feature.profile.di

import com.zc.vndb.feature.profile.data.remote.ProfileService
import com.zc.vndb.feature.profile.data.repository.ProfileRepositoryImpl
import com.zc.vndb.feature.profile.domain.repository.ProfileRepository
import com.zc.vndb.feature.profile.domain.usecase.GetListEntriesUseCase
import com.zc.vndb.feature.profile.domain.usecase.GetProfileUseCase
import com.zc.vndb.feature.profile.domain.usecase.LogOutUseCase
import com.zc.vndb.feature.profile.domain.usecase.ObserveSessionUseCase
import com.zc.vndb.feature.profile.presentation.ProfileViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    // data
    singleOf(::ProfileService)
    singleOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }

    // domain
    factoryOf(::ObserveSessionUseCase)
    factoryOf(::LogOutUseCase)
    factoryOf(::GetProfileUseCase)
    factoryOf(::GetListEntriesUseCase)

    // presentation
    viewModelOf(::ProfileViewModel)
}
