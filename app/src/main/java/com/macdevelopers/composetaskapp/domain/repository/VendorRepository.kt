package com.macdevelopers.composetaskapp.domain.repository

import com.macdevelopers.composetaskapp.data.remote.dto.VendorDto

interface VendorRepository {
    suspend fun getVendors(): Result<List<VendorDto>>
}
