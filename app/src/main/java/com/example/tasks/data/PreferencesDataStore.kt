package com.example.tasks.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "settings"

private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class PreferencesDataStore private constructor(private val context: Context) {

    companion object {
        private val KEY_SORT = stringPreferencesKey("sort_type")
        private val KEY_DATE = longPreferencesKey("last_date")

        @Volatile private var INSTANCE: PreferencesDataStore? = null

        fun getInstance(context: Context): PreferencesDataStore =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferencesDataStore(context).also { INSTANCE = it }
            }
    }

    val sortTypeFlow: Flow<SortType> =
        context.dataStore.data.map { prefs ->
            SortType.valueOf(
                prefs[KEY_SORT] ?: SortType.DATE_ASC.name
            )
        }

    val lastDateFlow: Flow<Long> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_DATE] ?: System.currentTimeMillis()
        }

    suspend fun saveSortType(value: SortType) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SORT] = value.name
        }
    }

    suspend fun saveLastDate(value: Long) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DATE] = value
        }
    }
}
