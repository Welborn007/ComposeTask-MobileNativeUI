package com.macdevelopers.shared.di

import com.macdevelopers.shared.data.local.SharedAuthPreferences
import com.macdevelopers.shared.data.local.createDataStore
import com.macdevelopers.shared.data.remote.createHttpClient
import com.macdevelopers.shared.data.repository.AuthRepositoryImpl
import com.macdevelopers.shared.data.repository.VendorRepositoryImpl
import com.macdevelopers.shared.domain.repository.AuthRepository
import com.macdevelopers.shared.domain.repository.VendorRepository
import com.macdevelopers.shared.domain.usecase.IsUserLoggedInUseCase
import com.macdevelopers.shared.domain.usecase.LoginUseCase
import com.macdevelopers.shared.domain.usecase.SignupUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

val commonModule = module {
    single<HttpClient> { createHttpClient(get()) }
    
    single { SharedAuthPreferences(get()) }
    
    single<AuthRepository> { 
        AuthRepositoryImpl(
            httpClient = get(),
            authPreferencesProvider = { get<SharedAuthPreferences>() }
        ) 
    }
    
    single<VendorRepository> { VendorRepositoryImpl(get()) }

    // Use Cases
    factory { LoginUseCase(get()) }
    factory { SignupUseCase(get()) }
    factory { IsUserLoggedInUseCase(get()) }
}

fun initKoin(appDeclaration: org.koin.dsl.KoinAppDeclaration = {}) = 
    org.koin.core.context.startKoin {
        appDeclaration()
        modules(commonModule, platformModule())
    }

// Helper for iOS
fun initKoin() = initKoin {}
