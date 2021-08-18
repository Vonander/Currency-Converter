package com.vonander.currency_converter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeLiveResponse(
    var success: Boolean,
    var source: String?,
    var quotes: HashMap<String, Double>?
): Parcelable
