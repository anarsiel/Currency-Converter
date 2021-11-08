import config.Config
import controllers.ConverterController
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.logging.*
import remote.Converter
import validators.ConverterValidator

class Dependencies(config: Config = Config()) {
    private val httpClient = HttpClient(CIO)  {
        install(Logging)
    }

    private val converter = Converter(config, httpClient)

    private val converterValidator = ConverterValidator()
    val converterController = ConverterController(converter, converterValidator)
}