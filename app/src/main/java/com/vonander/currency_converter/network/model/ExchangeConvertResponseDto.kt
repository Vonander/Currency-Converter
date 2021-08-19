package com.vonander.currency_converter.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue

data class ExchangeConvertResponseDto(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("result")
    var result: Double?,

    @SerializedName("error")
    var error: HashMap<String, @RawValue Any>?
)