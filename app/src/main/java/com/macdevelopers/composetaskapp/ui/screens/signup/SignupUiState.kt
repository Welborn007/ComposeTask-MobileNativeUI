package com.macdevelopers.composetaskapp.ui.screens.signup

import com.macdevelopers.shared.domain.model.UserRole

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val nameErrorRes: Int? = null,
    val emailErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
    val isLoading: Boolean = false,
    val signupSuccess: Boolean = false,
    val networkError: Boolean = false
)
