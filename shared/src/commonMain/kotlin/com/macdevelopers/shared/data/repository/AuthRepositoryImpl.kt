package com.macdevelopers.shared.data.repository

import com.macdevelopers.shared.data.remote.dto.ApiResponseDto
import com.macdevelopers.shared.data.remote.dto.LoginRequestDto
import com.macdevelopers.shared.data.remote.dto.LoginResponseDto
import com.macdevelopers.shared.data.remote.dto.SignupRequestDto
import com.macdevelopers.shared.data.remote.dto.SignupResponseDto
import com.macdevelopers.shared.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val authPreferencesProvider: () -> AuthPreferencesBridge,
    private val baseUrl: String = "http://192.168.0.106:8080/api/"
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return authPreferencesProvider().isLoggedIn()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val response: ApiResponseDto<LoginResponseDto> = httpClient.post("${baseUrl}auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequestDto(email, password))
            }.body()

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
        password: String
    ): Result<String> {
        return try {
            val response: ApiResponseDto<SignupResponseDto> = httpClient.post("${baseUrl}auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(SignupRequestDto(name, email, password))
            }.body()

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
