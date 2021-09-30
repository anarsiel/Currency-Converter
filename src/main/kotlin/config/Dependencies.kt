package config

import controller.Controller
import io.ktor.client.*
import remote.Converter

class Dependencies(val config: Config) {
    val httpClient = HttpClient()
    val controller = Controller(this)
    val converter = Converter(this)
}