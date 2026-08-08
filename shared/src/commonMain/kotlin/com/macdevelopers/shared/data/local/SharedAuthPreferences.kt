package com.macdevelopers.shared.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.macdevelopers.shared.data.remote.dto.UsersResponseDto
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

    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_ROLE_KEY = stringPreferencesKey("user_role")

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

    override suspend fun saveUserData(usersResponseDto: UsersResponseDto) {
        dataStore.edit { prefs ->
            prefs[USER_NAME_KEY] = usersResponseDto.name
            prefs[USER_EMAIL_KEY] = usersResponseDto.email
            prefs[USER_ROLE_KEY] = usersResponseDto.role
            prefs[USER_ID_KEY] = usersResponseDto.id
        }
    }

    override suspend fun getUserData(): UsersResponseDto? {
        return dataStore.data.map { prefs ->
            val name = prefs[USER_NAME_KEY]
            val email = prefs[USER_EMAIL_KEY]
            val role = prefs[USER_ROLE_KEY]
            val id = prefs[USER_ID_KEY]

            if (name != null && email != null && role != null && id != null) {
                UsersResponseDto(id, name, email, role)
            } else {
                null
            }
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
