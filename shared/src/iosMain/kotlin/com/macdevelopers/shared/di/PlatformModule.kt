package com.macdevelopers.shared.di

import androidx.room.Room
import com.macdevelopers.shared.data.local.createDataStore
import com.macdevelopers.shared.data.local.db.AppDatabase
import com.macdevelopers.shared.util.NetworkObserver
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun platformModule() = module {
    single<HttpClientEngine> { Darwin.create() }
    single { createDataStore() }
    single { NetworkObserver() }
    single {
        val dbFilePath = documentDirectory() + "/app_database.db"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        )
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}
