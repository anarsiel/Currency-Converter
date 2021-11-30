package config

import core.IncorrectEndpointException
import core.InternalServerException
import core.UnavailableServiceException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import org.slf4j.event.Level
import remote.UnsuccessfulConverterResponse

fun Application.plugins() {
    install(StatusPages) {
        exception<Exception> { cause ->
            when (cause) {
                is InternalServerException -> {
                    val response = UnsuccessfulConverterResponse(errorMessage = "${cause.message}")
                    call.respond(HttpStatusCode.InternalServerError, response)
                    log.info("InternalServerException: ${cause.message}")
                }
                is UnavailableServiceException -> {
                    val response = UnsuccessfulConverterResponse(errorMessage = "Remote service internal error.")
                    call.respond(HttpStatusCode.BadGateway, response)
                    log.info("UnavailableServiceException: ${cause.message}")
                }

                is IncorrectEndpointException -> {
                    val response = UnsuccessfulConverterResponse(errorMessage = "Bad request to remote server")
                    call.respond(HttpStatusCode.BadRequest, response)
                    log.info("RemoteException: ${cause.message}")
                }
            }
        }
    }

    install(ContentNegotiation) {
        jackson()
    }

    install(CallLogging) {
        level = Level.INFO
    }
}