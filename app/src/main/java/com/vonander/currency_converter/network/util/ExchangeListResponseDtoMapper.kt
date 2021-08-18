package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ExchangeListResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ExchangeListResponseDto

class ExchangeListResponseDtoMapper : DomainMapper<ExchangeListResponseDto, ExchangeListResponse> {
    override fun mapToDomainModel(model: ExchangeListResponseDto): ExchangeListResponse {
        return ExchangeListResponse(
            success = model.success,
            currencies = model.currencies
        )
    }

    override fun mapFromDomainModel(domainModel: ExchangeListResponse): ExchangeListResponseDto {
        return ExchangeListResponseDto(
            success = domainModel.success,
            currencies = domainModel.currencies
        )
    }
}