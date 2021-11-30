package validators

import core.ValidatorException
import org.assertj.core.api.Assertions
import org.junit.Test
import kotlin.test.assertFailsWith

class ConverterValidatorTest {
    private val converterValidator = ConverterValidator()

    @Test
    fun `OK Validation`() {
        converterValidator.validateConvertFromTo(
            fromCurrency = "USD",
            toCurrency = "RUB",
            setOf("USD", "RUB")
        )
    }

    @Test
    fun `Incorrect toCurrency`() {
        val exception = assertFailsWith<ValidatorException> {
            converterValidator.validateConvertFromTo(
                fromCurrency = "USD",
                toCurrency = "RUB",
                setOf("USD")
            )
        }
        Assertions.assertThat(exception.message).isEqualTo("Wrong `to` parameter: `RUB`")
    }

    @Test
    fun `Incorrect fromCurrency`() {
        val exception = assertFailsWith<ValidatorException> {
            converterValidator.validateConvertFromTo(
                fromCurrency = "USD",
                toCurrency = "RUB",
                setOf("RUB")
            )
        }
        Assertions.assertThat(exception.message).isEqualTo("Wrong `from` parameter: `USD`")
    }
}