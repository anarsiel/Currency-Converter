import io.ktor.application.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.response.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.html.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            val controller = Controller()

            get("/converter") {
                try {
                    val fromCurrency = call.request.queryParameters["from"]
                    val toCurrency = call.request.queryParameters["to"]
                    val rate = controller.converter(fromCurrency, toCurrency)
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
        }
    }.start(wait = true)
}

data class Response(
    val converterResponse: String?,
    val errorMessage: String?
)

// https://free.currconv.com/api/v7/currencies?apiKey=
// http://localhost:8080/converter?from=USD&to=RUB