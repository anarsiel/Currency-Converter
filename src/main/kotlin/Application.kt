import common.ControllerException
import config.Config
import config.Dependencies
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerialName

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        gson()
    }

    val config = Config()
    val dependencies = Dependencies(config)

    routing {
        get("/converter{from}{to}") {
            try {
                val fromCurrency = call.request.queryParameters["from"]
                val toCurrency = call.request.queryParameters["to"]
                val rate = dependencies.controller.converter(fromCurrency, toCurrency)
                val response = Response(
                    "1 $fromCurrency = $rate $toCurrency",
                    null
                )
                call.respond(HttpStatusCode.OK, response)
            } catch (e: ControllerException) {
                val response = Response(
                    null,
                    e.message
                )
                call.respond(HttpStatusCode.BadRequest, response)
            }
        }

        get {
            call.respond(HttpStatusCode.NotFound, Response())
        }
    }
}

data class Response(
    @SerialName("converterResponse")
    val converterResponse: String? = null,
    @SerialName("errorMessage")
    val errorMessage: String? = null
)

// https://free.currconv.com/api/v7/currencies?apiKey=
// http://localhost:8080/converter?from=USD&to=RUB