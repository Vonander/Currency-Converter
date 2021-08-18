package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.ExchangeListResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ExchangeListResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSupportedCurrencies(
    private val service: CurrencyLayerService,
    private val dtoMapper: ExchangeListResponseDtoMapper,
    private val accessKey: String
) {

    fun execute(): Flow<DataState<ExchangeListResponse>> = flow {
        try {

            emit(DataState.loading())

            val response = getSupportedCurrenciewFromNetwork()

            emit(DataState.success(response))

        } catch (e: Exception) {
            emit(DataState.error<ExchangeListResponse>(e.message ?: "Unknown get supported currencies Error"))
        }
    }

    private suspend fun getSupportedCurrenciewFromNetwork(): ExchangeListResponse {
        return dtoMapper.mapToDomainModel(
            service.list(
                access_key = accessKey
            )
        )
    }
}