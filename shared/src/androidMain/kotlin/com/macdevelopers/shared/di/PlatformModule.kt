package com.macdevelopers.shared.di

import androidx.room.Room
import com.macdevelopers.shared.data.local.createDataStore
import com.macdevelopers.shared.data.local.db.AppDatabase
import com.macdevelopers.shared.util.NetworkObserver
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { createDataStore(androidContext()) }
    single { NetworkObserver(androidContext()) }
    single {
        val dbFile = androidContext().getDatabasePath("app_database.db")
        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = dbFile.absolutePath
        )
    }
}
