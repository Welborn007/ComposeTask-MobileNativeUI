package com.macdevelopers.composetaskapp.data.remote.dto

data class VendorDto(
    val id: Int,
    val businessName: String,
    val description: String?,
    val category: String?,
    val location: String?,
    val gstNumber: String?,
    val verified: Boolean,
    val ownerEmail: String?,
    val createdAt: String?
)
