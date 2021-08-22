package com.vonander.currency_converter.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liveResponseEntity")
data class LiveResponseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "success")
    var success: String,

    @ColumnInfo(name = "source")
    var source: String?,

    @ColumnInfo(name = "quotes")
    var quotes: String?,

    @ColumnInfo(name ="error")
    var error: String?,
)