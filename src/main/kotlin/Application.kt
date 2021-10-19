import config.Config
import controllers.Controllers
import core.Dependencies
import core.InternalServerException
import core.RemoteException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import remote.UnsuccessfulConverterResponse
import routes.convertFromTo


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.launch() {
    install(StatusPages) {
        exception<InternalServerException> { cause ->
            val response = UnsuccessfulConverterResponse(errorMessage = cause.toString())
            call.respond(HttpStatusCode.InternalServerError, response)
        }

        exception<RemoteException> { cause ->
            val response = UnsuccessfulConverterResponse(errorMessage = cause.toString())
            call.respond(HttpStatusCode.BadGateway, response)
        }
    }

    install(ContentNegotiation) {
        jackson()
    }

    val config = Config()
    val dependencies = Dependencies(config)
    val controllers = Controllers(dependencies)

    routing {
        convertFromTo(controllers.converterController)
    }
}

// https://free.currconv.com/api/v7/currencies?apiKey=
// http://localhost:8080/convert?from=USD&to=RUB