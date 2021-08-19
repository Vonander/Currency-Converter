package com.vonander.currency_converter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeListResponse(
    var success: Boolean,
    var currencies: HashMap<String, String>?
): Parcelable