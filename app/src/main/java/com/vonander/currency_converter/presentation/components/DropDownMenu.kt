package com.vonander.currency_converter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DropDownMenu(
    expanded: Boolean,
    selectedIndex: Int,
    items: List<String>,
    onSelect: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit
) {
    Box {

        Button(
            onClick = { onButtonClick() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            )
        ) {
            Text(
                text = items[selectedIndex],
                color = MaterialTheme.colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(0.5f)
                .background(
                    color = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            items.forEachIndexed { index, string ->
                if (selectedIndex == index) {
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colors.secondary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        onClick = { onSelect(index) }
                    ) {
                        Text(
                            text = string,
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colors.primary,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        onClick = { onSelect(index) }
                    ) {
                        Text(
                            text = string,
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}