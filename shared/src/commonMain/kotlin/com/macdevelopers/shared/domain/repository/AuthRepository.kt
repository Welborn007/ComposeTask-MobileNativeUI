package com.macdevelopers.shared.domain.repository

import com.macdevelopers.shared.data.remote.dto.UsersResponseDto
import com.macdevelopers.shared.domain.model.UserRole

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun login(
        email: String,
        password: String
    ): Result<String>
    suspend fun signup(
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): Result<String>
    suspend fun refreshToken(): Result<String>
    suspend fun ensureTokenFresh(): Result<Unit>
    suspend fun logout()
    suspend fun getUserProfile(): Result<UsersResponseDto>
    suspend fun getSavedUserData(): Result<UsersResponseDto?>
}
