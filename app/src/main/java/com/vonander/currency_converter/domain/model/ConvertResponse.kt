package com.vonander.currency_converter.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ConvertResponse(
    var success: Boolean,
    var result: Double?,
    var error: HashMap<String, @RawValue Any>?
): Parcelable