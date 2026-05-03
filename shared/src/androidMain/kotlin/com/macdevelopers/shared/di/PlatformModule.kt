package com.macdevelopers.shared.di

import com.macdevelopers.shared.data.local.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { createDataStore(androidContext()) }
}
