package com.macdevelopers.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponseDto(
    val token: String,
    val message: String
)
