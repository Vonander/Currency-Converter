package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ExchangeRateResonse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ExchangeRateResponseDto

class ExchangeRateResponseDtoMapper : DomainMapper<ExchangeRateResponseDto, ExchangeRateResonse> {
    override fun mapToDomainModel(model: ExchangeRateResponseDto): ExchangeRateResonse {
        return ExchangeRateResonse(
            success = model.success,
            terms = model.terms,
            privacy = model.privacy,
            timeStamp = model.timeStamp,
            source = model.source,
            quotes = model.quotes
        )
    }

    override fun mapFromDomainModel(domainModel: ExchangeRateResonse): ExchangeRateResponseDto {
        return ExchangeRateResponseDto(
            success = domainModel.success,
            terms = domainModel.terms,
            privacy = domainModel.privacy,
            timeStamp = domainModel.timeStamp,
            source = domainModel.source,
            quotes = domainModel.quotes
        )
    }

    fun toDomainList(initial: List<ExchangeRateResponseDto>): List<ExchangeRateResonse> {
        return initial.map { mapToDomainModel(it)}
    }

    fun fromDomainList(initial: List<ExchangeRateResonse>): List<ExchangeRateResponseDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}