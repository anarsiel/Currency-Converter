package controller

import config.Dependencies
import common.ControllerException
import common.ConverterException

class Controller(private val dependencies: Dependencies) {
    suspend fun converter(fromCurrency: String?, toCurrency: String?): String {
        val currencies = dependencies.converter.getListOfAllCurrencies()

        if (!currencies.contains(fromCurrency)) {
            throw ControllerException("Wrong `from` parameter: `$fromCurrency`")
        }

        if (!currencies.contains(toCurrency)) {
            throw ControllerException("Wrong `to` parameter: `$toCurrency`")
        }

        try {
            return dependencies.converter.convert(fromCurrency!!, toCurrency!!)
        } catch (e: ConverterException) {
            throw e.message?.let { ControllerException(it) }!!
        }
    }
}