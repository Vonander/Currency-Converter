package com.vonander.currency_converter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.currency_converter.BaseApplication
import com.vonander.currency_converter.datastore.TimeElapsedDatastore
import com.vonander.currency_converter.domain.model.ConvertResponse
import com.vonander.currency_converter.domain.model.ListResponse
import com.vonander.currency_converter.domain.model.LiveResponse
import com.vonander.currency_converter.interactors.GetCurrencyConversion
import com.vonander.currency_converter.interactors.GetSupportedCurrencies
import com.vonander.currency_converter.interactors.SearchLiveRates
import com.vonander.currency_converter.util.LIST_KEY
import com.vonander.currency_converter.util.LIVE_KEY
import com.vonander.currency_converter.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    app: BaseApplication,
    private val getSupportedCurrencies: GetSupportedCurrencies,
    private val searchLiveRates: SearchLiveRates,
    private val getCurrencyConversion: GetCurrencyConversion
) : ViewModel() {

    private val loading = mutableStateOf(false)
    private val datastore = TimeElapsedDatastore(app)
    val exchangeFromCurrencyLabel = mutableStateOf("")
    val exchangeToCurrencyLabel = mutableStateOf("")
    val source = mutableStateOf("")
    val ratesList: MutableState<List<HashMap<String, Double>>> = mutableStateOf(listOf())
    val supportedCurrenciesMap = mutableStateOf(HashMap<String, String>())
    val supportedCurrenciesList: MutableState<List<String>> = mutableStateOf(listOf(""))
    val searchBarQueryText = mutableStateOf("1")
    val searchBarResultText = mutableStateOf("=")
    val errorMessage = mutableStateOf("")
    val dropDownMenu1Expanded = mutableStateOf(false)
    val dropDownMenu1SelectedIndex = mutableStateOf(0)
    val dropDownMenu2Expanded = mutableStateOf(false)
    val dropDownMenu2SelectedIndex = mutableStateOf(0)

    init {
        getSupportedCurrenciesFromCache()
    }

    fun onTriggerEvent(event: ExchangeUseCaseEvent) {
        viewModelScope.launch {
            try {

                when(event) {
                    is ExchangeUseCaseEvent.GetSupportedCurrenciesEvent -> {
                        getSupportedCurrencies()
                    }

                    is ExchangeUseCaseEvent.SearchRatesEvent -> {
                        newRatesSearch()
                    }

                    is ExchangeUseCaseEvent.GetCurrencyConversion -> {
                        getCurrencyConversion()
                    }
                }

            } catch (e: Exception) {
                setErrorMessage(error ="onTriggerEvent: $event Exception: $e, ${e.cause}" )
            }
        }
    }

    /**BandwidthLimitFeature*/
    private fun getSupportedCurrenciesFromCache() {
        datastore.hasTimeElapsed(
            key = LIST_KEY,
            minutes = 30
        ) { timeElapsed ->

            if (!timeElapsed) {
                viewModelScope.launch {
                    appendSupportedCurrencies(getSupportedCurrencies.getCachedCurrencies())
                }
            } else {

                getSupportedCurrencies()
            }
        }
    }

    private fun getSupportedCurrencies() {
        getSupportedCurrencies.execute().onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { response ->
                appendSupportedCurrencies(response = response)
            }

            dataState.error?.let { error ->
                setErrorMessage(error = "getSupportedCurrencies error Exception: $error")
            }

        }.launchIn(viewModelScope)
    }

    /**
     * BandwidthLimitFeature
     *
     * Only used for the default currency USD
     * */
    private fun tryToGetRatesFromCache() {
        if (!defaultCurrencyIsUSD()) {
            newRatesSearch()

            return
        }

        datastore.hasTimeElapsed(
            key = LIVE_KEY,
            minutes = 30
        ) { timeElapsed ->

            if (!timeElapsed) {

                viewModelScope.launch {

                    if (searchLiveRates.checkIfLiveResponseCacheListIsEmpty()) {
                        newRatesSearch()

                    } else {
                        searchLiveRates.getLiveResponseFromCache { liveResponse ->
                            appendRatesToList(listOf(liveResponse))
                        }
                    }
                }

            }else {

                /** over 30 min has passed, it's ok to use API*/
                newRatesSearch()
            }
        }
    }

    private fun newRatesSearch() {
        searchLiveRates.execute(
            source = source.value
        ).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { response ->
                appendRatesToList(response)
            }

            dataState.error?.let { error ->
                setErrorMessage(error = "newRatesSearch error Exception: $error")

                defaultToUSD()
            }

        }.launchIn(viewModelScope)
    }

    private fun getCurrencyConversion() {
        if (!passedCurrencyConversionErrorHandling()) {
            return
        }

        getCurrencyConversion.execute(
            from = exchangeFromCurrencyLabel.value,
            to = exchangeToCurrencyLabel.value,
            amount = searchBarQueryText.value.toDouble()
        ).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { response ->
                handleConversionResponse(response)
            }

            dataState.error?.let { error ->
                setErrorMessage(error = "getCurrencyConversion error: $error")
            }

        }.launchIn(viewModelScope)
    }

    private fun passedCurrencyConversionErrorHandling():Boolean {
        var passed = true

        if (exchangeFromCurrencyLabel.value.isBlank() || exchangeToCurrencyLabel.value.isBlank()) {
            setErrorMessage(error = "Currencies are not set")
            passed = false
        }

        try {
            searchBarQueryText.value.toDouble()

        } catch (e: Exception) {
            setErrorMessage(error = "getCurrencyConversion error Exception: $e ${searchBarQueryText.value} is not supported")
            passed = false
        }

        return passed
    }

    private fun appendSupportedCurrencies(response: ListResponse) {
        response.currencies.let {

            supportedCurrenciesMap.value = response.currencies!!

            val newEntries = mutableListOf<String>()

            it?.entries?.forEach {
                newEntries.add(it.key)
            }

            newEntries.sort()

            supportedCurrenciesList.value = newEntries
        }
    }

    private fun appendRatesToList(response: List<LiveResponse>) {
        val response = response[getLastIndex(response)]

        if (!response.success) {
            errorMessage.value = (response.error?.get("info") ?: "Unknown Api error").toString()
            defaultToUSD()
        }

        val newList = mutableListOf<HashMap<String, Double>>()

        response.quotes?.forEach {
            val newEntry = HashMap<String, Double>()
            newEntry[it.key] = it.value
            newList.add(newEntry)
        }

        ratesList.value = newList
    }

    private fun handleConversionResponse(response: ConvertResponse) {
        if (!passedConversionResponseErrorHandling(response)) {
            calculateConversionWithSavedValues()
            return
        }

        searchBarResultText.value = "= ${response.result}"
    }

    private fun passedConversionResponseErrorHandling(response: ConvertResponse): Boolean {
        var passed = true

        if (!response.success) {
            Log.e(TAG, (response.error?.get("info")
                ?: "Unknown Api error").toString() + "/nCalculating with stashed values")

            passed = false
        }

        return passed
    }

    private fun calculateConversionWithSavedValues() {

        println("okej calculateConversionWithSavedValues ratesList.value: ${ratesList.value}")

        var rate = ""

        ratesList.value.onEach { hashMap ->

            val currencyString: String = hashMap.keys.toString()
            val rateString: String = hashMap.values.toString()

            val currencyStringShort = currencyString.drop(4).dropLast(1)
            val rateStringEdited = rateString.drop(1).dropLast(1)

            if (currencyStringShort == exchangeToCurrencyLabel.value) {
                rate = rateStringEdited
            }
        }

        val amount: Double = searchBarQueryText.value.toDouble()
        val rateDouble: Double = rate.toDouble()
        val currencyConverted = amount * rateDouble


        println("okej searchBarResultText.text: ${searchBarResultText.value}")
        searchBarResultText.value = "= $currencyConverted"
    }

    fun updateCurrencyLazyColumnList(index: Int) {
        dropDownMenu1SelectedIndex.value = index
        val currency = supportedCurrenciesList.value[index]

        source.value = currency
        exchangeFromCurrencyLabel.value = currency

        tryToGetRatesFromCache()
    }

    fun updateDropdownMenu2Label(index: Int) {
        dropDownMenu2SelectedIndex.value = index
        val currency = supportedCurrenciesList.value[index]

        exchangeToCurrencyLabel.value = currency
    }

    fun onQueryChanged(query: String) {
        setQuery(query = query)
    }

    private fun setQuery(query: String) {
        this.searchBarQueryText.value = query
    }

    private fun setErrorMessage(error: String) {
        errorMessage.value = error
        Log.e(TAG, error)
    }

    private fun defaultCurrencyIsUSD(): Boolean {
        return source.value == "USD"
    }

    private fun getLastIndex(list: List<LiveResponse>): Int {
        return if (list.isEmpty()) {
            0
        } else {
            list.lastIndex
        }
    }

    private fun defaultToUSD() {
        exchangeFromCurrencyLabel.value = "USD"
        supportedCurrenciesList.value.forEachIndexed { index, currency ->
            if (currency == "USD") {
                dropDownMenu1SelectedIndex.value = index
            }
        }

        source.value = "USD"
    }
}
