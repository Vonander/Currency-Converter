package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.cache.ListResponseDao
import com.vonander.currency_converter.cache.util.ListResponseEntityMapper
import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.ListResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ListResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetSupportedCurrencies(
    private val service: CurrencyLayerService,
    private val entityMapper: ListResponseEntityMapper,
    private val listDtoMapper: ListResponseDtoMapper,
    private val listResponseDao: ListResponseDao,
    private val accessKey: String
) {

    fun execute(): Flow<DataState<ListResponse>> = flow {
        try {

            emit(DataState.loading())

            val response = getSupportedCurrenciesFromNetwork()

            listResponseDao.insertListResponse(entityMapper.mapFromDomainModel(response))

            val cacheLiveResult = listResponseDao.getAllListResponses()

            val listResponsesFromCache = entityMapper.mapToDomainModel(cacheLiveResult)

            emit(DataState.success(listResponsesFromCache))

        } catch (e: Exception) {
            emit(DataState.error<ListResponse>(e.message ?: "Unknown get supported currencies Error"))
        }
    }

    private suspend fun getSupportedCurrenciesFromNetwork(): ListResponse {
        return listDtoMapper.mapToDomainModel(
            service.list(
                access_key = accessKey
            )
        )
    }
}