package com.macdevelopers.composetaskapp.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.shared.data.local.SharedAuthPreferences
import com.macdevelopers.shared.domain.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authPreferences: SharedAuthPreferences,
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomeUiState())
    val state: State<HomeUiState> = _state

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
