package routes

import controllers.ConverterController
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.convertFromTo(converterController: ConverterController) {
    get("/convert") {
        val params = call.request.queryParameters
        call.respond(converterController.convertFromTo(params["from"], params["to"]))
    }
}