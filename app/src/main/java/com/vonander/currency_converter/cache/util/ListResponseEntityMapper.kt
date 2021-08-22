package com.vonander.currency_converter.cache.util

import com.vonander.currency_converter.cache.model.ListResponseEntity
import com.vonander.currency_converter.domain.model.ListResponse
import com.vonander.currency_converter.domain.util.DomainMapper

class ListResponseEntityMapper: DomainMapper<ListResponseEntity, ListResponse> {
    val mapper = MapperSharedLogic()

    override fun mapToDomainModel(model: ListResponseEntity): ListResponse {
        return ListResponse(
            id = model.id,
            success = mapper.convertStringToBoolean(model.success),
            currencies = mapper.convertStringToStringHashMap(currencies = model.currencies),
            error = mapper.convertStringToHashMapAny(model.error)
        )
    }

    override fun mapFromDomainModel(domainModel: ListResponse): ListResponseEntity {
        return ListResponseEntity(
            id = domainModel.id,
            success = mapper.convertBooleanToString(domainModel.success),
            currencies = mapper.convertHashMapToString(currencies = domainModel.currencies),
            error = mapper.convertHashMapToString(error = domainModel.error)
        )
    }
}