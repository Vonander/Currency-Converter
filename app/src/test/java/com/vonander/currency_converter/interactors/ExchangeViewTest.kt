package com.vonander.currency_converter.interactors

import com.google.gson.GsonBuilder
import com.vonander.currency_converter.cache.*
import com.vonander.currency_converter.cache.util.ListResponseEntityMapper
import com.vonander.currency_converter.cache.util.LiveResponseEntityMapper
import com.vonander.currency_converter.network.data.MockWebServerResponses.currencyConversionResponses
import com.vonander.currency_converter.network.data.MockWebServerResponses.liveRatesRespondses
import com.vonander.currency_converter.network.data.MockWebServerResponses.supportedCurrenciesResponses
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.network.util.ConvertResponseDtoMapper
import com.vonander.currency_converter.network.util.ListResponseDtoMapper
import com.vonander.currency_converter.network.util.LiveResponseDtoMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import kotlin.math.absoluteValue

class ExchangeViewTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    private lateinit var getSupportedCurrencies: GetSupportedCurrencies
    private lateinit var searchLiveRates: SearchLiveRates
    private lateinit var getCurrencyConversion: GetCurrencyConversion
    private lateinit var currencyLayerService: CurrencyLayerService

    private val exchangeListResponseDtoMapper = ListResponseDtoMapper()
    private val exchangeLiveResponseDtoMapper = LiveResponseDtoMapper()
    private val exchangeConvertResponseDtoMapper = ConvertResponseDtoMapper()
    private val appDatabaseFake = AppDatabaseFake()
    private val liveResponseEntityMapper = LiveResponseEntityMapper()
    private val liveResponseDao = LiveResponseDaoFake(appDatabaseFake)
    private val listResponseEntityMapper = ListResponseEntityMapper()
    private val listResponseDao = ListResponseDaoFake(appDatabaseFake)

    private val mockAccessKey = "1234"
    private val mockSource = "USD"

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        baseUrl = mockWebServer.url("/")
        currencyLayerService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(CurrencyLayerService::class.java)

        getSupportedCurrencies = GetSupportedCurrencies(
            service = currencyLayerService,
            listDtoMapper = exchangeListResponseDtoMapper,
            entityMapper = listResponseEntityMapper,
            listResponseDao = listResponseDao,
            accessKey = mockAccessKey
        )

        searchLiveRates = SearchLiveRates(
            liveResponseDao = liveResponseDao,
            liveDtoMapper = exchangeLiveResponseDtoMapper,
            service = currencyLayerService,
            entityMapper = liveResponseEntityMapper,
            accessKey = mockAccessKey
        )

        getCurrencyConversion = GetCurrencyConversion(
            service = currencyLayerService,
            dtoMapper = exchangeConvertResponseDtoMapper,
            accessKey = mockAccessKey
        )
    }

    @Test
    fun getSupportedCurrencies(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(supportedCurrenciesResponses)
        )

        val defaultResponse = listResponseDao.getAllListResponses()

        assert(defaultResponse.success == "false")

        val flowItems = getSupportedCurrencies.execute().toList()

        assert(flowItems[0].loading)

        val supportedCurrencies = flowItems[1].data

        assert(supportedCurrencies?.success == true)

        assert(supportedCurrencies?.currencies?.isNotEmpty() == true)

        assert(!flowItems[1].loading)
    }

    @Test
    fun getSupportedCurrencies_emitHttpError(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val flowItems = getSupportedCurrencies.execute().toList()

        assert(flowItems[0].loading)

        val error = flowItems[1].error

        assert(error != null)

        assert(!flowItems[1].loading)
    }

    @Test
    fun searchLiveRates(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(liveRatesRespondses)
        )

        val defaultResponse = liveResponseDao.getAllLiveResponses()

        assert(defaultResponse.success == "false")

        val flowItems = searchLiveRates.execute(
            source = mockSource
        ).toList()

        assert(flowItems[0].loading)

        val liveRates = flowItems[1].data

        assert(liveRates?.success == true)

        assert(liveRates?.quotes?.isNotEmpty() == true)

        assert(!flowItems[1].loading)
    }

    @Test
    fun getCurrencyConversion(): Unit = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(currencyConversionResponses)
        )

        val flowItems = getCurrencyConversion.execute(
            from = "",
            to = "",
            amount = 1.0
        ).toList()

        assert(flowItems[0].loading)

        val conversionResponse = flowItems[1].data

        assert(conversionResponse?.success == true)

        assert(conversionResponse?.result?.absoluteValue is Double)

        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}