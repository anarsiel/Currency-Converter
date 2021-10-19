import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import remote.Converter

class ApplicationTest {
//    val mk = mock<Converter>()

    @Test
    fun `Identical Correct Currencies`() {
        withTestApplication(Application::launch) {
            handleRequest(HttpMethod.Get, "/convert?from=USD&to=USD").apply {
                val expectedConverterResponse = """
                    |{
                        |"response":"1 USD = 1 USD",
                        |"errorMessage":null
                    |}
                """.trimMargin().filterNot { it == '\n' }

                assertThat(HttpStatusCode.OK).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(expectedConverterResponse)
            }
        }
    }

    @Test
    fun `Incorrect 'from' parameter`() {
        withTestApplication(Application::launch) {
            handleRequest(HttpMethod.Get, "/convert?from=INCORRECT&to=USD").apply {
                val expectedConverterResponse = """
                    |{
                        |"response":null,
                        |"errorMessage":"core.ValidatorException: Wrong `from` parameter: `INCORRECT`"
                    |}
                """.trimMargin().filterNot { it == '\n' }

                assertThat(HttpStatusCode.InternalServerError).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(expectedConverterResponse)
            }
        }
    }

    @Test
    fun `Incorrect 'to' parameter`() {
        withTestApplication(Application::launch) {
            handleRequest(HttpMethod.Get, "/convert?from=USD&to=INCORRECT").apply {
                val expectedConverterResponse = """
                    |{
                        |"response":null,
                        |"errorMessage":"core.ValidatorException: Wrong `to` parameter: `INCORRECT`"
                    |}
                """.trimMargin().filterNot { it == '\n' }

                assertThat(HttpStatusCode.InternalServerError).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(expectedConverterResponse)
            }
        }
    }

    @Test
    fun `Page not found`() {
        withTestApplication(Application::launch) {
            handleRequest(HttpMethod.Get, "/PaGeNoTfOuNd").apply {
                assertThat(HttpStatusCode.NotFound).isEqualTo(response.status())
                assertThat(response.content).isNull()
            }
        }
    }
}

