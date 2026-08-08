package com.macdevelopers.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponseDto(
    val id: String,
    val name: String,
    val email: String,
    val role: String
)