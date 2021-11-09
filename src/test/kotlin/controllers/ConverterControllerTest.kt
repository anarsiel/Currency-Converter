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
            val params = Parameters.build {
                mapOf("from" to "USD", "to" to "USD")
            }
            val expectedConverterResponse = """
                |{
                    |"currencyFrom":"USD",
                    |"currencyTo":"USD",
                    |"rate":1.0
                |}
            """.trimMargin().filterNot { it == '\n' }

            converter.stub {
                onBlocking { converter.getListOfAllCurrencies() } doReturn(setOf("USD"))
                onBlocking { converter.convert(any(), any()) } doReturn("1.0")
            }

//            runBlocking {
//                whenever(converter.getListOfAllCurrencies()).thenReturn(setOf("USD"))
//                whenever(converter.convert(any(), any())).thenReturn("1.0")
//            }

            runBlocking {
                assertThat(converterController.convertFromTo(params))
                    .isEqualTo(expectedConverterResponse)
            }
        }
    }
}