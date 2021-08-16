package com.vonander.currency_converter.network.model

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponseDto(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("terms")
    var terms: String,

    @SerializedName("privacy")
    var privacy: String,

    @SerializedName("timeStamp")
    var timeStamp: Int,

    @SerializedName("source")
    var source: String,

    @SerializedName("quotes")
    var quotes: HashMap<String, Float>,
)