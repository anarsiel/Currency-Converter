package core

sealed class InternalServerException(message: String) : Exception(message)
class ValidatorException(message: String) : InternalServerException(message)

sealed class RemoteException(message: String) : Exception(message)
class ConverterException(message: String) : RemoteException(message)