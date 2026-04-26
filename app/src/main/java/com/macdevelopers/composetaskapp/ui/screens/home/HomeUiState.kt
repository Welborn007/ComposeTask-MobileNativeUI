package com.macdevelopers.composetaskapp.ui.screens.home

import com.macdevelopers.composetaskapp.data.remote.dto.VendorDto

data class HomeUiState(
    val vendors: List<VendorDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
