import config.Config
import controllers.Controllers
import core.InternalServerException
import core.RemoteException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import remote.UnsuccessfulConverterResponse
import routes.convertFromTo


fun main() {
    val dependencies = Dependencies()
    val controllers = Controllers(dependencies)

    embeddedServer(Netty, port = 8080) {
        plugins()
        routing(controllers)
    }.start(wait = true)
}

fun Application.routing(controllers: Controllers) {
    routing {
        convertFromTo(controllers.converterController)
    }
}

fun Application.plugins() {
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
}

// https://free.currconv.com/api/v7/currencies?apiKey=
// http://localhost:8080/convert?from=USD&to=RUB