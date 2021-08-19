package com.vonander.currency_converter.network.util

import com.vonander.currency_converter.domain.model.ConvertResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import com.vonander.currency_converter.network.model.ConvertResponseDto

class ConvertResponseDtoMapper : DomainMapper<ConvertResponseDto, ConvertResponse> {
    override fun mapToDomainModel(model: ConvertResponseDto): ConvertResponse {
        return ConvertResponse(
            success = model.success,
            result = model.result,
            error = model.error
        )
    }

    override fun mapFromDomainModel(domainModel: ConvertResponse): ConvertResponseDto {
        return ConvertResponseDto(
            success = domainModel.success,
            result = domainModel.result,
            error = domainModel.error
        )
    }
}