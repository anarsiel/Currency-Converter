class Controller {
    suspend fun converter(fromCurrency: String?, toCurrency: String?): String {
        val converter = Converter()
        val currencies = converter.getListOfAllCurrencies()

        if (!currencies.contains(fromCurrency)) {
            throw ControllerException("Wrong `from` parameter: `$fromCurrency`")
        }

        if (!currencies.contains(toCurrency)) {
            throw ControllerException("Wrong `to` parameter: `$toCurrency`")
        }

        try {
            return converter.convert(fromCurrency!!, toCurrency!!)
        } catch (e: ConverterException) {
            throw e.message?.let { ControllerException(it) }!!
        }
    }
}