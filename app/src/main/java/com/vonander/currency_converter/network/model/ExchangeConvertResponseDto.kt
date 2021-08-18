package com.vonander.currency_converter.network.model

import com.google.gson.annotations.SerializedName

data class ExchangeConvertResponseDto(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("result")
    var result: Double?
)