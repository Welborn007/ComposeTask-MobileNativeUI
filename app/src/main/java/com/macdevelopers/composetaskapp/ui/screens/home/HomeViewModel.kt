package com.macdevelopers.composetaskapp.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.shared.data.local.SharedAuthPreferences
import com.macdevelopers.shared.domain.repository.VendorRepository
import com.macdevelopers.shared.util.NetworkObserver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val authPreferences: SharedAuthPreferences,
    private val vendorRepository: VendorRepository,
    networkObserver: NetworkObserver
) : ViewModel() {

    private val _state = mutableStateOf(HomeUiState())
    val state: State<HomeUiState> = _state

    val isConnected: StateFlow<Boolean> = networkObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    init {
        getVendors()
    }

    fun getVendors() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            vendorRepository.getVendors()
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
            authPreferences.clearToken()
            authPreferences.clearAll()
        }
    }
}
