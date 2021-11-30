package remote

interface Response

data class SuccessfulConverterResponse(
    val currencyFrom: String,
    val currencyTo: String,
    val rate: Double,
) : Response

data class UnsuccessfulConverterResponse(
    val errorMessage: String,
) : Response