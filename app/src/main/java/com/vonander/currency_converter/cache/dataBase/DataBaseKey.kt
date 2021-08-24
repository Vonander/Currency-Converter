package com.vonander.currency_converter.cache.dataBase

sealed class DataBaseKey(
    val name: String
) {
    object List: DataBaseKey("list_key")
    object Live: DataBaseKey("live_key")
}
