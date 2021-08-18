package com.vonander.currency_converter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val snackbarMessage = mutableStateOf("")
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
                Log.e(TAG,"onTriggerEvent: $event Exception: $e, ${e.cause}")
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
                errorMessage.value = error
                println("okej newCurrenciesSearch -> error: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun newRatesSearch() {
        searchLiveRates.execute(
            source = source.value
        ).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { response ->
                appendratesToList(response)
            }

            dataState.error?.let { error ->
                errorMessage.value = error
                println("okej newRatesSearch -> error: $error")
            }

        }.launchIn(viewModelScope)
    }

    private fun getCurrencyConversion() {
        val amount: Double

        if (exchangeFromCurrency.value.isBlank() || exchangeToCurrency.value.isBlank()) {
            errorMessage.value = "Currencies are not set"

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
                    searchBarResultText.value = "= ${response.result}"
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getCurrencyConversion error: $error")
                    errorMessage.value = error
                }

            }.launchIn(viewModelScope)

        } catch (e: Exception) {
            errorMessage.value = "${searchBarQueryText.value} is not supported"
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

    private fun appendratesToList(response: ExchangeLiveResponse) {
        val newList = mutableListOf<HashMap<String, Double>>()

        response.quotes?.forEach {
            val newEntry = HashMap<String, Double>()
            newEntry[it.key] = it.value
            newList.add(newEntry)
        }

        ratesList.value = newList
    }

    fun updateCurrencyLazyColumnList(index: Int) {
        dropDownMenu1SelectedIndex.value = index
        val currency = supportedCurrenciesList.value[index]

        source.value = currency
        exchangeFromCurrency.value = currency

        newRatesSearch()
    }

    fun updateMenu2Label(index: Int) {
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
}
