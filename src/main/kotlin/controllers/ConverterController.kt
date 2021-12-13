package controllers

import remote.Converter
import remote.SuccessfulConverterResponse
import validators.ConverterValidator

class ConverterController(private val converter: Converter, private val validator: ConverterValidator) {
    suspend fun convertFromTo(fromCurrency: String?, toCurrency: String?): SuccessfulConverterResponse {
        val currencies = converter.getListOfAllCurrencies()
        validator.validateConvertFromTo(fromCurrency, toCurrency, currencies)

        val rate = converter.convert(fromCurrency!!, toCurrency!!)

        return SuccessfulConverterResponse(
            fromCurrency,
            toCurrency,
            rate
        )
    }
}