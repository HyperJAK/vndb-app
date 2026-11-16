package com.zc.vndb.feature.login.di

import com.zc.vndb.feature.login.data.remote.LoginService
import com.zc.vndb.feature.login.data.repository.LoginRepositoryImpl
import com.zc.vndb.feature.login.domain.repository.LoginRepository
import com.zc.vndb.feature.login.domain.usecase.LogInUseCase
import com.zc.vndb.feature.login.presentation.LoginViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val loginModule = module {
    // data
    singleOf(::LoginService)
    singleOf(::LoginRepositoryImpl) { bind<LoginRepository>() }

    // domain
    factoryOf(::LogInUseCase)

    // presentation
    viewModelOf(::LoginViewModel)
}
