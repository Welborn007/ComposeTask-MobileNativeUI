package com.macdevelopers.shared.di

import com.macdevelopers.shared.data.local.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { Darwin.create() }
    single { createDataStore() }
}
