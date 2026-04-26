package com.macdevelopers.composetaskapp.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository
import com.macdevelopers.shared.domain.usecase.LoginUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when repository login succeeds`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val expectedToken = "token123"
        whenever(authRepository.login(email, password)).thenReturn(Result.success(expectedToken))

        val result = loginUseCase(email, password)

        assertTrue(result.isSuccess)
        assertEquals(expectedToken, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository login fails`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val expectedException = Exception("Login failed")
        whenever(authRepository.login(email, password)).thenReturn(Result.failure(expectedException))

        val result = loginUseCase(email, password)

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}
