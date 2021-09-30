package controller

import config.Dependencies
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import remote.SuccessfulConverterResponse
import remote.serializeResponse

fun Route.convertFromTo(dependencies: Dependencies) {
    get("/converter") {
        val fromCurrency = call.request.queryParameters["from"]
        val toCurrency = call.request.queryParameters["to"]
        val rate = dependencies.controller.converter(fromCurrency, toCurrency)
        val converterResponse = SuccessfulConverterResponse(
            "1 $fromCurrency = $rate $toCurrency",
            null
        )
        call.respond(HttpStatusCode.OK, serializeResponse(converterResponse))
    }
}