package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ExchangeResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ExchangeResponseDto

class ExchangeResponseDtoMapper : DomainMapper<ExchangeResponseDto, ExchangeResponse> {
    override fun mapToDomainModel(model: ExchangeResponseDto): ExchangeResponse {
        return ExchangeResponse(
            success = model.success,
            terms = model.terms,
            privacy = model.privacy,
            timeStamp = model.timeStamp,
            source = model.source,
            quotes = model.quotes
        )
    }

    override fun mapFromDomainModel(domainModel: ExchangeResponse): ExchangeResponseDto {
        return ExchangeResponseDto(
            success = domainModel.success,
            terms = domainModel.terms,
            privacy = domainModel.privacy,
            timeStamp = domainModel.timeStamp,
            source = domainModel.source,
            quotes = domainModel.quotes
        )
    }

    fun toDomainList(initial: List<ExchangeResponseDto>): List<ExchangeResponse> {
        return initial.map { mapToDomainModel(it)}
    }

    fun fromDomainList(initial: List<ExchangeResponse>): List<ExchangeResponseDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}