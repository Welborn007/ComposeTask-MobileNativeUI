package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository

class SignupUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Result<String> {
        return repository.signup(
            name = name,
            email = email,
            password = password
        )
    }
}
