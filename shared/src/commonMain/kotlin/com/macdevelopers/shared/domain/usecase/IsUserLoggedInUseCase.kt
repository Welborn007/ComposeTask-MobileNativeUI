package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository

class IsUserLoggedInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isLoggedIn()
    }
}
