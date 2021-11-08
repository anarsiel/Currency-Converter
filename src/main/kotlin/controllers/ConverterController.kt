package controllers

import io.ktor.http.*
import remote.Converter
import remote.SuccessfulConverterResponse
import validators.ConverterValidator

class ConverterController(private val converter: Converter, private val validator: ConverterValidator) {
    suspend fun convertFromTo(params: Parameters): SuccessfulConverterResponse {
        val currencies = converter.getListOfAllCurrencies()
        validator.validateConvertFromTo(params, currencies)

        val fromCurrency = params["from"]!!
        val toCurrency = params["to"]!!

        val rate = converter.convert(fromCurrency, toCurrency).toDouble()

        return SuccessfulConverterResponse(
            fromCurrency,
            toCurrency,
            rate,
            null
        )
    }
}