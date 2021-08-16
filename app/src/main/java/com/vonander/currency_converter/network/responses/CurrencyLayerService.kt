package com.vonander.currency_converter.network.responses

import com.vonander.currency_converter.domain.model.ExchangeRateResonse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerService {
    @GET("live")
    suspend fun search(
        @Query("access_key") access_key: String,
        @Query("currencies") currencies: String
    ): ExchangeRateResonse
}