package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.LiveResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.LiveResponseDto

class LiveResponseDtoMapper : DomainMapper<LiveResponseDto, LiveResponse> {
    override fun mapToDomainModel(model: LiveResponseDto): LiveResponse {
        return LiveResponse(
            success = model.success,
            source = model.source,
            quotes = model.quotes,
            error = model.error
        )
    }

    override fun mapFromDomainModel(domainModel: LiveResponse): LiveResponseDto {
        return LiveResponseDto(
            success = domainModel.success,
            source = domainModel.source,
            quotes = domainModel.quotes,
            error = domainModel.error
        )
    }
}