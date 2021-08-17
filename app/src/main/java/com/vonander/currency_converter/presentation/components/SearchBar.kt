package com.vonander.currency_converter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun SearchBar(
    query: String,
    result: String,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    exchangeFromLabel: String,
    exchangeToLabel: String
) {

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
                    modifier = Modifier.fillMaxWidth(0.5f),
                    value = query,
                    onValueChange = { onQueryChanged(it) },
                    label = { Text( text = exchangeFromLabel) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
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
                            println("okej search!")
                            onExecuteSearch()
                            focusManager.clearFocus(force = true)
                        },
                        onDone = {
                            println("okej done!")
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