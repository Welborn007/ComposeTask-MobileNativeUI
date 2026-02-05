package com.macdevelopers.composetaskapp.data.local.preferences


import android.content.Context
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
    private val prefs =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("logged_in", false)
    }

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

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
}
