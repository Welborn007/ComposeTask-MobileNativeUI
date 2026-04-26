package com.macdevelopers.shared.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect fun createDataStore(context: Any? = null): DataStore<Preferences>

internal const val AUTH_DATASTORE_FILENAME = "auth.preferences_pb"
