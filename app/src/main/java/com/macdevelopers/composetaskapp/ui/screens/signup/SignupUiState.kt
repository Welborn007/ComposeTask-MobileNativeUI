package com.macdevelopers.composetaskapp.ui.screens.signup

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val nameErrorRes: Int? = null,
    val emailErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
    val isLoading: Boolean = false,
    val signupSuccess: Boolean = false,
    val networkError: Boolean = false
)
