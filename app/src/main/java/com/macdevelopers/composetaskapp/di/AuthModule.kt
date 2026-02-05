package com.macdevelopers.composetaskapp.di

import com.macdevelopers.composetaskapp.data.local.preferences.AuthPreferences
import com.macdevelopers.composetaskapp.data.repository.AuthRepositoryImpl
import com.macdevelopers.composetaskapp.domain.repository.AuthRepository
import com.macdevelopers.composetaskapp.domain.usecase.IsUserLoggedInUseCase
import com.macdevelopers.composetaskapp.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideLoginUseCase(
        repository: AuthRepository
    ): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideIsUserLoggedInUseCase(
        repository: AuthRepository
    ): IsUserLoggedInUseCase {
        return IsUserLoggedInUseCase(repository)
    }
}
