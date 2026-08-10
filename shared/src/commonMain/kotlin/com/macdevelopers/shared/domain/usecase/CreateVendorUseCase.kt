package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.data.remote.dto.CreateVendorDto
import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.shared.domain.repository.VendorRepository

class CreateVendorUseCase(
    private val repository: VendorRepository
) {
    suspend operator fun invoke(createVendorDto: CreateVendorDto): Result<VendorDto> {
        return repository.createVendor(createVendorDto)
    }
}
