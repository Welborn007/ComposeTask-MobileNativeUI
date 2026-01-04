package com.macdevelopers.composetaskapp.domain.usecase

import com.macdevelopers.composetaskapp.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        // later: API call
        repository.login()
    }
}
