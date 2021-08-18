package com.vonander.currency_converter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ExchangeListResponse(
    var success: Boolean,
    var currencies: HashMap<String, String>?,
    var error: HashMap<String, @RawValue Any>?
): Parcelable