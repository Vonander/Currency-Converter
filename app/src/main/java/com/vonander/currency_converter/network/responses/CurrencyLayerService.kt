package com.vonander.currency_converter.network.responses

import com.vonander.currency_converter.network.model.ExchangeConvertResponseDto
import com.vonander.currency_converter.network.model.ExchangeListResponseDto
import com.vonander.currency_converter.network.model.ExchangeLiveResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyLayerService {
    @GET("live")
    suspend fun live(
        @Query("access_key") access_key: String,
        @Query("source") source: String
    ): ExchangeLiveResponseDto

    @GET("list")
    suspend fun list(
        @Query("access_key") access_key: String,
    ): ExchangeListResponseDto

    @GET("convert")
    suspend fun convert(
        @Query("access_key") access_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
    ): ExchangeConvertResponseDto
}