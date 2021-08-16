package com.vonander.currency_converter.presentation

sealed class ExchangeUseCaseEvent {
    object NewSearchEvent: ExchangeUseCaseEvent()
}
