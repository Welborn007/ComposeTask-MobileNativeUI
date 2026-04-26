package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<String> {
        return authRepository.login(
            email = email,
            password = password
        )
    }
}
