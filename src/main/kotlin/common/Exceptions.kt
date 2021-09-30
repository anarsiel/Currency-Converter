package common

open class InternalServerException(message: String) : Exception(message)
class ControllerException(message: String) : InternalServerException(message)

open class RemoteException(message: String) : Exception(message)
class ConverterException(message: String) : RemoteException(message)