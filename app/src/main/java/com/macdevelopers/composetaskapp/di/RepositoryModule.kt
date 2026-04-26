package com.macdevelopers.composetaskapp.di

import com.macdevelopers.shared.data.local.SharedAuthPreferences
import com.macdevelopers.shared.data.repository.VendorRepositoryImpl
import com.macdevelopers.shared.domain.repository.AuthRepository
import com.macdevelopers.shared.domain.repository.VendorRepository
import com.macdevelopers.shared.data.repository.AuthRepositoryImpl as SharedAuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        httpClient: HttpClient,
        sharedAuthPreferences: SharedAuthPreferences
    ): AuthRepository {
        return SharedAuthRepositoryImpl(
            httpClient = httpClient,
            authPreferencesProvider = { sharedAuthPreferences }
        )
    }

    @Provides
    @Singleton
    fun provideVendorRepository(
        httpClient: HttpClient
    ): VendorRepository {
        return VendorRepositoryImpl(httpClient)
    }
}
