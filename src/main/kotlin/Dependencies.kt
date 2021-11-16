import config.Config
import controllers.ConverterController
import remote.Converter
import validators.ConverterValidator

class Dependencies(
    private val config: Config = Config(),

    private val converterValidator: ConverterValidator = ConverterValidator(),
    private val converter: Converter = Converter(
        config.remoteConverterPrefix,
        config.remoteConverterApiKey,
    ),

    val converterController: ConverterController = ConverterController(converter, converterValidator)
)