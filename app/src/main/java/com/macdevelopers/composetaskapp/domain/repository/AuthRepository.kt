package com.macdevelopers.composetaskapp.domain.repository

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean
    suspend fun login(
        email: String,
        password: String
    ): Result<String>
}
