import io.ktor.application.call
import io.ktor.client.call.*
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
            get("/converter") {
                val currencies = getListOfAllCurrencies()

                val fromCurrency = call.request.queryParameters["from"]
                val toCurrency = call.request.queryParameters["to"]

                if (currencies.containsAll(arrayListOf(fromCurrency, toCurrency))) {
                    val httpResponse = convert(fromCurrency!!, toCurrency!!)
                    val responseBody: ByteArray = httpResponse.receive()
                    val conversionResult = responseBody.toString(Charsets.UTF_8)
                    val rate = getNumberFromResponse(conversionResult)

                    call.respondHtml(HttpStatusCode.OK) {
                        head {
                            title("Converter main page")
                        }
                        body {
                            div {
                                +"1$fromCurrency = $rate $toCurrency"
                            }
                        }
                    }
                }


            }
        }
    }.start(wait = true)
}

// https://free.currconv.com/api/v7/currencies?apiKey=do-not-use-this-api-key-r5IbMpHmWR8_wg07gb4rs
// http://localhost:8080/converter?from=USD&to=RUB