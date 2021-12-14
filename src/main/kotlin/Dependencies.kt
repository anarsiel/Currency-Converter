import config.Config
import controllers.ConverterController
import remote.Converter
import validators.ConverterValidator

class Dependencies(
    private val config: Config = Config(),

    private val converterValidator: ConverterValidator = ConverterValidator(),
    private val converter: Converter = Converter(
        remoteConverterApiKey=config.remoteConverterApiKey,
        remoteConverterPrefix=config.remoteConverterPrefix,
        cacheDurationSec=config.converterCacheDurationSec,
    ),

    val converterController: ConverterController = ConverterController(converter, converterValidator)
)