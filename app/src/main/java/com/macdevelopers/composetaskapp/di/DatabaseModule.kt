package com.macdevelopers.composetaskapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.macdevelopers.shared.data.local.SharedAuthPreferences
import com.macdevelopers.shared.data.local.createDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return createDataStore(context)
    }

    @Provides
    @Singleton
    fun provideSharedAuthPreferences(dataStore: DataStore<Preferences>): SharedAuthPreferences {
        return SharedAuthPreferences(dataStore)
    }
}