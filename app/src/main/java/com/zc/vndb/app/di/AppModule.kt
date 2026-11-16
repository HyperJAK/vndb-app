package com.zc.vndb.app.di

import com.zc.vndb.common.di.commonModule
import org.koin.dsl.module

/** Aggregates every Koin module in the app. Feature modules are added here as they land. */
val appModule = module {
    includes(commonModule)
}
