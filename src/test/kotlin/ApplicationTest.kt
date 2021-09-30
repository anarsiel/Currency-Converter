import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import remote.*
import kotlin.test.Test

class ApplicationTest {
    @Test
    fun `Identical Correct Currencies`() {
        withTestApplication(Application::converter) {
            handleRequest(HttpMethod.Get, "/converter?from=USD&to=USD").apply {
                val expectedConverterResponse = SuccessfulConverterResponse("1 USD = 1 USD")

                assertThat(HttpStatusCode.OK).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(serializeResponse(expectedConverterResponse))
            }
        }
    }

    @Test
    fun `Incorrect 'from' parameter`() {
        withTestApplication(Application::converter) {
            handleRequest(HttpMethod.Get, "/converter?from=INCORRECT&to=USD").apply {
                val expectedConverterResponse = UnsuccessfulConverterResponse(
                    errorMessage = "common.ControllerException: Wrong `from` parameter: `INCORRECT`"
                )

                assertThat(HttpStatusCode.InternalServerError).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(serializeResponse(expectedConverterResponse))
            }
        }
    }

    @Test
    fun `Incorrect 'to' parameter`() {
        withTestApplication(Application::converter) {
            handleRequest(HttpMethod.Get, "/converter?from=USD&to=INCORRECT").apply {
                val expectedConverterResponse = UnsuccessfulConverterResponse(
                    errorMessage = "common.ControllerException: Wrong `to` parameter: `INCORRECT`"
                )

                assertThat(HttpStatusCode.InternalServerError).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(serializeResponse(expectedConverterResponse))
            }
        }
    }

    @Test
    fun `Page not found`() {
        withTestApplication(Application::converter) {
            handleRequest(HttpMethod.Get, "/PaGeNoTfOuNd").apply {
                assertThat(HttpStatusCode.NotFound).isEqualTo(response.status())
                assertThat(deserializeResponse(response.content)).isNull()
            }
        }
    }
}

