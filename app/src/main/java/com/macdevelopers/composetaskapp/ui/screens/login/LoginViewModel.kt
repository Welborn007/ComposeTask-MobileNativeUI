package com.macdevelopers.composetaskapp.ui.screens.login


import com.macdevelopers.composetaskapp.R
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.composetaskapp.domain.model.NetworkException
import com.macdevelopers.composetaskapp.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    // --- Input updates ---
    fun onUsernameChange(username: String) {
        _uiState.update {
            it.copy(
                username = username,
                usernameErrorRes = null
            )
        }
    }
    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordErrorRes = null
            )
        }
    }


    // --- Login action ---
    fun onLoginClicked() {
        val state = _uiState.value

        val usernameError = validateUsername(state.username)
        val passwordError = validatePassword(state.password)

        if (usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    usernameErrorRes = usernameError,
                    passwordErrorRes = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, networkError = false) }

            val result = loginUseCase(
                email = state.username,
                password = state.password
            )

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false,
                            loginSuccess = true)
                    }
                    // Navigation should be triggered by UI observing success
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkException

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            networkError = isNetworkError,
                            passwordErrorRes = if (isNetworkError) null else R.string.error_login_failed
                        )
                    }
                }
        }
    }

    // --- Validation helpers ---
    private fun validateUsername(username: String): Int? {
        if (username.isBlank()) {
            return R.string.error_email_required
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            return R.string.error_invalid_email
        }
        return null
    }

    private fun validatePassword(password: String): Int? {
        if (password.isBlank()) {
            return R.string.error_password_required
        }
        if (password.length < 6) {
            return R.string.error_password_min_length
        }
        return null
    }

    fun onLoginHandled() {
        _uiState.update { it.copy(loginSuccess = false) }
    }

}
