package com.macdevelopers.shared.data.remote.dto

import com.macdevelopers.shared.domain.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequestDto(
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole
)
