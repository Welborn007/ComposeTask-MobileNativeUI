package com.macdevelopers.composetaskapp.data.repository

import android.content.Context
import com.macdevelopers.composetaskapp.R
import com.macdevelopers.composetaskapp.data.connectivity.ConnectivityChecker
import com.macdevelopers.composetaskapp.data.local.preferences.AuthPreferences
import com.macdevelopers.composetaskapp.data.remote.AuthApi
import com.macdevelopers.composetaskapp.data.remote.dto.ApiResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.LoginRequestDto
import com.macdevelopers.composetaskapp.data.remote.dto.LoginResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.SignupRequestDto
import com.macdevelopers.composetaskapp.data.remote.dto.SignupResponseDto
import com.macdevelopers.composetaskapp.domain.model.NetworkException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @Mock
    private lateinit var authApi: AuthApi

    @Mock
    private lateinit var authPreferences: AuthPreferences

    @Mock
    private lateinit var connectivityChecker: ConnectivityChecker

    @Mock
    private lateinit var context: Context

    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(context.getString(R.string.error_no_internet))
            .thenReturn("No internet connection. Please check your network.")
        whenever(context.getString(R.string.error_login_generic))
            .thenReturn("Login failed")
        repository = AuthRepositoryImpl(authApi, authPreferences, connectivityChecker, context)
    }

    @Test
    fun `test login with no internet returns network error`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(false)

        val result = repository.login("test@example.com", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NetworkException)
    }

    @Test
    fun `test login success saves token and marks logged in`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(true)
        val token = "test_token_123"
        val loginResponse = LoginResponseDto(
            token,
            message = "Login successful"
        )
        val apiResponse = ApiResponseDto(true, "Login successful", loginResponse)

        whenever(authApi.login(any<LoginRequestDto>())).thenReturn(apiResponse)

        val result = repository.login("test@example.com", "password123")

        assertTrue(result.isSuccess)
        assertEquals(token, result.getOrNull())
        verify(authPreferences).saveToken(token)
        verify(authPreferences).setLoggedIn(true)
    }

    @Test
    fun `test login failure returns server error`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(true)
        val apiResponse = ApiResponseDto<LoginResponseDto>(false, "Invalid credentials", null)

        whenever(authApi.login(any<LoginRequestDto>())).thenReturn(apiResponse)

        val result = repository.login("test@example.com", "wrongpassword")

        assertTrue(result.isFailure)
    }

    @Test
    fun `test signup with no internet returns network error`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(false)

        val result = repository.signup("John Doe", "test@example.com", "password123")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NetworkException)
    }

    @Test
    fun `test signup success saves token and marks logged in`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(true)
        val token = "test_token_123"
        val signupResponse = SignupResponseDto(token, "Signup successful")
        val apiResponse = ApiResponseDto(true, "Signup successful", signupResponse)

        whenever(authApi.signup(any<SignupRequestDto>())).thenReturn(apiResponse)

        val result = repository.signup("John Doe", "test@example.com", "password123")

        assertTrue(result.isSuccess)
        assertEquals(token, result.getOrNull())
        verify(authPreferences).saveToken(token)
        verify(authPreferences).setLoggedIn(true)
    }

    @Test
    fun `test signup failure returns server error`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(true)
        val apiResponse = ApiResponseDto<SignupResponseDto>(false, "Email already exists", null)

        whenever(authApi.signup(any<SignupRequestDto>())).thenReturn(apiResponse)

        val result = repository.signup("John Doe", "existing@example.com", "password123")

        assertTrue(result.isFailure)
    }

    @Test
    fun `test isLoggedIn returns saved login status`() = runTest {
        whenever(authPreferences.isLoggedIn()).thenReturn(flowOf(true))

        val result = repository.isLoggedIn()

        assertTrue(result)
    }

    @Test
    fun `test login exception handling`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(true)
        whenever(authApi.login(any<LoginRequestDto>())).thenThrow(RuntimeException("Network error"))

        val result = repository.login("test@example.com", "password123")

        assertTrue(result.isFailure)
    }

    @Test
    fun `test signup exception handling`() = runTest {
        whenever(connectivityChecker.isConnected()).thenReturn(true)
        whenever(authApi.signup(any<SignupRequestDto>())).thenThrow(RuntimeException("Network error"))

        val result = repository.signup("John Doe", "test@example.com", "password123")

        assertTrue(result.isFailure)
    }
}
