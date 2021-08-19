package com.vonander.currency_converter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vonander.currency_converter.presentation.ExchangeViewModel

@Composable
fun SearchBar(
    viewModel: ExchangeViewModel,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit
) {
    val query = viewModel.searchBarQueryText.value
    val result = viewModel.searchBarResultText.value
    val exchangeFromLabel = viewModel.exchangeFromCurrency.value
    val exchangeToLabel = viewModel.exchangeToCurrency.value

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.secondary,
        elevation = 8.dp
    ) {

        Column {
            val focusManager = LocalFocusManager.current

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 10.dp)
                    ,
                    value = query,
                    onValueChange = { onQueryChanged(it) },
                    label = { Text( text = exchangeFromLabel) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, "search icon")
                    },
                    trailingIcon = {
                        Icon(imageVector = Icons.Filled.ArrowForward, "arrow forward")
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onExecuteSearch()
                            focusManager.clearFocus(force = true)
                        }
                    ),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    ),
                )

                TextField(
                    modifier = Modifier.padding(top = 10.dp),
                    value = result,
                    onValueChange = {},
                    enabled = false,
                    label = { Text( text = exchangeToLabel) },
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface
                    )
                )
            }
        }
    }
}