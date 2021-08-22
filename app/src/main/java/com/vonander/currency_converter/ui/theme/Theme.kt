package com.vonander.currency_converter.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = Teal50_900,
    primaryVariant = Teal50_600,
    secondary = Complementary,
    background = Grey_900,
    onPrimary = Color.White,
    onBackground = Teal50_600,
)

@SuppressLint("ConflictingOnColor")
private val LightColorPalette = lightColors(
    primary = Teal50_400,
    primaryVariant = Teal50_600,
    secondary = Complementary,
    background = Color.White,
    onPrimary = Color.White,
    onBackground = Teal50_600

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CurrencyConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}