package routes

import controllers.ConverterController
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.convertFromTo(converterController: ConverterController) {
    get("/convert") {
        call.respond(converterController.convertFromTo(call.request.queryParameters))
        call.application.environment.log.info("SuccessfulConverterResponse ${call.response}")
    }
}