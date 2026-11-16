package com.zc.vndb.feature.discover.di

import kotlin.random.Random
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.zc.vndb.feature.discover")
class DiscoverModule {
    @Single
    fun random(): Random = Random.Default
}
