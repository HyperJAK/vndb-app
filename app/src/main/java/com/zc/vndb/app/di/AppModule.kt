package com.zc.vndb.app.di

import com.zc.vndb.common.di.commonModule
import com.zc.vndb.feature.browse.di.browseModule
import com.zc.vndb.feature.character.di.characterModule
import com.zc.vndb.feature.detail.di.detailModule
import com.zc.vndb.feature.discover.di.discoverModule
import com.zc.vndb.feature.login.di.loginModule
import com.zc.vndb.feature.profile.di.profileModule
import com.zc.vndb.feature.release.di.releaseModule
import org.koin.dsl.module

/** Aggregates every Koin module in the app. Feature modules are added here as they land. */
val appModule = module {
    includes(
        commonModule,
        discoverModule,
        browseModule,
        detailModule,
        characterModule,
        releaseModule,
        profileModule,
        loginModule,
    )
}
