package com.macdevelopers.shared.data.remote

import com.macdevelopers.shared.data.local.SharedAuthPreferences
import com.macdevelopers.shared.util.getCurrentTimeMillis
import io.ktor.client.plugins.api.createClientPlugin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val refreshMutex = Mutex()

fun createTokenRefreshPlugin(
    authPreferences: SharedAuthPreferences?,
    apiServiceProvider: () -> ApiService?
) = createClientPlugin("TokenRefreshPlugin") {
    onRequest { request, _ ->
        val url = request.url.buildString()
        // Skip refresh logic for authentication endpoints to avoid infinite loops
        if (url.contains("auth/login") || url.contains("auth/signup") || url.contains("auth/refresh")) {
            return@onRequest
        }

        authPreferences?.let { prefs ->
            apiServiceProvider()?.let { api ->
                refreshMutex.withLock {
                    try {
                        val expiryTime = prefs.getTokenExpiryTime()
                        val currentTime = getCurrentTimeMillis()

                        // Refresh token if expired or about to expire (within 60 seconds)
                        if (expiryTime != null && (currentTime + 60000).compareTo(expiryTime) >= 0) {
                            try {
                                val refreshToken = prefs.getRefreshTokenValue()
                                if (refreshToken != null) {
                                    val response = api.refreshToken(refreshToken)
                                    if (response.success && response.data != null) {
                                        val newToken = response.data.token
                                        val newRefreshToken = response.data.refreshToken
                                        val newExpiresIn = response.data.expiresIn

                                        prefs.saveToken(newToken)
                                        prefs.saveRefreshToken(newRefreshToken)
                                        val newExpiryTimeMillis = getCurrentTimeMillis() + (newExpiresIn * 1000L)
                                        prefs.saveTokenExpiryTime(newExpiryTimeMillis)
                                    }
                                }
                            } catch (e: Exception) {
                                // Log error but don't throw - let the request proceed with old token
                                println("Token refresh failed: ${e.message}")
                            }
                        }

                        // Add authorization header
                        val token = prefs.getToken().first()
                        if (token != null) {
                            request.headers["Authorization"] = "Bearer $token"
                        }
                    } catch (e: Exception) {
                        println("Token plugin error: ${e.message}")
                    }
                }
            }
        }
    }
}

