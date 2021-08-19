package com.vonander.currency_converter.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.vonander.currency_converter.presentation.ExchangeUseCaseEvent
import com.vonander.currency_converter.presentation.ExchangeViewModel
import com.vonander.currency_converter.presentation.components.*
import com.vonander.currency_converter.ui.theme.CurrencyConverterTheme
import kotlinx.coroutines.launch

@Composable
fun ExchangeView(
    viewModel: ExchangeViewModel
) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    CurrencyConverterTheme {

        Scaffold(

            topBar = {
                SearchBar(
                    viewModel = viewModel,
                    onQueryChanged = { viewModel.onQueryChanged( query = it) },
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(
                            event = ExchangeUseCaseEvent.GetCurrencyConversion
                        )
                    }
                )
            },

            bottomBar = {
                Box(
                    modifier = Modifier.padding(bottom = 50.dp)
                ) {

                    CustomLazyColumn(viewModel = viewModel)

                    if (viewModel.errorMessage.value.isNotBlank()) {

                        showSnackbar(
                            viewModel = viewModel,
                            scaffoldState = scaffoldState
                        )

                        DefaultSnackbar(
                            snackbarHostState = scaffoldState.snackbarHostState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 10.dp),
                            onDismiss = {
                                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        )
                    }
                }
            },

            scaffoldState = scaffoldState,

            drawerContent = { AboutView(context = context) },

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
                            viewModel.updateDropdownMenu2Label(index = it)
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

private fun showSnackbar(
    viewModel: ExchangeViewModel,
    scaffoldState: ScaffoldState
) {

    viewModel.viewModelScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            message = viewModel.errorMessage.value,
            actionLabel = "Hide"
        )
    }
}