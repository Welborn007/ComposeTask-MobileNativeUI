package com.macdevelopers.composetaskapp.di

import com.macdevelopers.shared.domain.repository.AuthRepository
import com.macdevelopers.shared.domain.usecase.IsUserLoggedInUseCase
import com.macdevelopers.shared.domain.usecase.LoginUseCase
import com.macdevelopers.shared.domain.usecase.SignupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
    fun provideSignupUseCase(
        repository: AuthRepository
    ): SignupUseCase {
        return SignupUseCase(repository)
    }

    @Provides
    fun provideIsUserLoggedInUseCase(
        repository: AuthRepository
    ): IsUserLoggedInUseCase {
        return IsUserLoggedInUseCase(repository)
    }
}
