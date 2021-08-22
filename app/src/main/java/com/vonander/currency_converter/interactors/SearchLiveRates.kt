package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.cache.LiveResponseDao
import com.vonander.currency_converter.cache.util.LiveResponseEntityMapper
import com.vonander.currency_converter.domain.DataState
import com.vonander.currency_converter.domain.model.LiveResponse
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.LiveResponseDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchLiveRates(
    private val service: CurrencyLayerService,
    private val entityMapper: LiveResponseEntityMapper,
    private val liveDtoMapper: LiveResponseDtoMapper,
    private val liveResponseDao: LiveResponseDao,
    private val accessKey: String
) {
    fun execute(
        source: String
    ): Flow<DataState<LiveResponse>> = flow {
        try {
            emit(DataState.loading())

            val response = getExchangeRateResponseFromNetwork(source)

            liveResponseDao.insertLiveResponse(entityMapper.mapFromDomainModel(response))

            val cacheLiveResult = liveResponseDao.getAllLiveResponses()

            val liveResponsesFromCache = entityMapper.mapToDomainModel(cacheLiveResult)

            emit(DataState.success(liveResponsesFromCache))

        } catch (e: Exception) {
            emit(DataState.error<LiveResponse>(e.message ?: "Unknown search live rates Error"))
        }
    }

    private suspend fun getExchangeRateResponseFromNetwork(
        source: String
    ): LiveResponse {

        return liveDtoMapper.mapToDomainModel(
            service.live(
                access_key = accessKey,
                source = source
            )
        )
    }
}