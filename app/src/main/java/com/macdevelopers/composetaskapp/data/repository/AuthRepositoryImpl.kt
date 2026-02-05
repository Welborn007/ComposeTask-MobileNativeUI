package com.macdevelopers.composetaskapp.data.repository

import com.macdevelopers.composetaskapp.data.local.preferences.AuthPreferences
import com.macdevelopers.composetaskapp.data.remote.AuthApi
import com.macdevelopers.composetaskapp.data.remote.dto.LoginRequestDto
import com.macdevelopers.composetaskapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return authPreferences.isLoggedIn()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = authApi.login(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )

            if (response.success && response.data != null) {
                val token = response.data.token

                authPreferences.saveToken(token)

                Result.success(token)
            } else {
                Result.failure(
                    IllegalStateException(
                        response.message.ifBlank {
                            "Login failed"
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}