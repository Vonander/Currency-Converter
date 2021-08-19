package com.vonander.currency_converter.cache.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vonander.currency_converter.cache.LiveResponseDao
import com.vonander.currency_converter.cache.model.LiveResponseEntity


@Database(entities = [LiveResponseEntity::class], version = 1)
abstract class LiveResponseDatabase: RoomDatabase() {

    abstract fun LiveResponseDao(): LiveResponseDao
}