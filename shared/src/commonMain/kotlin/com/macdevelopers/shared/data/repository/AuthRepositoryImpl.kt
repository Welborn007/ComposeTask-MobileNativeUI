package com.macdevelopers.shared.data.repository

import com.macdevelopers.shared.data.remote.ApiService
import com.macdevelopers.shared.domain.model.UserRole
import com.macdevelopers.shared.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val authPreferencesProvider: () -> AuthPreferencesBridge,
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return authPreferencesProvider().isLoggedIn()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val response = apiService.login(email, password)

            if (response.success && response.data != null) {
                val token = response.data.token
                authPreferencesProvider().saveToken(token)
                authPreferencesProvider().setLoggedIn(true)
                Result.success(token)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): Result<String> {
        return try {
            val response = apiService.signup(name, email, password, role)

            if (response.success && response.data != null) {
                val token = response.data.token
                authPreferencesProvider().saveToken(token)
                authPreferencesProvider().setLoggedIn(true)
                Result.success(token)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

interface AuthPreferencesBridge {
    suspend fun isLoggedIn(): Boolean
    suspend fun saveToken(token: String)
    suspend fun setLoggedIn(loggedIn: Boolean)
}
