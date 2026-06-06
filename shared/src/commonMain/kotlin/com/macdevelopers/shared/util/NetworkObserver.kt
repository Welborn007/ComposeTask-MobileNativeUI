package com.macdevelopers.shared.util

import kotlinx.coroutines.flow.Flow

expect class NetworkObserver {
    val isConnected: Flow<Boolean>
}
