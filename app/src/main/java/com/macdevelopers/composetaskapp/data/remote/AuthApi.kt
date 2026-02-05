package com.macdevelopers.composetaskapp.data.remote
import com.macdevelopers.composetaskapp.data.remote.dto.ApiResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.LoginRequestDto
import com.macdevelopers.composetaskapp.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): ApiResponseDto<LoginResponseDto>
}