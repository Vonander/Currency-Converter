package com.vonander.currency_converter.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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

            scaffoldState = scaffoldState,

            drawerContent = { AboutView() },

            snackbarHost = { scaffoldState.snackbarHostState }
        ) {

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

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
                    .fillMaxWidth()
                ) {

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

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val supportedCurrenciesLazyColumn = createRef()

                Box(
                    modifier = Modifier
                        .constrainAs(supportedCurrenciesLazyColumn) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(bottom = 40.dp)
                ) {

                    CustomLazyColumn(viewModel = viewModel)

                    viewModel.errorMessage.value.getContentIfNotHandled()?.let { message ->

                        showSnackbar(
                            viewModel = viewModel,
                            scaffoldState = scaffoldState,
                            errorMessage = message
                        )
                    }

                    DefaultSnackbar(
                        snackbarHostState = scaffoldState.snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        onDismiss = {
                            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    )
                }
            }
        }
    }
}

private fun showSnackbar(
    viewModel: ExchangeViewModel,
    scaffoldState: ScaffoldState,
    errorMessage: String
) {

    viewModel.viewModelScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            message = errorMessage,
            actionLabel = "Hide",
            duration = SnackbarDuration.Short
        )
    }
}