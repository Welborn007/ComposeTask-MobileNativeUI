package com.macdevelopers.composetaskapp.ui.screens.vendorProfile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.shared.domain.usecase.GetMyVendorUseCase
import kotlinx.coroutines.launch

class VendorProfileViewModel(
    private val getMyVendorUseCase: GetMyVendorUseCase
) : ViewModel() {

    private val _state = mutableStateOf(VendorProfileUiState())
    val state: State<VendorProfileUiState> = _state

    init {
        getMyVendor()
    }

    fun getMyVendor() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getMyVendorUseCase()
                .onSuccess { vendors ->
                    _state.value = _state.value.copy(
                        vendor = vendors.firstOrNull(),
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
}
