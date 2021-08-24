package com.vonander.currency_converter.cache.util

import com.vonander.currency_converter.cache.model.LiveResponseEntity
import com.vonander.currency_converter.domain.model.LiveResponse
import com.vonander.currency_converter.domain.util.DomainMapper

class LiveResponseEntityMapper: DomainMapper<LiveResponseEntity, LiveResponse> {
    private val mapper = MapperSharedLogic()

    override fun mapToDomainModel(model: LiveResponseEntity): LiveResponse {
        return LiveResponse(
            id = model.id,
            success = mapper.convertStringToBoolean(model.success),
            source = model.source,
            quotes = mapper.convertStringToDoubleHashMap(quotes = model.quotes),
            error = mapper.convertStringToHashMapAny(error = model.error)
        )
    }

    override fun mapFromDomainModel(domainModel: LiveResponse): LiveResponseEntity {
        return LiveResponseEntity(
            id = domainModel.id,
            success = mapper.convertBooleanToString(domainModel.success),
            source = domainModel.source,
            quotes = mapper.convertHashMapToString(quotes = domainModel.quotes, error = null),
            error = mapper.convertHashMapToString(quotes = null, error = domainModel.error)
        )
    }

    fun fromEntityList(initial: List<LiveResponseEntity>): List<LiveResponse>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<LiveResponse>): List<LiveResponseEntity>{
        return initial.map { mapFromDomainModel(it) }
    }
}