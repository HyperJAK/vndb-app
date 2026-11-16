package com.zc.vndb.app.di

import com.zc.vndb.common.di.CommonModule
import com.zc.vndb.feature.browse.di.BrowseModule
import com.zc.vndb.feature.character.di.CharacterModule
import com.zc.vndb.feature.detail.di.DetailModule
import com.zc.vndb.feature.discover.di.DiscoverModule
import com.zc.vndb.feature.login.di.LoginModule
import com.zc.vndb.feature.profile.di.ProfileModule
import com.zc.vndb.feature.release.di.ReleaseModule
import org.koin.core.annotation.Module

/** Aggregates every Koin module in the app. Feature modules are added here as they land. */
@Module(
    includes = [
        CommonModule::class,
        DiscoverModule::class,
        BrowseModule::class,
        DetailModule::class,
        CharacterModule::class,
        ReleaseModule::class,
        ProfileModule::class,
        LoginModule::class,
    ],
)
class AppModule
