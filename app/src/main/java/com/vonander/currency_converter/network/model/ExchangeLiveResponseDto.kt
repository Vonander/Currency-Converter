package com.vonander.currency_converter.network.model

import com.google.gson.annotations.SerializedName

data class ExchangeLiveResponseDto(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("source")
    var source:  String?,

    @SerializedName("quotes")
    var quotes: HashMap<String, Double>?,

    @SerializedName("error")
    var error: HashMap<String, Any>?
)