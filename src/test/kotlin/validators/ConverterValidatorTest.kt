package validators

import core.ValidatorException
import org.assertj.core.api.Assertions
import org.junit.Test
import kotlin.test.assertFailsWith

class ConverterValidatorTest {
    private val converterValidator = ConverterValidator()

    @Test
    fun `OK Validation`(): Unit {
        converterValidator.validateConvertFromTo(
            fromCurrency = "USD",
            toCurrency = "RUB",
            setOf("USD", "RUB")
        )
    }

    @Test
    fun `Incorrect toCurrency`(): Unit {
        val exception = assertFailsWith<ValidatorException> {
            converterValidator.validateConvertFromTo(
                fromCurrency = "USD",
                toCurrency = "RUB",
                setOf("USD")
            )
        }
        Assertions.assertThat("Wrong `to` parameter: `RUB`").isEqualTo(exception.message)
    }

    @Test
    fun `Incorrect fromCurrency`(): Unit {
        val exception = assertFailsWith<ValidatorException> {
            converterValidator.validateConvertFromTo(
                fromCurrency = "USD",
                toCurrency = "RUB",
                setOf("RUB")
            )
        }
        Assertions.assertThat("Wrong `from` parameter: `USD`").isEqualTo(exception.message)
    }
}