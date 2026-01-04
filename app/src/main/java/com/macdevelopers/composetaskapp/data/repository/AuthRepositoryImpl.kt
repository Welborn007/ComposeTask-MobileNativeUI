package com.macdevelopers.composetaskapp.data.repository

import com.macdevelopers.composetaskapp.data.local.AuthPreferences
import com.macdevelopers.composetaskapp.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val prefs: AuthPreferences
) : AuthRepository {

    override suspend fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn()
    }

    override suspend fun login() {
        prefs.setLoggedIn(true)
    }
}