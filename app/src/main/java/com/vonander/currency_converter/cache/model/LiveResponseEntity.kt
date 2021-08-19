package com.vonander.currency_converter.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liveResponseEntity")
data class LiveResponseEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "currencyKey")
    var currencyKey: String,

    @ColumnInfo(name ="currencyRate")
    var currencyRate:  Long,
)