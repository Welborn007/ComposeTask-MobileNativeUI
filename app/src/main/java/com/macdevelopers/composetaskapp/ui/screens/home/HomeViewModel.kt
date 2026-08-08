package com.macdevelopers.composetaskapp.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.shared.domain.usecase.GetVendorsUseCase
import com.macdevelopers.shared.domain.usecase.LogoutUseCase
import com.macdevelopers.shared.domain.usecase.UserDataUseCase
import com.macdevelopers.shared.util.NetworkObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getVendorsUseCase: GetVendorsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val userDataUseCase: UserDataUseCase,
    networkObserver: NetworkObserver
) : ViewModel() {

    private val _state = mutableStateOf(HomeUiState())
    val state: State<HomeUiState> = _state

    val isConnected: StateFlow<Boolean> = networkObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        getVendors()
        getAuthDetails()
    }

    fun getAuthDetails(){
        viewModelScope.launch {
            try {
                val userData = userDataUseCase()
                _state.value = _state.value.copy(
                    userName = userData.name,
                    userEmail = userData.email,
                    userRole = userData.role
                )
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }

    fun getVendors() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getVendorsUseCase()
                .onSuccess { vendors ->
                    _state.value = _state.value.copy(
                        vendors = vendors,
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "An unknown error occurred"
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}
