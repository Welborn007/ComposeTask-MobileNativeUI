package com.macdevelopers.composetaskapp.ui.screens.vendorProfile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macdevelopers.shared.domain.usecase.CreateVendorUseCase
import com.macdevelopers.shared.data.remote.dto.CreateVendorDto
import com.macdevelopers.shared.domain.usecase.GetMyVendorUseCase
import kotlinx.coroutines.launch

class VendorProfileViewModel(
    private val getMyVendorUseCase: GetMyVendorUseCase,
    private val createVendorUseCase: CreateVendorUseCase
) : ViewModel() {

    private val _state = mutableStateOf(VendorProfileUiState())
    val state: State<VendorProfileUiState> = _state

    init {
        getMyVendor()
    }

    // Form state for creating a vendor (can be used by UI to bind inputs)
    var businessName: String = ""
        private set
    var description: String? = null
        private set
    var category: String? = null
        private set
    var location: String? = null
        private set
    var gstNumber: String? = null
        private set

    fun updateBusinessName(value: String) { businessName = value }
    fun updateDescription(value: String?) { description = value }
    fun updateCategory(value: String?) { category = value }
    fun updateLocation(value: String?) { location = value }
    fun updateGstNumber(value: String?) { gstNumber = value }

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

    /**
     * Create vendor by accepting raw form inputs. ViewModel will build the CreateVendorDto
     * and call the use-case. This keeps DTO construction inside the ViewModel.
     */
    fun createVendor(
        businessName: String,
        description: String?,
        category: String?,
        location: String?,
        gstNumber: String?
    ) {
        val dto = CreateVendorDto(
            businessName = businessName,
            description = description,
            category = category,
            location = location,
            gstNumber = gstNumber
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            createVendorUseCase(dto)
                .onSuccess { vendor ->
                    _state.value = _state.value.copy(
                        vendor = vendor,
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
