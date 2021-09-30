package remote

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

open class Response(
    open val response: Any?,
    open val errorMessage: Any?
)

open class ConverterResponse(
    override val response: String? = null,
    override val errorMessage: String? = null,
) : Response(response, errorMessage)

data class SuccessfulConverterResponse(
    override val response: String,
    override val errorMessage: String? = null,
) : Response(response, errorMessage)

data class UnsuccessfulConverterResponse(
    override val response: String? = null,
    override val errorMessage: String,
) : Response(response, errorMessage)

private val mapper = jacksonObjectMapper()

fun serializeResponse(converterResponse: SuccessfulConverterResponse): String {
    return mapper.writeValueAsString(converterResponse)
}

fun serializeResponse(converterResponse: UnsuccessfulConverterResponse): String {
    return mapper.writeValueAsString(converterResponse)
}

fun deserializeResponse(json: String?): ConverterResponse? {
    return mapper.readValue(json.toString())
}