package com.macdevelopers.shared.data.repository

import com.macdevelopers.shared.data.local.dao.VendorDao
import com.macdevelopers.shared.data.local.entity.toDto
import com.macdevelopers.shared.data.local.entity.toEntity
import com.macdevelopers.shared.data.remote.ApiService
import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.shared.domain.repository.VendorRepository

class VendorRepositoryImpl(
    private val apiService: ApiService,
    private val vendorDao: VendorDao,
    private val authPreferencesProvider: () -> AuthPreferencesBridge,
) : VendorRepository {

    override suspend fun getVendors(): Result<List<VendorDto>> {
        return try {
            val response = apiService.getVendors()

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

    override suspend fun getMyVendor(): Result<List<VendorDto>> {
        return try {
            val response = apiService.getMyVendor()

            if (response.success && response.data != null) {
                val vendors = response.data.content
                vendorDao.insertVendors(vendors.map { it.toEntity() })
                Result.success(vendors)
            } else {
                // API returned failure - try to return cached vendor for logged in user
                val user = try { authPreferencesProvider().getUserData() } catch (_: Exception) { null }
                val cached = user?.email?.let { vendorDao.getVendorByOwnerEmail(it) }
                if (cached != null) {
                    Result.success(listOf(cached.toDto()))
                } else {
                    Result.failure(Exception(response.message))
                }
            }
        } catch (e: Exception) {
            // On network or unexpected errors, fallback to cached vendor for logged in user
            try {
                val user = authPreferencesProvider().getUserData()
                val cached = user?.email?.let { vendorDao.getVendorByOwnerEmail(it) }
                if (cached != null) {
                    return Result.success(listOf(cached.toDto()))
                }
            } catch (_: Exception) {
                // ignore and return original exception below
            }
            Result.failure(e)
        }
    }
}
