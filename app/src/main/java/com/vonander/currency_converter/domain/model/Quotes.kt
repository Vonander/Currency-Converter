package com.vonander.currency_converter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quotes(
    var quotes: HashMap<String, Float>
): Parcelable
