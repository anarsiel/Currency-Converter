import io.ktor.http.*
import io.ktor.server.testing.*
import config.plugins
import config.routing
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class ApplicationTest {
    private val dependencies = Dependencies()

    @Test
    fun `Identical Correct Currencies`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            handleRequest(HttpMethod.Get, "/convert?from=USD&to=USD").apply {
                val expectedConverterResponse = """
                    |{
                        |"currencyFrom":"USD",
                        |"currencyTo":"USD",
                        |"rate":1.0
                    |}
                """.trimMargin().filterNot { it == '\n' }

                assertThat(HttpStatusCode.OK).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(expectedConverterResponse)
            }
        }
    }

    @Test
    fun `Incorrect 'from' parameter`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            handleRequest(HttpMethod.Get, "/convert?from=INCORRECT&to=USD").apply {
                val expectedConverterResponse = """
                    |{
                        |"errorMessage":"Internal service error."
                    |}
                """.trimMargin().filterNot { it == '\n' }

                assertThat(HttpStatusCode.InternalServerError).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(expectedConverterResponse)
            }
        }
    }

    @Test
    fun `Incorrect 'to' parameter`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            handleRequest(HttpMethod.Get, "/convert?from=USD&to=INCORRECT").apply {
                val expectedConverterResponse = """
                    |{
                        |"errorMessage":"Internal service error."
                    |}
                """.trimMargin().filterNot { it == '\n' }

                assertThat(HttpStatusCode.InternalServerError).isEqualTo(response.status())
                assertThat(response.content).isEqualTo(expectedConverterResponse)
            }
        }
    }

    @Test
    fun `Page not found`() {
        withTestApplication({
            plugins()
            routing(dependencies)
        }) {
            handleRequest(HttpMethod.Get, "/PaGeNoTfOuNd").apply {
                assertThat(HttpStatusCode.NotFound).isEqualTo(response.status())
                assertThat(response.content).isNull()
            }
        }
    }
}

