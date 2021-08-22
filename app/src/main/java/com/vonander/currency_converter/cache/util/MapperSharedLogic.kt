package com.vonander.currency_converter.cache.util

import kotlinx.parcelize.RawValue

class MapperSharedLogic {

    fun convertStringToBoolean(string: String): Boolean {
        return string == "true"
    }

    fun convertBooleanToString(boolean: Boolean): String {
        return if (boolean) "true" else "false"
    }

    fun convertStringToDoubleHashMap(quotes: String?): HashMap<String, Double> {
        val hashMap = quotes?.dropLast(1)?.split(",")?.associate {
            val (left, right) = it.split("=")
            left to right.toDouble()
        }

        return hashMap as HashMap<String, Double>
    }

    fun convertStringToStringHashMap(currencies: String?): HashMap<String, String> {
        var hashMap = HashMap<String, String>()

        val c = currencies ?: ""

        if (c.isNotEmpty()) {
            hashMap = currencies?.dropLast(1)?.split(",")
                ?.map { it.split("=") }
                ?.map { it.first() to it.last() }
                ?.toMap() as HashMap<String, String>

        } else {
            hashMap["currencies"] = ""
        }

        return hashMap
    }

    fun convertStringToHashMapAny(error: String?): HashMap<String, @RawValue Any> {
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

    fun convertHashMapToString(
        quotes: HashMap<String, Double>? = null,
        currencies: HashMap<String, String>? = null,
        error: HashMap<String, Any>? = null
    ): String {
        val stringBuilder = StringBuilder()

        quotes?.let {
            quotes.forEach { (key, value) ->
                stringBuilder.append("$key=$value,")
            }
        }

        currencies?.let {
            currencies.forEach { (key, value) ->
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