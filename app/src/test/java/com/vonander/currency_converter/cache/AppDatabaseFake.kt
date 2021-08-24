package com.vonander.currency_converter.cache

import com.vonander.currency_converter.cache.model.ListResponseEntity
import com.vonander.currency_converter.cache.model.LiveResponseEntity

class AppDatabaseFake {

    var listResponseEntity = ListResponseEntity(
        id = 1,
        success = "false",
        currencies = "currencies",
        error = "error"
    )

    val liveResponseEntityList = mutableListOf<LiveResponseEntity>()
}