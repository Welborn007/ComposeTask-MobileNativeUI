package com.macdevelopers.composetaskapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.composetaskapp.domain.usecase.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedInUseCase
) : ViewModel() {

    fun checkLogin(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            delay(1500)
            onResult(isUserLoggedIn())
        }
    }
}
