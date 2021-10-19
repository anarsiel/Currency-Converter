package validators

import core.ValidatorException
import io.ktor.http.*

class ConverterValidator {
    fun validateConvertFromTo(params: Parameters, correctCurrencies: Set<String>) {
        val fromCurrency = params["from"]
        val toCurrency = params["to"]

        if (fromCurrency == null || !correctCurrencies.contains(fromCurrency)) {
            throw ValidatorException("Wrong `from` parameter: `$fromCurrency`")
        }

        if (toCurrency == null || !correctCurrencies.contains(toCurrency)) {
            throw ValidatorException("Wrong `to` parameter: `$toCurrency`")
        }

    }
}