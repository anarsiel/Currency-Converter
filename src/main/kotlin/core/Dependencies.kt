package core

import config.Config
import io.ktor.client.*
import remote.Converter
import validators.Validators

class Dependencies(config: Config) {

    private val httpClient = HttpClient()

    val converter = Converter(config, httpClient)
    val validators = Validators()
}