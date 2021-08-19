package com.vonander.currency_converter.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vonander.currency_converter.cache.model.LiveResponseEntity

@Dao
interface LiveResponseDao {

    @Insert
    suspend fun insertLiveResponse(response: LiveResponseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLiveResponses(responses: List<LiveResponseEntity>): LongArray

    @Query("DELETE FROM liveResponseEntity")
    suspend fun deleteAllLiveResponses()

    @Query("""
        SELECT * FROM liveResponseEntity
    """)
    suspend fun getAllLiveResponses(): LiveResponseEntity
}