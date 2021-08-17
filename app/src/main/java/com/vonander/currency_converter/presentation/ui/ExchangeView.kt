package com.vonander.currency_converter.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vonander.currency_converter.presentation.ExchangeViewModel
import com.vonander.currency_converter.presentation.components.SearchBar
import com.vonander.currency_converter.ui.theme.CurrencyConverterTheme


@Composable
fun ExchangeView(
    viewModel: ExchangeViewModel
) {

    val context = LocalContext.current
    val snackbarMessage = viewModel.snackbarMessage.value
    val scaffoldState = rememberScaffoldState()

    CurrencyConverterTheme {

        Scaffold(
            topBar = {

                SearchBar(
                    query = viewModel.searchBarQueryText.value,
                    result = viewModel.searchBarResultText.value,
                    onQueryChanged = {
                        println("okej $it")
                        viewModel.onQueryChanged( query = it)
                    },
                    onExecuteSearch = { println("okej search!")},
                    exchangeFromLabel = viewModel.exchangeFromLabel.value,
                    exchangeToLabel = viewModel.exchangeToLabel.value
                )
            },
            bottomBar = {},
            scaffoldState = scaffoldState,
            drawerContent = {},
            snackbarHost = { scaffoldState.snackbarHostState }
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 10.dp)
                ) {

                    Text(
                        text = "testing testing $viewModel",
                        color = MaterialTheme.colors.primary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}