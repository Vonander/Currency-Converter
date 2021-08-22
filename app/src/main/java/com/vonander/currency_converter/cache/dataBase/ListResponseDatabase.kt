package com.vonander.currency_converter.cache.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vonander.currency_converter.cache.ListResponseDao
import com.vonander.currency_converter.cache.model.ListResponseEntity

@Database(entities = [ListResponseEntity::class], version = 1)
abstract class ListResponseDatabase: RoomDatabase() {

    abstract fun ListResponseDao(): ListResponseDao
}