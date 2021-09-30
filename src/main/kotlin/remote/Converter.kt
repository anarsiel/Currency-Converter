package remote

import config.Dependencies
import common.ConverterException
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Converter(private val dependencies: Dependencies) {
    suspend fun convert(fromCurrency: String, toCurrency: String): String {
        val request = "${dependencies.config.remoteConverterPrefix}/convert?" +
                "q=${fromCurrency}_$toCurrency&" +
                "compact=ultra" +
                "&apiKey=${dependencies.config.remoteConverterApiKey}"
        val jsonAsString = getAndToString(request)
        return jsonAsString.split(":", "}")[1]
    }

    suspend fun getListOfAllCurrencies(): Set<String> {
        val request = "${dependencies.config.remoteConverterPrefix}/currencies?" +
                "apiKey=${dependencies.config.remoteConverterApiKey}"
        val jsonAsString = getAndToString(request)
        return jsonAsString
            .split("\"")
            .filter { it.length == 3 }
            .toSet()
    }

    private suspend fun getAndToString(request: String): String {
        try {
            val httpResponse: HttpResponse = dependencies.httpClient.get(request)
            val responseBody: ByteArray = httpResponse.receive()
            return responseBody.toString(Charsets.UTF_8)
        } catch (e: Exception) {
            throw e.message?.let { ConverterException(it) }!!
        }
    }
}