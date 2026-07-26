package com.macdevelopers.shared.domain.repository

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
}
