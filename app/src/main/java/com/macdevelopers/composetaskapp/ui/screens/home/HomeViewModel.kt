package com.macdevelopers.composetaskapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.composetaskapp.data.local.preferences.AuthPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authPreferences: AuthPreferences
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authPreferences.clearToken()
            authPreferences.clearAll()
        }
    }
}

