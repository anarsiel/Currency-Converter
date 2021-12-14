package config

data class Config(
    val remoteConverterApiKey: String = generateRemoteConverterApiKey(),
    val remoteConverterPrefix: String = generateRemoteConverterPrefix(),
    val converterCacheDurationSec: Long = 3600
)

private fun generateRemoteConverterApiKey(): String {
    return "0fa9c4e153e4fe5b3668"
}

private fun generateRemoteConverterPrefix(): String {
    return "https://free.currconv.com/api/v7"
}