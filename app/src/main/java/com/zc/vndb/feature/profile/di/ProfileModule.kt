package com.zc.vndb.feature.profile.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.zc.vndb.feature.profile.data.local.KeystoreTokenCipher
import com.zc.vndb.feature.profile.data.local.SessionLocalDataSource
import com.zc.vndb.feature.profile.data.local.TokenCipher
import com.zc.vndb.feature.profile.data.remote.ProfileService
import com.zc.vndb.feature.profile.data.repository.ProfileRepositoryImpl
import com.zc.vndb.feature.profile.data.repository.SessionRepositoryImpl
import com.zc.vndb.feature.profile.domain.repository.ProfileRepository
import com.zc.vndb.feature.profile.domain.repository.SessionRepository
import com.zc.vndb.feature.profile.domain.usecase.GetListEntriesUseCase
import com.zc.vndb.feature.profile.domain.usecase.GetProfileUseCase
import com.zc.vndb.feature.profile.domain.usecase.LogInUseCase
import com.zc.vndb.feature.profile.domain.usecase.LogOutUseCase
import com.zc.vndb.feature.profile.domain.usecase.ObserveSessionUseCase
import com.zc.vndb.feature.profile.presentation.LoginViewModel
import com.zc.vndb.feature.profile.presentation.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    // data
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create { androidContext().preferencesDataStoreFile("session") }
    }
    single<TokenCipher> { KeystoreTokenCipher() }
    singleOf(::SessionLocalDataSource)
    singleOf(::ProfileService)
    singleOf(::SessionRepositoryImpl) { bind<SessionRepository>() }
    singleOf(::ProfileRepositoryImpl) { bind<ProfileRepository>() }

    // domain
    factoryOf(::ObserveSessionUseCase)
    factoryOf(::LogInUseCase)
    factoryOf(::LogOutUseCase)
    factoryOf(::GetProfileUseCase)
    factoryOf(::GetListEntriesUseCase)

    // presentation
    viewModelOf(::ProfileViewModel)
    viewModelOf(::LoginViewModel)
}
