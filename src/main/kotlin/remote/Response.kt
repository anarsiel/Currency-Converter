package remote

interface Response

data class SuccessfulConverterResponse(
    val response: String,
    val errorMessage: String? = null,
) : Response

data class UnsuccessfulConverterResponse(
    val response: String? = null,
    val errorMessage: String,
) : Response