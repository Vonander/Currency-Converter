package com.vonander.currency_converter.network.model

import com.google.gson.annotations.SerializedName

data class ListResponseDto(
    @SerializedName("success")
    var success: Boolean,

    @SerializedName("currencies")
    var currencies: HashMap<String, String>?
)