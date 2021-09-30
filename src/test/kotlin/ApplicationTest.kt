import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `Identical Correct Currencies`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/converter?from=USD&to=USD").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                val expectedResponse = Response("1 USD = 1 USD")
                assertEquals(
                    Gson().toJson(expectedResponse),
                    response.content
                )
            }
        }
    }

    @Test
    fun `Incorrect 'from' parameter`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/converter?from=INCORRECT&to=USD").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val expectedResponse = Response(
                    errorMessage = "common.ControllerException: Wrong `from` parameter: `INCORRECT`"
                )
                assertEquals(
                    Gson().toJson(expectedResponse),
                    response.content
                )
            }
        }
    }

    @Test
    fun `Incorrect 'to' parameter`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/converter?from=USD&to=INCORRECT").apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                val expectedResponse = Response(
                    errorMessage = "common.ControllerException: Wrong `to` parameter: `INCORRECT`"
                )
                assertEquals(
                    Gson().toJson(expectedResponse),
                    response.content
                )
            }
        }
    }

    @Test
    fun `Page not found`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/PaGeNoTfOuNd").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
                assertEquals(
                    null,
                    response.content
                )
            }
        }
    }

    @Test
    fun `Page not found2`() {
        withTestApplication(Application::module) {
            handleRequest(HttpMethod.Get, "/PaGeNoTfOuNd").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
                assertEquals(
                    null,
                    response.content
                )
            }
        }
    }
}

