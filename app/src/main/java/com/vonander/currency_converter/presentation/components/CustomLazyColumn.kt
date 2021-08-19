package com.vonander.currency_converter.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vonander.currency_converter.presentation.ExchangeViewModel


@Composable
fun CustomLazyColumn(
    viewModel: ExchangeViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight(0.6f)
            .fillMaxWidth()
            .horizontalScroll(
                state = ScrollState(0), enabled = true
            )
            .background(color = MaterialTheme.colors.background)
    ) {
        itemsIndexed(
            items = viewModel.ratesList.value
        ) { _, rates ->

            Text(
                text = "$rates",
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}