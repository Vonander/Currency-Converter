package com.vonander.currency_converter.navigation

sealed class Screen(
    val route: String
) {
    object ExchangeView: Screen("exchangeView")
}
