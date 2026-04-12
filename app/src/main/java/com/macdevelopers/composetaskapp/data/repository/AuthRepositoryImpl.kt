package com.macdevelopers.composetaskapp.data.repository

import android.content.Context
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.data.connectivity.ConnectivityChecker
import com.macdevelopers.composetaskapp.data.local.preferences.AuthPreferences
import com.macdevelopers.composetaskapp.data.remote.AuthApi
import com.macdevelopers.composetaskapp.data.remote.dto.LoginRequestDto
import com.macdevelopers.composetaskapp.data.remote.dto.SignupRequestDto
import com.macdevelopers.composetaskapp.domain.model.NetworkException
import com.macdevelopers.composetaskapp.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authPreferences: AuthPreferences,
    private val connectivityChecker: ConnectivityChecker,
    @ApplicationContext private val context: Context
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return authPreferences.isLoggedIn().first()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        // Check connectivity before making API call
        if (!connectivityChecker.isConnected()) {
            return@withContext Result.failure(
                NetworkException(context.getString(R.string.error_no_internet))
            )
        }

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
                authPreferences.setLoggedIn(true)

                Result.success(token)
            } else {
                Result.failure(
                    IllegalStateException(
                        response.message.ifBlank {
                            context.getString(R.string.error_login_generic)
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Result<String> = withContext(Dispatchers.IO) {
        // Check connectivity before making API call
        if (!connectivityChecker.isConnected()) {
            return@withContext Result.failure(
                NetworkException(context.getString(R.string.error_no_internet))
            )
        }

        try {
            val response = authApi.signup(
                SignupRequestDto(
                    name = name,
                    email = email,
                    password = password
                )
            )

            if (response.success && response.data != null) {
                val token = response.data.token

                authPreferences.saveToken(token)
                authPreferences.setLoggedIn(true)

                Result.success(token)
            } else {
                Result.failure(
                    IllegalStateException(
                        response.message.ifBlank {
                            context.getString(R.string.error_login_generic)
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}