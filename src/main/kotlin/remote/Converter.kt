package remote

import config.Config
import core.ConverterException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class Converter(private val config: Config, private val httpClient: HttpClient) {
    suspend fun convert(fromCurrency: String, toCurrency: String): String {
        val request = "${config.remoteConverterPrefix}/convert?" +
                "q=${fromCurrency}_$toCurrency&" +
                "compact=ultra" +
                "&apiKey=${config.remoteConverterApiKey}"
        val jsonAsString = getAndToString(request)
        return jsonAsString.split(":", "}")[1]
    }

    suspend fun getListOfAllCurrencies(): Set<String> {
        val request = "${config.remoteConverterPrefix}/currencies?" +
                "apiKey=${config.remoteConverterApiKey}"
        val jsonAsString = getAndToString(request)
        return jsonAsString
            .split("\"")
            .filter { it.length == 3 }
            .toSet()
    }

    private suspend fun getAndToString(request: String): String {
        try {
            val httpResponse: HttpResponse =  httpClient.get(request)
            val responseBody: ByteArray = httpResponse.receive()
            return responseBody.toString(Charsets.UTF_8)
        }  catch (e: Exception) {
            throw ConverterException(e.toString())
        }
    }
}