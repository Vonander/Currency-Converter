package com.vonander.currency_converter.util

import androidx.datastore.preferences.core.stringPreferencesKey

val TAG = "CurrencyConverter"

val DATABASE_NAME_LIST= "list_response_db"
val DATABASE_NAME_LIVE = "live_response_db"

val LIST_KEY = stringPreferencesKey("list_key")
val LIVE_KEY = stringPreferencesKey("live_key")