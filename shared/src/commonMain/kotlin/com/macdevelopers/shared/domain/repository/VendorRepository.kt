package com.macdevelopers.shared.domain.repository

import com.macdevelopers.shared.data.remote.dto.CreateVendorDto
import com.macdevelopers.shared.data.remote.dto.VendorDto

interface VendorRepository {
    suspend fun getVendors(): Result<List<VendorDto>>
    suspend fun getMyVendor(): Result<List<VendorDto>>
    suspend fun createVendor(createVendorDto: CreateVendorDto): Result<VendorDto>
}
