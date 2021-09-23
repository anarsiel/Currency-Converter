import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Converter {
    private val client = HttpClient()
    private val apiKey = "0fa9c4e153e4fe5b3668"
    private val prefix = "https://free.currconv.com/api/v7"

    suspend fun convert(fromCurrency: String, toCurrency: String): String {
        val request = "$prefix/convert?" +
                "q=${fromCurrency}_$toCurrency&" +
                "compact=ultra" +
                "&apiKey=$apiKey"
        val jsonAsString = getAndToString(request)
        return jsonAsString.split(":", "}")[1]
    }

    suspend fun getListOfAllCurrencies(): Set<String> {
        val request = "$prefix/currencies?apiKey=$apiKey"
        val jsonAsString = getAndToString(request)
        return jsonAsString
            .split("\"")
            .filter { it.length == 3 }
            .toSet()
    }

    private suspend fun getAndToString(request: String): String {
        try {
            val httpResponse: HttpResponse = client.get(request)
            val responseBody: ByteArray = httpResponse.receive()
            return responseBody.toString(Charsets.UTF_8)
        } catch (e: Exception) {
            throw e.message?.let { ConverterException(it) }!!
        }
    }
}