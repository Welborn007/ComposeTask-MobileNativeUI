package com.macdevelopers.shared.data.repository

import com.macdevelopers.shared.data.remote.ApiService
import com.macdevelopers.shared.domain.model.UserRole
import com.macdevelopers.shared.domain.repository.AuthRepository
import com.macdevelopers.shared.util.getCurrentTimeMillis

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
                val refreshToken = response.data.refreshToken
                val expiresIn = response.data.expiresIn.toIntOrNull() ?: 3600

                authPreferencesProvider().saveToken(token)
                authPreferencesProvider().saveRefreshToken(refreshToken)

                // Calculate expiry time
                val expiryTimeMillis = getCurrentTimeMillis() + (expiresIn * 1000L)
                authPreferencesProvider().saveTokenExpiryTime(expiryTimeMillis)

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

    override suspend fun refreshToken(): Result<String> {
        return try {
            val refreshTokenValue = authPreferencesProvider().getRefreshTokenValue()
                ?: return Result.failure(Exception("Refresh token not found"))

            val response = apiService.refreshToken(refreshTokenValue)

            if (response.success && response.data != null) {
                val newToken = response.data.token
                val newRefreshToken = response.data.refreshToken
                val expiresIn = response.data.expiresIn

                authPreferencesProvider().saveToken(newToken)
                authPreferencesProvider().saveRefreshToken(newRefreshToken)

                // Calculate new expiry time
                val expiryTimeMillis = getCurrentTimeMillis() + (expiresIn * 1000L)
                authPreferencesProvider().saveTokenExpiryTime(expiryTimeMillis)

                Result.success(newToken)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        authPreferencesProvider().clearAll()
    }

    /**
     * Helper function to ensure token is fresh before making API calls
     */
    override suspend fun ensureTokenFresh(): Result<Unit> {
        return try {
            val expiryTime = authPreferencesProvider().getTokenExpiryTime()
            val currentTime = getCurrentTimeMillis()

            // If token is expired or about to expire (within 60 seconds), refresh it
            if (expiryTime != null && (currentTime + 60000).compareTo(expiryTime) >= 0) {
                return refreshToken().map { Unit }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

interface AuthPreferencesBridge {
    suspend fun isLoggedIn(): Boolean
    suspend fun saveToken(token: String)
    suspend fun setLoggedIn(loggedIn: Boolean)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getRefreshTokenValue(): String?
    suspend fun saveTokenExpiryTime(expiryTimeMillis: Long)
    suspend fun getTokenExpiryTime(): Long?
    suspend fun clearAll()
}
