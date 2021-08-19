package com.vonander.currency_converter.cache.util

import com.vonander.currency_converter.cache.model.LiveResponseEntity
import com.vonander.currency_converter.domain.model.LiveResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import kotlinx.parcelize.RawValue

class LiveResponseEntityMapper: DomainMapper<LiveResponseEntity, LiveResponse> {
    override fun mapToDomainModel(model: LiveResponseEntity): LiveResponse {
        return LiveResponse(
            success = convertStringToBoolean(model.success),
            source = model.source,
            quotes = convertStringDoubleToHashMap(model.quotes!!),
            error = convertStringAnyToHashMap(model.error!!)
        )
    }

    override fun mapFromDomainModel(domainModel: LiveResponse): LiveResponseEntity {
        return LiveResponseEntity(
            success = convertBooleanToString(domainModel.success),
            source = domainModel.source,
            quotes = convertHashMapDoubleToString(domainModel.quotes),
            error = convertHashMapAnyToString(domainModel.error)
        )
    }

    private fun convertStringToBoolean(string: String): Boolean {
        return string == "true"
    }

    private fun convertBooleanToString(boolean: Boolean): String {
        return if (boolean) "true" else "false"
    }

    private fun convertStringAnyToHashMap(string: String): HashMap<String, @RawValue Any> {
        val parts = string.split(",")
        val hashMap = HashMap<String, @RawValue Any>()
        hashMap[parts[0]] = parts[1]

        return hashMap
    }

    private fun convertStringDoubleToHashMap(string: String): HashMap<String, Double> {
        val parts = string.split(",")
        val hashMap = HashMap<String, Double>()
        hashMap[parts[0]] = parts[1].toDouble()

        return hashMap
    }

    private fun convertHashMapAnyToString(hashMap: HashMap<String, @RawValue Any>? ): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(hashMap?.keys?.first())
        stringBuilder.append(",")
        stringBuilder.append(hashMap?.getValue(hashMap.keys.first()))

        return stringBuilder.toString()
    }

    private fun convertHashMapDoubleToString(hashMap: HashMap<String, Double>? ): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(hashMap?.keys?.first())
        stringBuilder.append(",")
        stringBuilder.append(hashMap?.getValue(hashMap.keys.first()))

        return stringBuilder.toString()
    }
}