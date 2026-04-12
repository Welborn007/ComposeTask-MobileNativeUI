package com.macdevelopers.composetaskapp.ui.screens.signup

import com.macdevelopers.composetaskapp.R
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.composetaskapp.domain.model.NetworkException
import com.macdevelopers.composetaskapp.domain.usecase.SignupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    // --- Input updates ---
    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                nameErrorRes = null
            )
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailErrorRes = null
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

    // --- Signup action ---
    fun onSignupClicked() {
        val state = _uiState.value

        val nameError = validateName(state.name)
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        if (nameError != null || emailError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    nameErrorRes = nameError,
                    emailErrorRes = emailError,
                    passwordErrorRes = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, networkError = false) }

            val result = signupUseCase(
                name = state.name,
                email = state.email,
                password = state.password
            )

            result
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoading = false,
                            signupSuccess = true)
                    }
                    // Navigation should be triggered by UI observing success
                }
                .onFailure { exception ->
                    val isNetworkError = exception is NetworkException

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            networkError = isNetworkError,
                            passwordErrorRes = if (isNetworkError) null else R.string.error_signup_failed
                        )
                    }
                }
        }
    }

    // --- Validation helpers ---
    private fun validateName(name: String): Int? {
        if (name.isBlank()) {
            return R.string.error_name_required
        }
        if (name.length < 2) {
            return R.string.error_name_min_length
        }
        return null
    }

    private fun validateEmail(email: String): Int? {
        if (email.isBlank()) {
            return R.string.error_email_required
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

    fun onSignupHandled() {
        _uiState.update { it.copy(signupSuccess = false) }
    }

}
