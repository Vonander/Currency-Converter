package com.vonander.currency_converter.cache

import com.vonander.currency_converter.cache.model.ListResponseEntity

class ListResponseDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): ListResponseDao {

    override suspend fun insertListResponse(response: ListResponseEntity): Long {
        appDatabaseFake.listResponseEntity = response

        return 1
    }

    override suspend fun deleteAllListResponses() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllListResponses(): ListResponseEntity {
        return appDatabaseFake.listResponseEntity
    }
}