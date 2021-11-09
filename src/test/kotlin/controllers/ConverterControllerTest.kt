package controllers

import Dependencies
import config.plugins
import config.routing
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.kotlin.*
import remote.Converter
import remote.SuccessfulConverterResponse
import validators.ConverterValidator

class ConverterControllerTest {
    private val converter = mock<Converter>()
    private val converterValidator = mock<ConverterValidator>()
    private val dependencies = Dependencies(
        converter = converter,
        converterValidator = converterValidator,
    )
    private val converterController = dependencies.converterController

    @Test
    fun `Correct conversion`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            val expectedConverterResponse = SuccessfulConverterResponse(currencyFrom="USD", currencyTo="USD", rate=1.0)

            converter.stub {
                onBlocking { converter.getListOfAllCurrencies() } doReturn(setOf("USD"))
                onBlocking { converter.convert(any(), any()) } doReturn("1.0")
            }

            runBlocking {
                assertThat(converterController.convertFromTo(fromCurrency = "USD", toCurrency = "USD"))
                    .isEqualTo(expectedConverterResponse)
            }
        }
    }
}