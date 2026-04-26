package com.macdevelopers.composetaskapp.data.remote

import com.macdevelopers.composetaskapp.data.remote.dto.ApiResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.PaginatedResponseDto
import com.macdevelopers.composetaskapp.data.remote.dto.VendorDto
import retrofit2.http.GET

interface VendorApi {

    @GET("vendors")
    suspend fun getVendors(): ApiResponseDto<PaginatedResponseDto<VendorDto>>
}
