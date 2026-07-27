package com.macdevelopers.shared.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.macdevelopers.shared.data.repository.AuthPreferencesBridge
import com.macdevelopers.shared.util.getCurrentTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SharedAuthPreferences(
    private val dataStore: DataStore<Preferences>
) : AuthPreferencesBridge {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    private val TOKEN_EXPIRY_TIME_KEY = stringPreferencesKey("token_expiry_time")
    private val LOGGED_IN_KEY = booleanPreferencesKey("logged_in")

    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { prefs ->
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun saveTokenExpiryTime(expiryTimeMillis: Long) {
        dataStore.edit { prefs ->
            prefs[TOKEN_EXPIRY_TIME_KEY] = expiryTimeMillis.toString()
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }
    }

    override suspend fun getRefreshTokenValue(): String? {
        return dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN_KEY]
        }.first()
    }

    override suspend fun getTokenExpiryTime(): Long? {
        return dataStore.data.map { prefs ->
            prefs[TOKEN_EXPIRY_TIME_KEY]?.toLongOrNull()
        }.first()
    }

    fun isTokenExpired(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            val expiryTime = prefs[TOKEN_EXPIRY_TIME_KEY]?.toLongOrNull()
            if (expiryTime != null) {
                getCurrentTimeMillis() >= expiryTime
            } else {
                false
            }
        }
    }

    override suspend fun setLoggedIn(loggedIn: Boolean) {
        dataStore.edit { prefs ->
            prefs[LOGGED_IN_KEY] = loggedIn
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return isLoggedInFlow().first()
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    suspend fun clearToken() {
        dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
            prefs.remove(TOKEN_EXPIRY_TIME_KEY)
        }
    }

    fun isLoggedInFlow(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[LOGGED_IN_KEY] ?: false
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
