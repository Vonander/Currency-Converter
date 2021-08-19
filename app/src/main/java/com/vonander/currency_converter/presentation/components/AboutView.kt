package com.vonander.currency_converter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vonander.currency_converter.ui.theme.CurrencyConverterTheme

@Composable
fun AboutView () {
    val uriHandler = LocalUriHandler.current
    val url = "https://www.linkedin.com/in/johanfornander/"

    CurrencyConverterTheme {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.background)
                .padding(bottom = 10.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = "2021 August",
                    color = MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp)
                )

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "linkedin.com/in/johanfornander",
                        color = MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                top = 10.dp,
                                end= 10.dp,
                                bottom = 10.dp
                            )
                            .clickable {
                                uriHandler.openUri(url)
                            }
                    )
                }
            }
        }
    }
}

