package com.macdevelopers.composetaskapp.ui.screens.login

import com.macdevelopers.composetaskapp.MainDispatcherRule
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.domain.model.NetworkException
import com.macdevelopers.composetaskapp.domain.usecase.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var loginUseCase: LoginUseCase

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = LoginViewModel(loginUseCase)
    }

    @Test
    fun `test initial state`() {
        val state = viewModel.uiState.value
        assertEquals("", state.username)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertFalse(state.loginSuccess)
        assertFalse(state.networkError)
    }

    @Test
    fun `test onUsernameChange updates state`() {
        viewModel.onUsernameChange("test@example.com")
        assertEquals("test@example.com", viewModel.uiState.value.username)
    }

    @Test
    fun `test onPasswordChange updates state`() {
        viewModel.onPasswordChange("password123")
        assertEquals("password123", viewModel.uiState.value.password)
    }

    @Test
    fun `test onUsernameChange clears error`() {
        viewModel.onUsernameChange("")
        viewModel.onPasswordChange("password")
        viewModel.onLoginClicked()
        assertTrue(viewModel.uiState.value.usernameErrorRes != null)

        viewModel.onUsernameChange("test@example.com")
        assertTrue(viewModel.uiState.value.usernameErrorRes == null)
    }

    @Test
    fun `test onPasswordChange clears error`() {
        viewModel.onPasswordChange("")
        viewModel.onUsernameChange("test@example.com")
        viewModel.onLoginClicked()
        assertTrue(viewModel.uiState.value.passwordErrorRes != null)

        viewModel.onPasswordChange("password123")
        assertTrue(viewModel.uiState.value.passwordErrorRes == null)
    }

    @Test
    fun `test email validation - blank email shows error`() {
        viewModel.onUsernameChange("")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClicked()

        assertEquals(R.string.error_email_required, viewModel.uiState.value.usernameErrorRes)
    }

    @Test
    fun `test email validation - invalid email shows error`() {
        viewModel.onUsernameChange("invalidemail")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClicked()

        assertEquals(R.string.error_invalid_email, viewModel.uiState.value.usernameErrorRes)
    }

    @Test
    fun `test password validation - blank password shows error`() {
        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("")
        viewModel.onLoginClicked()

        assertEquals(R.string.error_password_required, viewModel.uiState.value.passwordErrorRes)
    }

    @Test
    fun `test password validation - password less than 6 characters shows error`() {
        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("pass")
        viewModel.onLoginClicked()

        assertEquals(R.string.error_password_min_length, viewModel.uiState.value.passwordErrorRes)
    }

    @Test
    fun `test valid credentials - login success`() = runTest {
        val token = "test_token_123"
        whenever(loginUseCase(
            email = "test@example.com",
            password = "password123"
        )).thenReturn(Result.success(token))

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClicked()

        // Wait for async operation
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.loginSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `test network error shows network banner`() = runTest {
        val networkError = NetworkException("No internet connection. Please check your network.")
        whenever(loginUseCase(
            email = "test@example.com",
            password = "password123"
        )).thenReturn(Result.failure(networkError))

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onLoginClicked()

        // Wait for async operation
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.networkError)
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.passwordErrorRes == null)
    }

    @Test
    fun `test server error shows password error`() = runTest {
        val serverError = Exception("Invalid email or password")
        whenever(loginUseCase(
            email = "test@example.com",
            password = "wrongpassword"
        )).thenReturn(Result.failure(serverError))

        viewModel.onUsernameChange("test@example.com")
        viewModel.onPasswordChange("wrongpassword")
        viewModel.onLoginClicked()

        // Wait for async operation
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.networkError)
        assertEquals(R.string.error_login_failed, viewModel.uiState.value.passwordErrorRes)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `test onLoginHandled clears success state`() {
        viewModel.onLoginClicked()
        viewModel.onLoginHandled()
        assertFalse(viewModel.uiState.value.loginSuccess)
    }

    @Test
    fun `test multiple validation errors show all errors`() {
        viewModel.onUsernameChange("")
        viewModel.onPasswordChange("")
        viewModel.onLoginClicked()

        assertEquals(R.string.error_email_required, viewModel.uiState.value.usernameErrorRes)
        assertEquals(R.string.error_password_required, viewModel.uiState.value.passwordErrorRes)
    }
}
