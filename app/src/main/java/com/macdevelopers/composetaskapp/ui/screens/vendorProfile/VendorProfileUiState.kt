package com.macdevelopers.composetaskapp.ui.screens.vendorProfile

import com.macdevelopers.shared.data.remote.dto.VendorDto

data class VendorProfileUiState(
    val vendor: VendorDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
