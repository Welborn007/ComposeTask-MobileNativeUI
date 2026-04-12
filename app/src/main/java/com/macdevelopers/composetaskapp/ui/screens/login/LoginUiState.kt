package com.macdevelopers.composetaskapp.ui.screens.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val networkError: Boolean = false
)
