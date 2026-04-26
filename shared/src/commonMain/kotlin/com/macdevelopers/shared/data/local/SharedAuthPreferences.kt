package com.macdevelopers.shared.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.macdevelopers.shared.data.repository.AuthPreferencesBridge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SharedAuthPreferences(
    private val dataStore: DataStore<Preferences>
) : AuthPreferencesBridge {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val LOGGED_IN_KEY = booleanPreferencesKey("logged_in")

    override suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
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
        }
    }

    fun isLoggedInFlow(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[LOGGED_IN_KEY] ?: false
        }
    }

    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
