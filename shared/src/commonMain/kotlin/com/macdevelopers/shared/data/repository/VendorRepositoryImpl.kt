package com.macdevelopers.shared.data.repository

import com.macdevelopers.shared.data.remote.dto.ApiResponseDto
import com.macdevelopers.shared.data.remote.dto.PaginatedResponseDto
import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.shared.domain.repository.VendorRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class VendorRepositoryImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "http://192.168.0.106:8080/api/"
) : VendorRepository {

    override suspend fun getVendors(): Result<List<VendorDto>> {
        return try {
            val response: ApiResponseDto<PaginatedResponseDto<VendorDto>> = 
                httpClient.get("${baseUrl}vendors").body()
            
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
