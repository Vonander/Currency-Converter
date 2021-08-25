package com.vonander.currency_converter.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vonander.currency_converter.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mainDataStore")

@Singleton
class TimeElapsedDatastore (
    private val app: BaseApplication
) {

    fun saveCurrentTimeToDataStore(key: Preferences.Key<String>) {
        val dateNow = Calendar.getInstance().time
        val dateNowLong = dateNow.time
        val dateNowString = dateNowLong.toString()

        CoroutineScope(Dispatchers.Default ).launch {
            app.dataStore.edit {
                it[key] = dateNowString
            }
        }
    }

    fun getLatestStoredTimestamp(
        key: Preferences.Key<String>,
        completion: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.Default ).launch {
            val valueFlow: Flow<String> = app.dataStore.data.map {
                it[key]?: ""
            }
            completion(valueFlow.first())
        }
    }

    fun hasTimeElapsed(
        key: Preferences.Key<String>,
        minutes: Int,
        timeElapsed: (Boolean) -> Unit) {

        val dateNow = Calendar.getInstance().time
        val dateNowLong = dateNow.time

        getLatestStoredTimestamp(
            key = key
        ) {

            if (it.isEmpty()) {
                saveCurrentTimeToDataStore(
                    key = key
                )

                timeElapsed(true)

            } else {

                val differenceInMillis = dateNowLong - it.toLong()
                val differenceInSec = differenceInMillis / 1000
                val differenceInMin = differenceInSec / 60

                if (differenceInMin > minutes) {

                    saveCurrentTimeToDataStore(key)

                    timeElapsed(true)

                } else {

                    timeElapsed(false)
                }
            }
        }
    }
}