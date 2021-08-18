package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.ExchangeConvertResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeConvertResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrencyConversion(
    private val service: CurrencyLayerService,
    private val dtoMapper: ExchangeConvertResponseDtoMapper,
    private val accessKey: String
) {

    fun execute(
        from: String,
        to: String,
        amount: Double
    ): Flow<DataState<ExchangeConvertResponse>> = flow {
        try {

            emit(DataState.loading())

            val response = GetCurrencyConversionFromNetwork(from, to, amount)

            emit(DataState.success(response))

        } catch (e: Exception) {
            emit(DataState.error<ExchangeConvertResponse>(e.message ?: "Unknown currency convert error"))
        }
    }

    private suspend fun GetCurrencyConversionFromNetwork(
        from: String,
        to: String,
        amount: Double
    ): ExchangeConvertResponse {
        return dtoMapper.mapToDomainModel(
            service.convert(
                access_key = accessKey,
                from = from,
                to = to,
                amount = amount
            )
        )
    }
}