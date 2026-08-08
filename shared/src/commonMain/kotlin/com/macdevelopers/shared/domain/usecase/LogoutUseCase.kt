package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository

class LogoutUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
