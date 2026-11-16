package com.zc.vndb.app.di

import org.koin.core.annotation.KoinApplication

/** Entry point for `startKoin<AppKoin>`: loads [AppModule] and every module it includes. */
@KoinApplication(modules = [AppModule::class])
object AppKoin
