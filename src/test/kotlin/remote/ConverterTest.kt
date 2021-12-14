package remote

import core.IncorrectEndpointException
import core.UnavailableServiceException
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.Test
import java.io.File
import kotlin.test.assertFailsWith

@Suppress("UnstableApiUsage")
class ConverterTest {
    private val resourcesPath = "src/test/resources"

    private val port = 30000
    private val fakeRemoteConverterApiKey = "fakeApi2390123456789"
    private val prefix = "http://localhost:$port"

    private val healthyClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (true) {
                    request.url.toString().startsWith("$prefix/currencies") -> {
                        respond(File("$resourcesPath/fakeCurrenciesList.json").readText())
                    }
                    request.url.toString().startsWith("$prefix/convert") -> {
                        respond(File("$resourcesPath/fakeUSDRUBResponse.json").readText())
                    }
                    else -> error("Unhandled ${request.url}")
                }
            }
        }
    }

    private val unhealthyClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (true) {
                    request.url.toString().startsWith("$prefix/currencies") -> {
                        respond(File("$resourcesPath/unexpectedServiceResponse.json").readText())
                    }
                    request.url.toString().startsWith("$prefix/convert") -> {
                        respond(File("$resourcesPath/unexpectedServiceResponse.json").readText())
                    }
                    else -> error("Unhandled ${request.url}")
                }
            }
        }
    }

    private val deadClient = HttpClient(MockEngine) {
        engine {
            addHandler { _ ->
                throw Exception("Client is dead")
            }
        }
    }

    private val unexpectedRateResponseClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                when (true) {
                    request.url.toString().startsWith("$prefix/convert") -> {
                        respond(File("$resourcesPath/unexpectedUSDRUBRateResponse.json").readText())
                    }
                    else -> error("Unhandled ${request.url}")
                }
            }
        }
    }

    private val healthyConverter = Converter(
        prefix,
        fakeRemoteConverterApiKey,
        healthyClient
    )

    private val unhealthyConverter = Converter(
        prefix,
        fakeRemoteConverterApiKey,
        unhealthyClient
    )

    private val unexpectedRateResponseConverter = Converter(
        prefix,
        fakeRemoteConverterApiKey,
        unexpectedRateResponseClient
    )

    private val deadClientConverter = Converter(
        prefix,
        fakeRemoteConverterApiKey,
        deadClient
    )

    @Test
    fun ` successful getListOfAllCurrencies response `() {
        runBlocking {
            assertThat(healthyConverter.getListOfAllCurrencies())
                .isEqualTo(setOf("USD", "RUB"))
        }
    }

    @Test
    fun ` successful conversion response `() {
        runBlocking {
            assertThat(
                healthyConverter
                    .convert("USD", "RUB")
                    .toString()
            ).isEqualTo(
                JSONObject(File("$resourcesPath/fakeUSDRUBResponse.json").readText())["USD_RUB"].toString()
            )
        }
    }


    @Test
    fun ` unexpected service response on getListOfAllCurrencies `() {
        runBlocking {
            val exception = assertFailsWith<UnavailableServiceException> {
                unhealthyConverter.getListOfAllCurrencies()
            }
            assertThat(exception.message).isEqualTo("org.json.JSONException: JSONObject[\"results\"] not found.")
        }
    }

    @Test
    fun ` unexpected service response on conversion `() {
        runBlocking {
            val exception = assertFailsWith<UnavailableServiceException> {
                unhealthyConverter.convert("USD", "RUB")
            }
            assertThat(exception.message)
                .isEqualTo("org.json.JSONException: JSONObject[\"USD_RUB\"] not found.")
        }
    }

    @Test
    fun ` unexpected rate on conversion `() {
        runBlocking {
            val exception = assertFailsWith<IncorrectEndpointException> {
                unexpectedRateResponseConverter.convert("USD", "RUB")
            }
            assertThat(exception.message)
                .isEqualTo("java.lang.NumberFormatException: For input string: \"Not a number\"")
        }
    }

    @Test
    fun ` dead client `() {
        runBlocking {
            val exception = assertFailsWith<UnavailableServiceException> {
                deadClientConverter.convert("USD", "RUB")
            }
            assertThat(exception.message)
                .isEqualTo("java.lang.Exception: Client is dead")
        }
    }

    @Test
    fun ` successful caching currencies`() {
        runBlocking {
            healthyConverter.getListOfAllCurrencies()
            assertThat(healthyConverter
                .getSuccessfulRequestsCache()
                .getIfPresent("$prefix/currencies?apiKey=$fakeRemoteConverterApiKey").toString()
            ).isEqualTo(JSONObject(File("$resourcesPath/fakeCurrenciesList.json").readText()).toString())
        }
    }

    @Test
    fun ` successful caching rate`() {
        runBlocking {
            healthyConverter.convert("USD", "RUB")
            assertThat(healthyConverter
                .getSuccessfulRequestsCache()
                .getIfPresent("$prefix/convert?q=USD_RUB&compact=ultra&apiKey=$fakeRemoteConverterApiKey")
                .toString()
            ).isEqualTo(JSONObject(File("$resourcesPath/fakeUSDRUBResponse.json").readText()).toString())
        }
    }
}