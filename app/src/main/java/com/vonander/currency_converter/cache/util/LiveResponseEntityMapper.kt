package com.vonander.currency_converter.cache.util

import com.vonander.currency_converter.cache.model.LiveResponseEntity
import com.vonander.currency_converter.domain.model.LiveResponse
import com.vonander.currency_converter.domain.util.DomainMapper
import kotlinx.parcelize.RawValue

class LiveResponseEntityMapper: DomainMapper<LiveResponseEntity, LiveResponse> {
    override fun mapToDomainModel(model: LiveResponseEntity): LiveResponse {
        return LiveResponse(
            id = model.id,
            success = convertStringToBoolean(model.success),
            source = model.source,
            quotes = convertStringToHashMap(quotes = model.quotes),
            error = convertStringToHashMapAny(error = model.error)
        )
    }

    override fun mapFromDomainModel(domainModel: LiveResponse): LiveResponseEntity {
        return LiveResponseEntity(
            id = domainModel.id,
            success = convertBooleanToString(domainModel.success),
            source = domainModel.source,
            quotes = convertHashMapToString(quotes = domainModel.quotes, error = null),
            error = convertHashMapToString(quotes = null, error = domainModel.error)
        )
    }

    private fun convertStringToBoolean(string: String): Boolean {
        return string == "true"
    }

    private fun convertBooleanToString(boolean: Boolean): String {
        return if (boolean) "true" else "false"
    }

    private fun convertStringToHashMap(quotes: String?): HashMap<String, Double> {
        val hashMap = quotes?.dropLast(1)?.split(",")?.associate {
            val (left, right) = it.split("=")
            left to right.toDouble()
        }

        return hashMap as HashMap<String, Double>
    }

    private fun convertStringToHashMapAny(error: String?): HashMap<String, @RawValue Any> {
        var hashMap = HashMap<String, Any>()
        val e = error ?: ""

        if (e.isNotEmpty()) {

            hashMap = error?.dropLast(1)?.split(",")?.associate {
                val (left, right) = it.split("=")
                left to right as Any
            } as HashMap<String, @RawValue Any>

        } else {

            hashMap["error"] = e
        }

        return hashMap
    }

    private fun convertHashMapToString(
        quotes: HashMap<String, Double>?,
        error: HashMap<String, Any>?
    ): String {
        val stringBuilder = StringBuilder()

        quotes?.let {
            quotes.forEach { (key, value) ->
                stringBuilder.append("$key=$value,")
            }
        }

        error?.let {
            error.forEach { (key, value) ->
                stringBuilder.append("$key=$value,")
            }
        }

        return stringBuilder.toString()
    }
}