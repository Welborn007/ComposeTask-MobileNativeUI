package com.macdevelopers.shared

import com.macdevelopers.shared.di.initKoin
import com.macdevelopers.shared.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// This makes the shared repositories accessible to Swift
object IOSDIHelper : KoinComponent {
    fun init() = initKoin()
    
    fun getAuthRepository(): AuthRepository {
        val repository: AuthRepository by inject()
        return repository
    }
}
