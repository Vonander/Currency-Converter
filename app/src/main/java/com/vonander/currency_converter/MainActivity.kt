package com.vonander.currency_converter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.GsonBuilder
import com.vonander.currency_converter.network.responses.CurrencyLayerService
import com.vonander.currency_converter.ui.theme.CurrencyConverterTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }

                val service = Retrofit.Builder()
                    .baseUrl("http://api.currencylayer.com/")
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .build()
                    .create(CurrencyLayerService::class.java)

                CoroutineScope(IO).launch {
                    val response = service.search(
                        access_key = "8a6c606aa70b3d564527035656fd6d75",
                        currencies = "USD,AUD,CAD,PLN,MXN"
                    )
                    println("response: $response")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyConverterTheme {
        Greeting("Android")
    }
}