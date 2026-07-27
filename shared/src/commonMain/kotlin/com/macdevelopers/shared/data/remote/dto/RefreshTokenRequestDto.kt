package com.macdevelopers.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String
)

