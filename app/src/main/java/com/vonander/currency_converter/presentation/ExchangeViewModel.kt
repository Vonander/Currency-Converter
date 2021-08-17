package com.vonander.currency_converter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.currency_converter.domain.model.ExchangeResponse
import com.vonander.currency_converter.interactors.SearchExchange
import com.vonander.currency_converter.util.STATE_KEY_QUERY
import com.vonander.currency_converter.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val searchExchange: SearchExchange,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val loading = mutableStateOf(false)
    val exchangeFromLabel = mutableStateOf("USD")
    val exchangeToLabel = mutableStateOf("XXX")
    val currencies = mutableStateOf("USD,AUD,CAD,PLN,MXN")
    var quotesList: MutableState<List<HashMap<String, Double>>> = mutableStateOf(listOf())
    val searchBarQueryText = mutableStateOf("1")
    val searchBarResultText = mutableStateOf("19.90504")
    val errorMessage = mutableStateOf("")
    val snackbarMessage = mutableStateOf("")
    val dropDownMenu1Expanded = mutableStateOf(false)
    val dropDownMenu1SelectedIndex = mutableStateOf(0)
    val dropDownMenu2Expanded = mutableStateOf(false)
    val dropDownMenu2SelectedIndex = mutableStateOf(0)

    fun onTriggerEvent(event: ExchangeUseCaseEvent) {
        viewModelScope.launch {
            try {

                when(event) {
                    is ExchangeUseCaseEvent.NewSearchEvent -> {
                        newSearch()
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG,"onTriggerEvent: $event Exception: $e, ${e.cause}")
            }

        }
    }

    private fun newSearch() {

        searchExchange.execute(
            currencies = currencies.value
        ).onEach { dataState ->

            loading.value = dataState.loading

            dataState.data?.let { response ->
                appendQuotes(response)
            }

            dataState.error?.let { error ->
                errorMessage.value = error
                println("okej newSearch -> error: $error")
            }

        }.launchIn(viewModelScope)

    }

    private fun appendQuotes(response: ExchangeResponse) {
        val newList = mutableListOf<HashMap<String, Double>>()

        response.quotes.forEach {
            val newEntry = HashMap<String, Double>()
            newEntry[it.key] = it.value
            newList.add(newEntry)
        }

        quotesList.value = newList
    }

    fun onQueryChanged(query: String) {
        setQuery(query = query)
    }

    private fun setQuery(query: String) {
        this.searchBarQueryText.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}
