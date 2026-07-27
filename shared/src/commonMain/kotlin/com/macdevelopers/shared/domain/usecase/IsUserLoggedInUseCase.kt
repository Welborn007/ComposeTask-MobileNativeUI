package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository

class IsUserLoggedInUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        if (!repository.isLoggedIn()) return false
        
        return repository.ensureTokenFresh().fold(
            onSuccess = { true },
            onFailure = {
                repository.logout()
                false
            }
        )
    }
}
