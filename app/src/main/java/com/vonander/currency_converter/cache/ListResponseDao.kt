package com.vonander.currency_converter.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vonander.currency_converter.cache.model.ListResponseEntity

@Dao
interface ListResponseDao {

    @Insert
    suspend fun insertListResponse(response: ListResponseEntity): Long

    @Query("DELETE FROM ListResponseEntity")
    suspend fun deleteAllListResponses()

    @Query("""
        SELECT * FROM ListResponseEntity
    """)
    suspend fun getAllListResponses(): ListResponseEntity
}