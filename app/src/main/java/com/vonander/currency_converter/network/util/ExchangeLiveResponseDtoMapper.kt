package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ExchangeLiveResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ExchangeLiveResponseDto

class ExchangeLiveResponseDtoMapper : DomainMapper<ExchangeLiveResponseDto, ExchangeLiveResponse> {
    override fun mapToDomainModel(model: ExchangeLiveResponseDto): ExchangeLiveResponse {
        return ExchangeLiveResponse(
            success = model.success,
            source = model.source,
            quotes = model.quotes
        )
    }

    override fun mapFromDomainModel(domainModel: ExchangeLiveResponse): ExchangeLiveResponseDto {
        return ExchangeLiveResponseDto(
            success = domainModel.success,
            source = domainModel.source,
            quotes = domainModel.quotes
        )
    }
}