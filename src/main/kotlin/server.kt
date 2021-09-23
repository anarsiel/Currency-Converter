import io.ktor.application.*
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
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

                    call.respondHtml(HttpStatusCode.OK) {
                        head {
                            title("Converter main page")
                        }
                        body {
                            div {
                                +"1 $fromCurrency = $rate $toCurrency"
                            }
                        }
                    }
                } catch (e: ControllerException) {
                    call.respondHtml(HttpStatusCode.BadRequest) {
                        head {
                            title("Error page")
                        }
                        body {
                            div {
                                + e.message!!
                            }
                        }
                    }
                }
            }
        }
    }.start(wait = true)
}

// https://free.currconv.com/api/v7/currencies?apiKey=
// http://localhost:8080/converter?from=USD&to=RUB