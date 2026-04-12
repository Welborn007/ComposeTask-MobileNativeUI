package com.macdevelopers.composetaskapp.ui.screens.signup

import com.macdevelopers.composetaskapp.MainDispatcherRule
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.domain.model.NetworkException
import com.macdevelopers.composetaskapp.domain.usecase.SignupUseCase
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
class SignupViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var signupUseCase: SignupUseCase

    private lateinit var viewModel: SignupViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = SignupViewModel(signupUseCase)
    }

    @Test
    fun `test initial state`() {
        val state = viewModel.uiState.value
        assertEquals("", state.name)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertFalse(state.isLoading)
        assertFalse(state.signupSuccess)
        assertFalse(state.networkError)
    }

    @Test
    fun `test onNameChange updates state`() {
        viewModel.onNameChange("John Doe")
        assertEquals("John Doe", viewModel.uiState.value.name)
    }

    @Test
    fun `test onEmailChange updates state`() {
        viewModel.onEmailChange("test@example.com")
        assertEquals("test@example.com", viewModel.uiState.value.email)
    }

    @Test
    fun `test onPasswordChange updates state`() {
        viewModel.onPasswordChange("password123")
        assertEquals("password123", viewModel.uiState.value.password)
    }

    @Test
    fun `test onNameChange clears error`() {
        viewModel.onNameChange("")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()
        assertTrue(viewModel.uiState.value.nameErrorRes != null)

        viewModel.onNameChange("John Doe")
        assertTrue(viewModel.uiState.value.nameErrorRes == null)
    }

    @Test
    fun `test name validation - blank name shows error`() {
        viewModel.onNameChange("")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_name_required, viewModel.uiState.value.nameErrorRes)
    }

    @Test
    fun `test name validation - name less than 2 characters shows error`() {
        viewModel.onNameChange("J")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_name_min_length, viewModel.uiState.value.nameErrorRes)
    }

    @Test
    fun `test email validation - blank email shows error`() {
        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_email_required, viewModel.uiState.value.emailErrorRes)
    }

    @Test
    fun `test email validation - invalid email shows error`() {
        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("invalidemail")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_invalid_email, viewModel.uiState.value.emailErrorRes)
    }

    @Test
    fun `test password validation - blank password shows error`() {
        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_password_required, viewModel.uiState.value.passwordErrorRes)
    }

    @Test
    fun `test password validation - password less than 6 characters shows error`() {
        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("pass")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_password_min_length, viewModel.uiState.value.passwordErrorRes)
    }

    @Test
    fun `test valid credentials - signup success`() = runTest {
        val token = "test_token_123"
        whenever(signupUseCase(
            name = "John Doe",
            email = "test@example.com",
            password = "password123"
        )).thenReturn(Result.success(token))

        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        // Wait for async operation
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.signupSuccess)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `test network error shows network banner`() = runTest {
        val networkError = NetworkException("No internet connection. Please check your network.")
        whenever(signupUseCase(
            name = "John Doe",
            email = "test@example.com",
            password = "password123"
        )).thenReturn(Result.failure(networkError))

        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        // Wait for async operation
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.networkError)
        assertFalse(viewModel.uiState.value.isLoading)
        assertTrue(viewModel.uiState.value.passwordErrorRes == null)
    }

    @Test
    fun `test server error shows password error`() = runTest {
        val serverError = Exception("Email already exists")
        whenever(signupUseCase(
            name = "John Doe",
            email = "existing@example.com",
            password = "password123"
        )).thenReturn(Result.failure(serverError))

        viewModel.onNameChange("John Doe")
        viewModel.onEmailChange("existing@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        // Wait for async operation
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.networkError)
        assertEquals(R.string.error_signup_failed, viewModel.uiState.value.passwordErrorRes)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `test onSignupHandled clears success state`() {
        viewModel.onSignupClicked()
        viewModel.onSignupHandled()
        assertFalse(viewModel.uiState.value.signupSuccess)
    }

    @Test
    fun `test multiple validation errors show all errors`() {
        viewModel.onNameChange("")
        viewModel.onEmailChange("")
        viewModel.onPasswordChange("")
        viewModel.onSignupClicked()

        assertEquals(R.string.error_name_required, viewModel.uiState.value.nameErrorRes)
        assertEquals(R.string.error_email_required, viewModel.uiState.value.emailErrorRes)
        assertEquals(R.string.error_password_required, viewModel.uiState.value.passwordErrorRes)
    }

    @Test
    fun `test valid name - minimum 2 characters`() {
        viewModel.onNameChange("Jo")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("password123")
        viewModel.onSignupClicked()

        assertTrue(viewModel.uiState.value.nameErrorRes == null)
    }
}
