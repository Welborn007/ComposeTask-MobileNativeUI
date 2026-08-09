package com.macdevelopers.shared.domain.usecase

import com.macdevelopers.shared.data.remote.dto.VendorDto
import com.macdevelopers.shared.domain.repository.VendorRepository

class GetMyVendorUseCase(
    private val repository: VendorRepository
) {
    suspend operator fun invoke(): Result<List<VendorDto>> {
        return repository.getMyVendor()
    }
}
