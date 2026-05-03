package com.macdevelopers.composetaskapp

import android.app.Application
import com.macdevelopers.composetaskapp.di.appModule
import com.macdevelopers.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}
