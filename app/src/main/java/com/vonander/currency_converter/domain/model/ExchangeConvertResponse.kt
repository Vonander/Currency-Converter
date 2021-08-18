package com.vonander.currency_converter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExchangeConvertResponse(
    var success: Boolean,
    var result: Double?
): Parcelable