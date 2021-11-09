import config.Config
import controllers.ConverterController
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.logging.*
import remote.Converter
import validators.ConverterValidator

class Dependencies(
    private val config: Config = Config(),

    private val httpClient: HttpClient = HttpClient(CIO)  {
        install(Logging)
    },

    private val converterValidator: ConverterValidator = ConverterValidator(),
    private val converter: Converter = Converter(config, httpClient),

    val converterController: ConverterController = ConverterController(converter, converterValidator)
)