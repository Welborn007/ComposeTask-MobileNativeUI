package com.macdevelopers.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponseDto(
    val token: String,
    val message: String,
    val refreshToken: String,
    val expiresIn: Int,
    val tokenType: String
)

