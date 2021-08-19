package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ExchangeConvertResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ExchangeConvertResponseDto

class ExchangeConvertResponseDtoMapper : DomainMapper<ExchangeConvertResponseDto, ExchangeConvertResponse> {
    override fun mapToDomainModel(model: ExchangeConvertResponseDto): ExchangeConvertResponse {
        return ExchangeConvertResponse(
            success = model.success,
            result = model.result,
            error = model.error
        )
    }

    override fun mapFromDomainModel(domainModel: ExchangeConvertResponse): ExchangeConvertResponseDto {
        return ExchangeConvertResponseDto(
            success = domainModel.success,
            result = domainModel.result,
            error = domainModel.error
        )
    }
}