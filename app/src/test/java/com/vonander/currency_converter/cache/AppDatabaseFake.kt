package com.vonander.currency_converter.cache

import com.vonander.currency_converter.cache.model.ListResponseEntity
import com.vonander.currency_converter.cache.model.LiveResponseEntity

class AppDatabaseFake {

    var liveResponseEntity = LiveResponseEntity(
        id = 1,
        success = "false",
        source = "source",
        quotes = "quotes",
        error = "error"
    )

    var listResponseEntity = ListResponseEntity(
        id = 1,
        success = "false",
        currencies = "currencies",
        error = "error"
    )
}