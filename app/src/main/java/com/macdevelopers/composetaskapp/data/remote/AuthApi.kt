package com.macdevelopers.composetaskapp.data.remote

import com.macdevelopers.composetaskapp.data.remote.dto.ApiResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.LoginRequestDto
import com.macdevelopers.composetaskapp.data.remote.dto.LoginResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.SignupRequestDto
import com.macdevelopers.composetaskapp.data.remote.dto.SignupResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): ApiResponseDto<LoginResponseDto>

    @POST("auth/signup")
    suspend fun signup(
        @Body request: SignupRequestDto
    ): ApiResponseDto<SignupResponseDto>
}