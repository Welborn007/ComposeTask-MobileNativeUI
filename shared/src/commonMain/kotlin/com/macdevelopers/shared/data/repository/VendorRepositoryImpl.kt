package com.macdevelopers.shared.data.repository

import com.macdevelopers.shared.data.local.dao.VendorDao
import com.macdevelopers.shared.data.local.entity.toDto
import com.macdevelopers.shared.data.local.entity.toEntity
import com.macdevelopers.shared.data.remote.dto.ApiResponseDto
import com.macdevelopers.shared.data.remote.dto.PaginatedResponseDto
import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.shared.domain.repository.VendorRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class VendorRepositoryImpl(
    private val httpClient: HttpClient,
    private val vendorDao: VendorDao,
    private val baseUrl: String,
) : VendorRepository {

    override suspend fun getVendors(): Result<List<VendorDto>> {
        return try {
            val response: ApiResponseDto<PaginatedResponseDto<VendorDto>> = 
                httpClient.get("${baseUrl}vendors").body()
            
            if (response.success && response.data != null) {
                val vendors = response.data.content
                // Save to local cache
                vendorDao.deleteAllVendors()
                vendorDao.insertVendors(vendors.map { it.toEntity() })
                Result.success(vendors)
            } else {
                // If API call fails but we have cached data, return cached data
                val cachedVendors = vendorDao.getAllVendors()
                if (cachedVendors.isNotEmpty()) {
                    Result.success(cachedVendors.map { it.toDto() })
                } else {
                    Result.failure(Exception(response.message))
                }
            }
        } catch (e: Exception) {
            // In case of network error, return cached data
            val cachedVendors = vendorDao.getAllVendors()
            if (cachedVendors.isNotEmpty()) {
                Result.success(cachedVendors.map { it.toDto() })
            } else {
                Result.failure(e)
            }
        }
    }
}
