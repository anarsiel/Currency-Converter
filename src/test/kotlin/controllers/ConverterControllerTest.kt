package controllers

import Dependencies
import config.plugins
import config.routing
import core.ValidatorException
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.mockito.kotlin.*
import remote.Converter
import remote.SuccessfulConverterResponse
import remote.UnsuccessfulConverterResponse
import validators.ConverterValidator
import kotlin.test.assertFailsWith

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

            converter.stub {
                onBlocking { converter.getListOfAllCurrencies() } doReturn (setOf("USD", "RUB"))
                onBlocking { converter.convert(any(), any()) } doReturn ("33.0")
            }

            runBlocking {
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

    @Test
    fun `Validation exception`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            val expectedConverterResponse =
                UnsuccessfulConverterResponse(errorMessage = "msg")

            converter.stub {
                onBlocking { converter.getListOfAllCurrencies() } doReturn (setOf("USD"))
            }

            converterValidator.stub {
                onBlocking { converterValidator.validateConvertFromTo(anyOrNull(), anyOrNull(), any()) } doAnswer {
                    throw ValidatorException("Incorrect toCurrency")
                }
            }

            runBlocking {
                assertThat(converterController.convertFromTo(fromCurrency = "USD", toCurrency = "RUB"))
                    .isEqualTo(expectedConverterResponse)

                val exception = assertFailsWith<ValidatorException> {
                    controller.calcFrequency("tag", 239)
                }
                assertThat("msg").isEqualTo(exception.message)

                inOrder(converter, converterValidator, converter) {
                    verify(converter).getListOfAllCurrencies()
                    verify(converterValidator).validateConvertFromTo("USD", "RUB", setOf("USD"))
                }
            }
        }
    }
}