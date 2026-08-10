package com.macdevelopers.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateVendorDto(
    val businessName: String,
    val description: String? = null,
    val category: String? = null,
    val location: String? = null,
    val gstNumber: String? = null
)
