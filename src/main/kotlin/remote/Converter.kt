package remote

import core.IncorrectEndpointException
import core.UnavailableServiceException
import org.json.JSONObject
import java.net.URL

open class Converter(
    private val remoteConverterPrefix: String,
    private val remoteConverterApiKey: String,
) {
    private val allCurrenciesEndpoint = "${remoteConverterPrefix}/currencies"
    private val conversionEndpoint = "${remoteConverterPrefix}/convert"

    open fun convert(fromCurrency: String, toCurrency: String): Double {
        val url = "$conversionEndpoint?" +
                "q=${fromCurrency}_$toCurrency&" +
                "compact=ultra" +
                "&apiKey=${remoteConverterApiKey}"

        try {
            val jsonObject = JSONObject(URL(url).readText())
            return jsonObject["${fromCurrency}_${toCurrency}"].toString().toDouble()
        } catch (e: org.json.JSONException) {
            throw UnavailableServiceException(e.toString())
        } catch (e: NumberFormatException) {
            throw IncorrectEndpointException(e.toString())
        }
    }

    open fun getListOfAllCurrencies(): Set<String> {
        val url = "$allCurrenciesEndpoint?apiKey=${remoteConverterApiKey}"

        try {
            val jsonObject = JSONObject(URL(url).readText())
            return JSONObject(jsonObject["results"].toString()).keySet()
        } catch (e: Exception) {
            throw UnavailableServiceException(e.toString())
        }
    }
}