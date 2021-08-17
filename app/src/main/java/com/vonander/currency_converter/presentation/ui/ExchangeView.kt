package com.vonander.currency_converter.presentation.ui

import androidx.compose.foundation.layout.*
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

    val items = listOf(
        "English",
        "Russian",
        "Spanish",
        "French",
        "German",
        "Swedish"
    )

    CurrencyConverterTheme {

        Scaffold(
            topBar = {
                SearchBar(
                    query = viewModel.searchBarQueryText.value,
                    result = viewModel.searchBarResultText.value,
                    onQueryChanged = { viewModel.onQueryChanged( query = it) },
                    onExecuteSearch = { println("okej ExchangeView - > search!")},
                    exchangeFromLabel = items[viewModel.dropDownMenu1SelectedIndex.value],
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

                Row(
                    modifier = Modifier.padding(top = 10.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {

                        DropDownMenu(
                            expanded = viewModel.dropDownMenu1Expanded.value,
                            selectedIndex = viewModel.dropDownMenu1SelectedIndex.value,
                            items = items,
                            onSelect = {
                                viewModel.dropDownMenu1SelectedIndex.value = it
                                viewModel.dropDownMenu1Expanded.value = false
                            },
                            onDismissRequest = {
                                viewModel.dropDownMenu1Expanded.value = false
                            },
                            onButtonClick = {
                                viewModel.dropDownMenu1Expanded.value = true
                            }
                        )

                    }

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {

                        DropDownMenu(
                            expanded = viewModel.dropDownMenu1Expanded.value,
                            selectedIndex = viewModel.dropDownMenu1SelectedIndex.value,
                            items = items,
                            onSelect = {
                                viewModel.dropDownMenu1SelectedIndex.value = it
                                viewModel.dropDownMenu1Expanded.value = false
                            },
                            onDismissRequest = {
                                viewModel.dropDownMenu1Expanded.value = false
                            },
                            onButtonClick = {
                                viewModel.dropDownMenu1Expanded.value = true
                            }
                        )
                    }
                }
            }
        }
    }
}