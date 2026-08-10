package com.macdevelopers.shared.data.remote

import com.macdevelopers.shared.data.remote.dto.ApiResponseDto
import com.macdevelopers.shared.data.remote.dto.CreateVendorDto
import com.macdevelopers.shared.data.remote.dto.LoginRequestDto
import com.macdevelopers.shared.data.remote.dto.LoginResponseDto
import com.macdevelopers.shared.data.remote.dto.LogoutRequestDto
import com.macdevelopers.shared.data.remote.dto.PaginatedResponseDto
import com.macdevelopers.shared.data.remote.dto.RefreshTokenRequestDto
import com.macdevelopers.shared.data.remote.dto.RefreshTokenResponseDto
import com.macdevelopers.shared.data.remote.dto.SignupRequestDto
import com.macdevelopers.shared.data.remote.dto.SignupResponseDto
import com.macdevelopers.shared.data.remote.dto.UsersResponseDto
import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.shared.domain.model.UserRole
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) {

    /**
     * Authentication APIs
     */
    suspend fun login(email: String, password: String): ApiResponseDto<LoginResponseDto> {
        return httpClient.post("${baseUrl}auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequestDto(email, password))
        }.body()
    }

    suspend fun signup(
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): ApiResponseDto<SignupResponseDto> {
        return httpClient.post("${baseUrl}auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(SignupRequestDto(name, email, password, role))
        }.body()
    }

    suspend fun refreshToken(refreshToken: String): ApiResponseDto<RefreshTokenResponseDto> {
        return httpClient.post("${baseUrl}auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequestDto(refreshToken))
        }.body()
    }

    suspend fun logout(refreshToken: String): ApiResponseDto<Unit> {
        return httpClient.post("${baseUrl}auth/logout") {
            contentType(ContentType.Application.Json)
            setBody(LogoutRequestDto(refreshToken))
        }.body()
    }

    /**
     * Users APIs
     */

    suspend fun getUserProfile(): ApiResponseDto<UsersResponseDto> {
        return httpClient.get("${baseUrl}users/me").body()
    }


    /**
     * Vendor APIs
     */
    suspend fun getVendors(): ApiResponseDto<PaginatedResponseDto<VendorDto>> {
        return httpClient.get("${baseUrl}vendors").body()
    }

    suspend fun getMyVendor(): ApiResponseDto<PaginatedResponseDto<VendorDto>> {
        return httpClient.get("${baseUrl}vendors/my").body()
    }

    suspend fun createVendor(createVendorDto: CreateVendorDto): ApiResponseDto<VendorDto> {
        return httpClient.post("${baseUrl}vendors") {
            contentType(ContentType.Application.Json)
            setBody(createVendorDto)
        }.body()
    }
}

