package com.macdevelopers.shared.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.macdevelopers.shared.data.remote.dto.VendorDto

@Entity(tableName = "vendors")
data class VendorEntity(
    @PrimaryKey val id: String,
    val businessName: String,
    val description: String?,
    val category: String?,
    val location: String?,
    val gstNumber: String?,
    val verified: Boolean,
    val ownerEmail: String?,
    val createdAt: String?,
    val averageRating: Double?,
    val totalReviews: Int?
)

fun VendorEntity.toDto() = VendorDto(
    id = id,
    businessName = businessName,
    description = description,
    category = category,
    location = location,
    gstNumber = gstNumber,
    verified = verified,
    ownerEmail = ownerEmail,
    createdAt = createdAt,
    averageRating = averageRating,
    totalReviews = totalReviews
)

fun VendorDto.toEntity() = VendorEntity(
    id = id,
    businessName = businessName,
    description = description,
    category = category,
    location = location,
    gstNumber = gstNumber,
    verified = verified,
    ownerEmail = ownerEmail,
    createdAt = createdAt,
    averageRating = averageRating,
    totalReviews = totalReviews
)
