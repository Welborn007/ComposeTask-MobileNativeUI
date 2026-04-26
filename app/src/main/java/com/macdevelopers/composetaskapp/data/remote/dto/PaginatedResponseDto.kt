package com.macdevelopers.composetaskapp.data.remote.dto

data class PaginatedResponseDto<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
    val last: Boolean
)
