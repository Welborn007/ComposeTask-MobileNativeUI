package com.macdevelopers.composetaskapp.domain.usecase

import com.macdevelopers.composetaskapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
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
