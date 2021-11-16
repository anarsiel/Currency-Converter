package remote

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action.stringContent
import com.xebialabs.restito.semantics.Condition.method
import com.xebialabs.restito.semantics.Condition.startsWithUri
import com.xebialabs.restito.server.StubServer
import core.IncorrectEndpointException
import core.UnavailableServiceException
import org.assertj.core.api.Assertions.assertThat
import org.glassfish.grizzly.http.Method
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class ConverterTest {
    private val port = 30000
    private var server: StubServer? = null
    private val fakeRemoteConverterApiKey = "fakeApi2390123456789"

    private val converter = Converter(
        "http://localhost:$port",
        fakeRemoteConverterApiKey,
    )

    private val fakeCurrenciesList = """
        {
            "results": {
                "RUB": {
                    "currencyName": "Russian Ruble",
                    "currencySymbol": "руб",
                    "id": "RUB"
                },
                "USD": {
                    "currencyName": "United States Dollar",
                    "currencySymbol": "${'$'}",
                    "id": "USD"
                }
            }
        }
    """.trimIndent()

    private val fakeUSDRUBResponse = """
        {
            "USD_RUB": 30.0
        }
    """.trimIndent()

    private val unexpectedServiceResponse = """
        {
            "UNEXPECTED": "unexpected text"
        }
    """.trimIndent()

    private val unexpectedUSDRUBRateResponse = """
        {
            "USD_RUB": "Not a number"
        }
    """.trimIndent()

    @Before
    fun start() {
        server = StubServer(port).run()
    }

    @After
    fun stop() {
        server?.stop()
    }

    @Test
    fun ` successful getListOfAllCurrencies response `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/currencies")
            )
            .then(
                stringContent(fakeCurrenciesList)
            )

        assertThat(converter.getListOfAllCurrencies())
            .isEqualTo(setOf("USD", "RUB"))
    }

    @Test
    fun ` unexpected service response on getListOfAllCurrencies `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/currencies")
            )
            .then(
                stringContent(unexpectedServiceResponse)
            )

        val exception = assertFailsWith<UnavailableServiceException> {
            converter.getListOfAllCurrencies()
        }
        assertThat("org.json.JSONException: JSONObject[\"results\"] not found.").isEqualTo(exception.message)
    }

    @Test
    fun ` successful conversion `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/convert")
            )
            .then(
                stringContent(fakeUSDRUBResponse)
            )

        assertThat(converter.convert("USD", "RUB"))
            .isEqualTo(30.0)
    }


    @Test
    fun ` unexpected service response on conversion `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/convert")
            )
            .then(
                stringContent(unexpectedServiceResponse)
            )

        val exception = assertFailsWith<UnavailableServiceException> {
            converter.convert("USD", "RUB")
        }
        assertThat("org.json.JSONException: JSONObject[\"USD_RUB\"] not found.").isEqualTo(exception.message)
    }

    @Test
    fun ` unexpected rate on conversion `() {
        whenHttp(server)
            .match(
                method(Method.GET),
                startsWithUri("/convert")
            )
            .then(
                stringContent(unexpectedUSDRUBRateResponse)
            )

        val exception = assertFailsWith<IncorrectEndpointException> {
            converter.convert("USD", "RUB")
        }
        assertThat("java.lang.NumberFormatException: For input string: \"Not a number\"").isEqualTo(exception.message)
    }
}