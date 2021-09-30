import common.ControllerException
import common.ConverterException
import common.InternalServerException
import common.RemoteException
import config.Config
import config.Dependencies
import controller.convertFromTo
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import remote.UnsuccessfulConverterResponse
import remote.serializeResponse

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.converter() {
    install(StatusPages) {
        exception<InternalServerException> { cause ->
            val response = UnsuccessfulConverterResponse(errorMessage = cause.toString())
            call.respond(HttpStatusCode.InternalServerError, serializeResponse(response))
        }

        exception<RemoteException> { cause ->
            val response = UnsuccessfulConverterResponse(errorMessage = cause.toString())
            call.respond(HttpStatusCode.BadGateway, serializeResponse(response))
        }
    }

    val config = Config()
    val dependencies = Dependencies(config)

    routing {
        convertFromTo(dependencies)
    }
}

// https://free.currconv.com/api/v7/currencies?apiKey=
// http://localhost:8080/converter?from=USD&to=RUB