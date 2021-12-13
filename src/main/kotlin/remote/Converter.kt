package remote

import core.IncorrectEndpointException
import core.UnavailableServiceException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.json.JSONObject

open class Converter(
    remoteConverterPrefix: String,
    private val remoteConverterApiKey: String,
    private val httpClient: HttpClient = HttpClient()
) {
    private val allCurrenciesEndpoint = "${remoteConverterPrefix}/currencies"
    private val conversionEndpoint = "${remoteConverterPrefix}/convert"

    open suspend fun convert(fromCurrency: String, toCurrency: String): Double {
        val url = "$conversionEndpoint?" +
                "q=${fromCurrency}_$toCurrency&" +
                "compact=ultra" +
                "&apiKey=${remoteConverterApiKey}"

        try {
            val jsonObject = getAsJson(url)
            return jsonObject["${fromCurrency}_${toCurrency}"].toString().toDouble()
        } catch (e: org.json.JSONException) {
            throw UnavailableServiceException(e.toString())
        } catch (e: NumberFormatException) {
            throw IncorrectEndpointException(e.toString())
        }
    }

    open suspend fun getListOfAllCurrencies(): Set<String> {
        val url = "$allCurrenciesEndpoint?apiKey=${remoteConverterApiKey}"

        try {
            val jsonObject = getAsJson(url)
            return JSONObject(jsonObject["results"].toString()).keySet()
        } catch (e: Exception) {
            throw UnavailableServiceException(e.toString())
        }
    }

    private suspend fun getAsJson(url: String): JSONObject {
        try {
            val httpResponse: HttpResponse =  httpClient.get(url)
            val responseBody: ByteArray = httpResponse.receive()
            return JSONObject(responseBody.toString(Charsets.UTF_8))
        }  catch (e: Exception) {
            throw UnavailableServiceException(e.toString())
        }
    }
}