package config

import core.InternalServerException
import core.RemoteException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import org.slf4j.event.Level
import remote.UnsuccessfulConverterResponse

fun Application.plugins() {
    install(StatusPages) {
        exception<InternalServerException> { cause ->
            val response = UnsuccessfulConverterResponse(errorMessage = "Internal service error.")
            call.respond(HttpStatusCode.InternalServerError, response)
            log.info("InternalServerException: ${cause.message}")
        }

        exception<RemoteException> { cause ->
            val response = UnsuccessfulConverterResponse(errorMessage = "Remote service internal error.")
            call.respond(HttpStatusCode.BadGateway, response)
            log.info("RemoteException: ${cause.message}")
        }
    }

    install(ContentNegotiation) {
        jackson()
    }

    install(CallLogging) {
        level = Level.INFO
    }
}