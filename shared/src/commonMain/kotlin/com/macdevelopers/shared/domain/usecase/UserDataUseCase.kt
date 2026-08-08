package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.data.remote.dto.UsersResponseDto
import com.macdevelopers.shared.domain.repository.AuthRepository

class UserDataUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): UsersResponseDto {
        return repository.getSavedUserData().fold(
            onSuccess = { it ?: throw IllegalStateException("User data not found") },
            onFailure = { throw it }
        )
    }
}
