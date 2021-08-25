package com.vonander.currency_converter.interactors

import com.vonander.currency_converter.cache.LiveResponseDao
import com.vonander.currency_converter.cache.model.LiveResponseEntity
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
    ): Flow<DataState<List<LiveResponse>>> = flow {
        try {
            emit(DataState.loading())

            insertToCache(source = source)

            val allLiveResponses = liveResponseDao.getAllLiveResponses()

            val liveResponsesFromCache = entityMapper.fromEntityList(allLiveResponses)

            emit(DataState.success(liveResponsesFromCache))

        } catch (e: Exception) {
            val errorMessage = handleErrorMessage(liveResponseDao.getAllLiveResponses()[0].error)
            liveResponseDao.deleteAllLiveResponses()

            emit(DataState.error<List<LiveResponse>>(message = errorMessage))
        }
    }

    private suspend fun insertToCache(source: String) {
        val response = getExchangeRateResponseFromNetwork(source)
        liveResponseDao.insertLiveResponse(entityMapper.mapFromDomainModel(response))
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

    private fun getLastIndex(list: List<LiveResponseEntity>): Int {
        return if (list.isEmpty()) {
            0
        } else {
            list.lastIndex
        }
    }

    private fun handleErrorMessage(error: String?): String {
        var errorMessage = "Unknown search live rates Error"

        error?.let {

            if (error.take(8) == "code=105") {
                errorMessage = "Your current Subscription Plan does not support Source Currency Switching."
            }
        }

        return errorMessage
    }

    suspend fun checkIfLiveResponseCacheListIsEmptyOrUnsuccessful(): Boolean {
        return liveResponseDao.getAllLiveResponses().isEmpty() ||
                liveResponseDao.getAllLiveResponses()[0].success == "false"
    }

    suspend fun getLiveResponseFromCache(completion: (LiveResponse) -> Unit ) {
        val cacheLiveResult = liveResponseDao.getAllLiveResponses()

        val liveResponsesFromCache = entityMapper.mapToDomainModel(cacheLiveResult[getLastIndex(cacheLiveResult)])

        completion(liveResponsesFromCache)
    }
}