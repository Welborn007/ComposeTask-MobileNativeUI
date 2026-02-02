package com.macdevelopers.composetaskapp.ui.screens.login


import com.macdevelopers.composetaskapp.R
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.macdevelopers.composetaskapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.update {
            it.copy(username = value, usernameErrorRes = null)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(password = value, passwordErrorRes = null)
        }
    }

    fun onLoginClicked() {
        val state = _uiState.value

        val usernameError = when {
            state.username.isBlank() ->
                R.string.error_email_required
            !Patterns.EMAIL_ADDRESS.matcher(state.username).matches() ->
                R.string.error_email_invalid
            else -> null
        }

        val passwordError = when {
            state.password.isBlank() ->
                R.string.error_password_required
            state.password.length < 6 ->
                R.string.error_password_length
            else -> null
        }

        if (usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    usernameErrorRes = usernameError,
                    passwordErrorRes = passwordError
                )
            }
            return
        }

        // Proceed with API call
        login(state.username, state.password)
    }

    private fun login(username: String, password: String) {
        // call use case here
    }
}

