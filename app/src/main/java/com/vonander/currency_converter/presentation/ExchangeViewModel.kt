package com.vonander.currency_converter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.currency_converter.domain.model.ExchangeListResponse
import com.vonander.currency_converter.domain.model.ExchangeLiveResponse
import com.vonander.currency_converter.interactors.GetSupportedCurrencies
import com.vonander.currency_converter.interactors.SearchLiveRates
import com.vonander.currency_converter.util.STATE_KEY_QUERY
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val loading = mutableStateOf(false)
    val exchangeFromLabel = mutableStateOf("USD")
    val exchangeToLabel = mutableStateOf("USD")
    val source = mutableStateOf("USD")
    val ratesList: MutableState<List<HashMap<String, Double>>> = mutableStateOf(listOf())
    val supportedCurrenciesList: MutableState<List<String>> = mutableStateOf(listOf("USD"))
    val searchBarQueryText = mutableStateOf("1")
    val searchBarResultText = mutableStateOf("1")
    val errorMessage = mutableStateOf("")
    val snackbarMessage = mutableStateOf("")
    val dropDownMenu1Expanded = mutableStateOf(false)
    val dropDownMenu1SelectedIndex = mutableStateOf(0)
    val dropDownMenu2Expanded = mutableStateOf(false)
    val dropDownMenu2SelectedIndex = mutableStateOf(0)

    init {
        newCurrenciesSearch()
        //newRatesSearch()
    }

    fun onTriggerEvent(event: ExchangeUseCaseEvent) {
        viewModelScope.launch {
            try {

                when(event) {
                    is ExchangeUseCaseEvent.GetSupportedCurrenciesEvent -> {
                        newCurrenciesSearch()
                    }

                    is ExchangeUseCaseEvent.SearchRatesEvent -> {
                        newRatesSearch()
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG,"onTriggerEvent: $event Exception: $e, ${e.cause}")
            }

        }
    }

    private fun newCurrenciesSearch() {
        getSupportedCurrencies.execute().onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { response ->
                appendSupportedCurrenciesToList(response = response)
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

    private fun appendSupportedCurrenciesToList(response: ExchangeListResponse) {
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
        println("okej ratesList: ${ratesList.value}")
    }

    fun onQueryChanged(query: String) {
        setQuery(query = query)
    }

    private fun setQuery(query: String) {
        this.searchBarQueryText.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}
