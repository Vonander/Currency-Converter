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
            emit(DataState.error<ExchangeLiveResponse>(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getExchangeRateResponseFromNetwork(
        source: String
    ): ExchangeLiveResponse {

        //return returnFejkResponse()

/*        val test = service.live(
            access_key = accessKey,
            source = source
        )

        println("okej test: $test")*/

        return dtoMapper.mapToDomainModel(
            service.live(
                access_key = accessKey,
                source = source
            )
        )
    }

/*    private fun returnFejkResponse(): ExchangeRateResponse {
        val newHashMap : HashMap<String, Double> = HashMap()
        newHashMap["USDMXN"] = 19.90504
        newHashMap["USDPLN"] = 3.87835
        newHashMap["USDAUD"] = 1.364498
        newHashMap["USDUSD"] = 1.0
        newHashMap["USDCAD"] = 1.25577

        val fejkResponseDto = ExchangeRateResponseDto(
            success = true,
            source = "USD",
            quotes = newHashMap
        )

        return dtoMapper.mapToDomainModel(
            fejkResponseDto
        )
    }*/
}