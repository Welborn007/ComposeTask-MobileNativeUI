package com.macdevelopers.composetaskapp.domain.usecase

import com.macdevelopers.shared.domain.repository.AuthRepository
import com.macdevelopers.shared.domain.usecase.SignupUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SignupUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var signupUseCase: SignupUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        signupUseCase = SignupUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when repository signup succeeds`() = runTest {
        val name = "John Doe"
        val email = "test@example.com"
        val password = "password123"
        val expectedToken = "token123"
        whenever(authRepository.signup(name, email, password)).thenReturn(Result.success(expectedToken))

        val result = signupUseCase(name, email, password)

        assertTrue(result.isSuccess)
        assertEquals(expectedToken, result.getOrNull())
    }

    @Test
    fun `invoke should return failure when repository signup fails`() = runTest {
        val name = "John Doe"
        val email = "test@example.com"
        val password = "password123"
        val expectedException = Exception("Signup failed")
        whenever(authRepository.signup(name, email, password)).thenReturn(Result.failure(expectedException))

        val result = signupUseCase(name, email, password)

        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}
