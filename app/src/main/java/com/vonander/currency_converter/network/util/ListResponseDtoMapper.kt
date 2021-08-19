package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ListResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ListResponseDto

class ListResponseDtoMapper : DomainMapper<ListResponseDto, ListResponse> {
    override fun mapToDomainModel(model: ListResponseDto): ListResponse {
        return ListResponse(
            success = model.success,
            currencies = model.currencies
        )
    }

    override fun mapFromDomainModel(domainModel: ListResponse): ListResponseDto {
        return ListResponseDto(
            success = domainModel.success,
            currencies = domainModel.currencies
        )
    }
}