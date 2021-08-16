package com.vonander.currency_converter.presentation.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.vonander.currency_converter.presentation.ExchangeViewModel


@Composable
fun ExchangeView(
    viewModel: ExchangeViewModel
) {

    Text(text = "testing testing $viewModel")

    //viewModel.onTriggerEvent(event = ExchangeUseCaseEvent.NewSearchEvent)

}