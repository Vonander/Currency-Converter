package com.vonander.currency_converter.presentation.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vonander.currency_converter.presentation.ExchangeUseCaseEvent
import com.vonander.currency_converter.presentation.ExchangeViewModel
import com.vonander.currency_converter.presentation.components.DropDownMenu
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
                    onQueryChanged = { viewModel.onQueryChanged( query = it) },
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(
                            event = ExchangeUseCaseEvent.GetCurrencyConversion
                        )
                    },
                    exchangeFromLabel = viewModel.exchangeFromCurrency.value,
                    exchangeToLabel = viewModel.exchangeToCurrency.value
                )
            },
            bottomBar = {
                Column(modifier = Modifier.padding(bottom = 50.dp)) {

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight(0.6f)
                            .fillMaxWidth()
                            .horizontalScroll(
                                state = ScrollState(0), enabled = true
                            )
                            .background(color = MaterialTheme.colors.primary)
                    ) {
                        itemsIndexed(
                            items = viewModel.ratesList.value
                        ) { _, rates ->

                            Text(
                                text = "$rates",
                                modifier = Modifier
                                    .padding(5.dp)
                            )
                        }
                    }
                }
            },
            scaffoldState = scaffoldState,
            drawerContent = {},
            snackbarHost = { scaffoldState.snackbarHostState }
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {

                Column(modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(0.5f)) {

                    DropDownMenu(
                        expanded = viewModel.dropDownMenu1Expanded.value,
                        selectedIndex = viewModel.dropDownMenu1SelectedIndex.value,
                        items = viewModel.supportedCurrenciesList.value,
                        onSelect = {
                            viewModel.dropDownMenu1Expanded.value = false
                            viewModel.updateCurrencyLazyColumnList(index = it)
                        },
                        onDismissRequest = {
                            viewModel.dropDownMenu1Expanded.value = false
                        },
                        onButtonClick = {
                            viewModel.dropDownMenu1Expanded.value = true
                        }
                    )
                }

                Column(modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()) {

                    DropDownMenu(
                        expanded = viewModel.dropDownMenu2Expanded.value,
                        selectedIndex = viewModel.dropDownMenu2SelectedIndex.value,
                        items = viewModel.supportedCurrenciesList.value,
                        onSelect = {
                            viewModel.dropDownMenu2Expanded.value = false
                            viewModel.updateMenu2Label(index = it)
                        },
                        onDismissRequest = {
                            viewModel.dropDownMenu2Expanded.value = false
                        },
                        onButtonClick = {
                            viewModel.dropDownMenu2Expanded.value = true
                        }
                    )
                }
            }
        }
    }
}