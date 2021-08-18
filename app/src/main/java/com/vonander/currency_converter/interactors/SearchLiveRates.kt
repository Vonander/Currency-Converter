package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.ExchangeLiveResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeLiveResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchLiveRates(
    private val service: CurrencyLayerService,
    private val dtoMapper: ExchangeLiveResponseDtoMapper,
    private val accessKey: String
) {
    fun execute(
        source: String
    ): Flow<DataState<ExchangeLiveResponse>> = flow {
        try {
            emit(DataState.loading())

            val response = getExchangeRateResponseFromNetwork(source)

            emit(DataState.success(response))

        } catch (e: Exception) {
            emit(DataState.error<ExchangeLiveResponse>(e.message ?: "Unknown search live rates Error"))
        }
    }

    private suspend fun getExchangeRateResponseFromNetwork(
        source: String
    ): ExchangeLiveResponse {

        return dtoMapper.mapToDomainModel(
            service.live(
                access_key = accessKey,
                source = source
            )
        )
    }
}