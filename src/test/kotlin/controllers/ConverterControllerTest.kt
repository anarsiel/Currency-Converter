package controllers

import Dependencies
import config.plugins
import config.routing
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
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

    @After
    fun noMoreInteractions() {
        verifyNoMoreInteractions(converter, converterValidator)
    }

    @Test
    fun `Correct conversion`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            val expectedConverterResponse =
                SuccessfulConverterResponse(currencyFrom = "USD", currencyTo = "RUB", rate = 33.0)

            runBlocking {
                whenever(converter.getListOfAllCurrencies()).thenReturn(setOf("USD", "RUB"))
                whenever(converter.convert(any(), any())).thenReturn(33.0)

                assertThat(converterController.convertFromTo(fromCurrency = "USD", toCurrency = "RUB"))
                    .isEqualTo(expectedConverterResponse)

                inOrder(converter, converterValidator, converter) {
                    verify(converter).getListOfAllCurrencies()
                    verify(converterValidator).validateConvertFromTo("USD", "RUB", setOf("USD", "RUB"))
                    verify(converter).convert("USD", "RUB")
                }
            }
        }
    }
}