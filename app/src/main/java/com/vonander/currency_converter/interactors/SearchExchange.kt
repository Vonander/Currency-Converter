package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.ExchangeResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchExchange(
    private val service: CurrencyLayerService,
    private val dtoMapper: ExchangeResponseDtoMapper,
    private val accessKey: String
) {

    fun execute(
        currencies: String
    ): Flow<DataState<ExchangeResponse>> = flow {

        try {

            emit(DataState.loading())


            val response = getExchangeRateResponseFromNetwork(currencies)


            emit(DataState.success(response))

        } catch (e: Exception) {
            emit(DataState.error<ExchangeResponse>(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getExchangeRateResponseFromNetwork(
        currencies: String
    ): ExchangeResponse {
        return dtoMapper.mapToDomainModel(
            service.search(
                access_key = accessKey,
                currencies = currencies
            )
        )
    }
}