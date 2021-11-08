package remote

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

interface Response

@JsonInclude(Include.NON_NULL)
data class SuccessfulConverterResponse(
    val currencyFrom: String,
    val currencyTo: String,
    val rate: Double,
    val errorMessage: String? = null,
) : Response

@JsonInclude(Include.NON_NULL)
data class UnsuccessfulConverterResponse(
    val currencyFrom: String? = null,
    val currencyTo: String? = null,
    val rate: Double? = null,
    val errorMessage: String,
) : Response