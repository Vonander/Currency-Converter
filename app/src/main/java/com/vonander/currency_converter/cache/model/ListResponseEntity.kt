package com.vonander.currency_converter.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listResponseEntity")
data class ListResponseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "success")
    var success: String,

    @ColumnInfo(name = "currencies")
    var currencies: String?,

    @ColumnInfo(name ="error")
    var error: String?,
)