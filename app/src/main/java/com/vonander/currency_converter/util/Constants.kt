package com.vonander.currency_converter.util

import androidx.datastore.preferences.core.stringPreferencesKey

const val TAG = "CurrencyConverter"

const val DATABASE_NAME_LIST= "list_response_db"
const val DATABASE_NAME_LIVE = "live_response_db"

val LIST_KEY = stringPreferencesKey("list_key")
val LIVE_KEY = stringPreferencesKey("live_key")

const val VERSION = "free"
//const val VERSION = "paid"