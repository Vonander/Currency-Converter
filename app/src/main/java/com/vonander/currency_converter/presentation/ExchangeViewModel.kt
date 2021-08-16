package com.vonander.currency_converter.presentation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vonander.currency_converter.domain.model.ExchangeResponse
import com.vonander.currency_converter.interactors.SearchExchange
import com.vonander.currency_converter.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val searchExchange: SearchExchange
) : ViewModel() {

    val loading = mutableStateOf(false)
    val currencies = mutableStateOf("")
    val quotesList: MutableState<List<HashMap<String, Float>>> = mutableStateOf(listOf())
    val errorMessage = mutableStateOf("")

    init {
        println("okej ExchangeViewModel init!")
    }

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

                println("okej newSearch -> quotesList: $quotesList")
            }

            dataState.error?.let { error ->
                errorMessage.value = error
                println("okej newSearch -> error: $error")
            }

        }.launchIn(viewModelScope)

    }

    private fun appendQuotes(response: ExchangeResponse) {
        val currentList = ArrayList(this.quotesList.value)

        response.quotes.forEach {
            val newEntry = HashMap<String, Float>()
            newEntry.put(it.key, it.value)
            currentList.add(newEntry)
        }
    }

}
