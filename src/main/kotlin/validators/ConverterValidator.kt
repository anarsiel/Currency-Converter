package validators

import core.ValidatorException

open class ConverterValidator {
    open fun validateConvertFromTo(fromCurrency: String?, toCurrency: String?, correctCurrencies: Set<String>) {
        if (fromCurrency == null || fromCurrency !in correctCurrencies) {
            throw ValidatorException("Wrong `from` parameter: `$fromCurrency`")
        }

        if (toCurrency == null || toCurrency !in correctCurrencies) {
            throw ValidatorException("Wrong `to` parameter: `$toCurrency`")
        }
    }
}