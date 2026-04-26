package com.macdevelopers.shared.domain.repository

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun login(
        email: String,
        password: String
    ): Result<String>
    suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Result<String>
}
