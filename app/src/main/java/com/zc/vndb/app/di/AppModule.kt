package com.zc.vndb.app.di

import com.zc.vndb.common.di.commonModule
import com.zc.vndb.feature.browse.di.browseModule
import com.zc.vndb.feature.discover.di.discoverModule
import org.koin.dsl.module

/** Aggregates every Koin module in the app. Feature modules are added here as they land. */
val appModule = module {
    includes(commonModule, discoverModule, browseModule)
}
