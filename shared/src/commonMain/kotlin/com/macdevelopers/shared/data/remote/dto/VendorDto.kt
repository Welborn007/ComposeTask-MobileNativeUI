package com.macdevelopers.shared.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VendorDto(
    val id: Int,
    val businessName: String,
    val description: String? = null,
    val category: String? = null,
    val location: String? = null,
    val gstNumber: String? = null,
    val verified: Boolean = false,
    val ownerEmail: String? = null,
    val createdAt: String? = null
)
