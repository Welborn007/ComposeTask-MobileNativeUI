package com.macdevelopers.composetaskapp.domain.usecase

import com.macdevelopers.composetaskapp.domain.repository.AuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsUserLoggedInUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        isUserLoggedInUseCase = IsUserLoggedInUseCase(authRepository)
    }

    @Test
    fun `invoke should return true when repository isLoggedIn returns true`() = runTest {
        whenever(authRepository.isLoggedIn()).thenReturn(true)

        val result = isUserLoggedInUseCase()

        assertTrue(result)
    }

    @Test
    fun `invoke should return false when repository isLoggedIn returns false`() = runTest {
        whenever(authRepository.isLoggedIn()).thenReturn(false)

        val result = isUserLoggedInUseCase()

        assertFalse(result)
    }
}
