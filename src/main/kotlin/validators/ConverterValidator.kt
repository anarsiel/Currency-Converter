package validators

import core.ValidatorException

open class ConverterValidator {
    open fun validateConvertFromTo(fromCurrency: String?, toCurrency: String?, correctCurrencies: Set<String>) {
        if (fromCurrency == null || !correctCurrencies.contains(fromCurrency)) {
            throw ValidatorException("Wrong `from` parameter: `$fromCurrency`")
        }

        if (toCurrency == null || !correctCurrencies.contains(toCurrency)) {
            throw ValidatorException("Wrong `to` parameter: `$toCurrency`")
        }

    }
}