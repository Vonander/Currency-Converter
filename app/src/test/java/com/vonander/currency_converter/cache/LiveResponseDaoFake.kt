package com.vonander.currency_converter.cache

import com.vonander.currency_converter.cache.model.LiveResponseEntity

class LiveResponseDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): LiveResponseDao {

    override suspend fun insertLiveResponse(response: LiveResponseEntity): Long {
        appDatabaseFake.liveResponseEntityList.add(response)

        return 1
    }

    override suspend fun deleteAllLiveResponses() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLiveResponses(): List<LiveResponseEntity> {
        return appDatabaseFake.liveResponseEntityList
    }
}