package remote

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import core.IncorrectEndpointException
import core.UnavailableServiceException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

@Suppress("UnstableApiUsage")
open class Converter(
    remoteConverterPrefix: String,
    private val remoteConverterApiKey: String,
    private val httpClient: HttpClient = HttpClient()
) {
    private val allCurrenciesEndpoint = "${remoteConverterPrefix}/currencies"
    private val conversionEndpoint = "${remoteConverterPrefix}/convert"

    private val successfulRequestsCache: Cache<String, JSONObject> = CacheBuilder.newBuilder().maximumSize(100)
        .expireAfterAccess(10, TimeUnit.HOURS)
        .build()

    fun getSuccessfulRequestsCache() = successfulRequestsCache

    open suspend fun convert(fromCurrency: String, toCurrency: String): Double {
        val url = "$conversionEndpoint?" +
                "q=${fromCurrency}_$toCurrency&" +
                "compact=ultra" +
                "&apiKey=${remoteConverterApiKey}"

        try {
            return when (val cachedJsonObject = successfulRequestsCache.getIfPresent(url)) {
                null -> {
                    val freshJsonObject = getAsJson(url)
                    val rate = freshJsonObject["${fromCurrency}_${toCurrency}"].toString().toDouble()

                    successfulRequestsCache.put(url, freshJsonObject)
                    rate
                }
                else -> cachedJsonObject["${fromCurrency}_${toCurrency}"].toString().toDouble()
            }
        } catch (e: org.json.JSONException) {
            throw UnavailableServiceException(e.toString())
        } catch (e: NumberFormatException) {
            throw IncorrectEndpointException(e.toString())
        }
    }

    open suspend fun getListOfAllCurrencies(): Set<String> {
        val url = "$allCurrenciesEndpoint?apiKey=${remoteConverterApiKey}"

        try {
            return when (val cachedJsonObject = successfulRequestsCache.getIfPresent(url)) {
                null -> {
                    val freshJsonObject = getAsJson(url)
                    val setOfCurrencies = JSONObject(freshJsonObject["results"].toString()).keySet()

                    successfulRequestsCache.put(url, freshJsonObject)
                    setOfCurrencies
                }
                else -> JSONObject(cachedJsonObject["results"].toString()).keySet()
            }
        } catch (e: Exception) {
            throw UnavailableServiceException(e.toString())
        }
    }

    private suspend fun getAsJson(url: String): JSONObject {
        try {
            val httpResponse: HttpResponse = httpClient.get(url)
            val responseBody: ByteArray = httpResponse.receive()
            return JSONObject(responseBody.toString(Charsets.UTF_8))
        } catch (e: Exception) {
            throw UnavailableServiceException(e.toString())
        }
    }
}