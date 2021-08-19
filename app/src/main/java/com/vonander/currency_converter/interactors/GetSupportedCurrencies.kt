package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.ListResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ListResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSupportedCurrencies(
    private val service: CurrencyLayerService,
    private val dtoMapper: ListResponseDtoMapper,
    private val accessKey: String
) {

    fun execute(): Flow<DataState<ListResponse>> = flow {
        try {

            emit(DataState.loading())

            val response = getSupportedCurrenciewFromNetwork()

            emit(DataState.success(response))

        } catch (e: Exception) {
            emit(DataState.error<ListResponse>(e.message ?: "Unknown get supported currencies Error"))
        }
    }

    private suspend fun getSupportedCurrenciewFromNetwork(): ListResponse {
        return dtoMapper.mapToDomainModel(
            service.list(
                access_key = accessKey
            )
        )
    }
}