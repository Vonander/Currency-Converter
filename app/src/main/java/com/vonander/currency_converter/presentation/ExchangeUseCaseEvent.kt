package com.vonander.currency_converter.presentation

sealed class ExchangeUseCaseEvent {
    object GetSupportedCurrenciesEvent: ExchangeUseCaseEvent()
    object SearchRatesEvent: ExchangeUseCaseEvent()
    object GetCurrencyConversion: ExchangeUseCaseEvent()
}
