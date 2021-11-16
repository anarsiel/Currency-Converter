package core

sealed class InternalServerException(message: String) : Exception(message)
class ValidatorException(message: String) : InternalServerException(message)

sealed class RemoteException(message: String) : Exception(message)
class UnavailableServiceException(message: String) : RemoteException(message)
class IncorrectEndpointException(message: String) : RemoteException(message)