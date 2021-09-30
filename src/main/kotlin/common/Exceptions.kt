package common

class ControllerException(message: String) : Exception("common.ControllerException: $message")

class ConverterException(message: String) : Exception("common.ConverterException: $message")