package com.macdevelopers.composetaskapp.data.repository

import com.macdevelopers.composetaskapp.data.remote.VendorApi
import com.macdevelopers.composetaskapp.data.remote.dto.VendorDto
import com.macdevelopers.composetaskapp.domain.repository.VendorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VendorRepositoryImpl @Inject constructor(
    private val vendorApi: VendorApi
) : VendorRepository {

    override suspend fun getVendors(): Result<List<VendorDto>> = withContext(Dispatchers.IO) {
        try {
            val response = vendorApi.getVendors()
            if (response.success && response.data != null) {
                Result.success(response.data.content)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
