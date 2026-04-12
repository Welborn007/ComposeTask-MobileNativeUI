package com.macdevelopers.composetaskapp.data.local.preferences


import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.macdevelopers.composetaskapp.data.local.authDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TOKEN_KEY = stringPreferencesKey("auth_token")
    private val LOGGED_IN_KEY = booleanPreferencesKey("logged_in")

    suspend fun saveToken(token: String) {
        context.authDataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return context.authDataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    suspend fun clearToken() {
        context.authDataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
        }
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.authDataStore.edit { prefs ->
            prefs[LOGGED_IN_KEY] = loggedIn
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return context.authDataStore.data.map { prefs ->
            prefs[LOGGED_IN_KEY] ?: false
        }
    }

    suspend fun clearAll() {
        context.authDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
