package com.vonander.currency_converter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.currency_converter.domain.model.ExchangeConvertResponse
import com.vonander.currency_converter.domain.model.ExchangeListResponse
import com.vonander.currency_converter.domain.model.ExchangeLiveResponse
import com.vonander.currency_converter.interactors.GetCurrencyConversion
import com.vonander.currency_converter.interactors.GetSupportedCurrencies
import com.vonander.currency_converter.interactors.SearchLiveRates
import com.vonander.currency_converter.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val getSupportedCurrencies: GetSupportedCurrencies,
    private val searchLiveRates: SearchLiveRates,
    private val getCurrencyConversion: GetCurrencyConversion
) : ViewModel() {

    val loading = mutableStateOf(false)
    val exchangeFromCurrency = mutableStateOf("")
    val exchangeToCurrency = mutableStateOf("")
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
        getSupportedCurrencies()
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
            }

        }.launchIn(viewModelScope)
    }

    private fun getCurrencyConversion() {
        val amount: Double

        if (exchangeFromCurrency.value.isBlank() || exchangeToCurrency.value.isBlank()) {
            setErrorMessage(error = "Currencies are not set")

            return
        }

        try {
            amount = searchBarQueryText.value.toDouble()

            getCurrencyConversion.execute(
                from = exchangeFromCurrency.value,
                to = exchangeToCurrency.value,
                amount = amount
            ).onEach { dataState ->

                loading.value = dataState.loading

                dataState.data?.let { response ->
                    handleConversionResponse(response)
                }

                dataState.error?.let { error ->
                    setErrorMessage(error = "getCurrencyConversion error: $error")
                }

            }.launchIn(viewModelScope)

        } catch (e: Exception) {
            setErrorMessage(
                error = "getCurrencyConversion error Exception: $e ${searchBarQueryText.value} is not supported"
            )
        }
    }

    private fun appendSupportedCurrencies(response: ExchangeListResponse) {
        response.currencies.let {
            supportedCurrenciesMap.value = response.currencies!!
        }

        response.currencies.let {
            val newEntries = mutableListOf<String>()

            it?.entries?.forEach {
                newEntries.add(it.key)
            }

            newEntries.sort()

            supportedCurrenciesList.value = newEntries
        }
    }

    private fun appendRatesToList(response: ExchangeLiveResponse) {
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

    private fun handleConversionResponse(response: ExchangeConvertResponse) {
        if (!response.success) {
            Log.e(TAG, (response.error?.get("info")
                ?: "Unknown Api error").toString() + "Calculating with stashed values")

            var rate = ""

            ratesList.value.onEach { hashMap ->

                val currencyString: String = hashMap.keys.toString()
                val rateString: String = hashMap.values.toString()

                val currencyStringShort = currencyString.drop(4).dropLast(1)
                val rateStringEdited = rateString.drop(1).dropLast(1)

                if (currencyStringShort == exchangeToCurrency.value) {
                    rate = rateStringEdited
                }
            }

            val amount: Double = searchBarQueryText.value.toDouble()
            val rateDouble: Double = rate.toDouble()
            val currencyConverted = amount * rateDouble

            searchBarResultText.value = "= $currencyConverted"

            return
        }

        searchBarResultText.value = "= ${response.result}"
    }

    fun updateCurrencyLazyColumnList(index: Int) {
        dropDownMenu1SelectedIndex.value = index
        val currency = supportedCurrenciesList.value[index]

        source.value = currency
        exchangeFromCurrency.value = currency

        newRatesSearch()
    }

    fun updateDropdownMenu2Label(index: Int) {
        dropDownMenu2SelectedIndex.value = index
        val currency = supportedCurrenciesList.value[index]

        exchangeToCurrency.value = currency
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

    private fun defaultToUSD() {
        exchangeFromCurrency.value = "USD"
        supportedCurrenciesList.value.forEachIndexed { index, currency ->
            if (currency == "USD") {
                dropDownMenu1SelectedIndex.value = index
            }
        }

        source.value = "USD"
        newRatesSearch()
    }
}
