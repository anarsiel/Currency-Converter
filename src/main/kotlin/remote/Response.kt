package remote

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

interface Response

@JsonInclude(Include.NON_NULL)
data class SuccessfulConverterResponse(
    val currencyFrom: String,
    val currencyTo: String,
    val rate: Double,
) : Response

@JsonInclude(Include.NON_NULL)
data class UnsuccessfulConverterResponse(
    val errorMessage: String,
) : Response