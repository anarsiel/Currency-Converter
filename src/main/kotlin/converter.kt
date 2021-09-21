import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.*

private const val apiKey = "0fa9c4e153e4fe5b3668"
private const val prefix = "https://free.currconv.com/api/v7"

suspend fun convert(fromCurrency: String, toCurrency2: String) : HttpResponse {
    val client = HttpClient()
    val request = generateConvertRequest(fromCurrency, toCurrency2)
    return client.get(request)
}

suspend fun getListOfAllCurrencies(): Set<String> {
    val client = HttpClient()
    val request = "$prefix/currencies?apiKey=$apiKey"
    val httpResponse: HttpResponse = client.get(request)
    val responseBody: ByteArray = httpResponse.receive()
    return responseBody.toString(Charsets.UTF_8).split("\"").filter { it.length == 3 }.toSet()
}

fun getNumberFromResponse(conversionResult: String): String {
    return conversionResult.split(":", "}")[1]
}

private fun generateConvertRequest(currency1: String, currency2: String): String {
    val query = currency1 + "_" + currency2
    return "$prefix/convert?q=$query&apiKey=$apiKey"
}

//@Serializable
//data class Customer(val id: Int, val firstName: String, val lastName: String)
