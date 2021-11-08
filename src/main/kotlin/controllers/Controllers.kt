package controllers

import Dependencies

class Controllers(dependencies: Dependencies) {
    val converterController = ConverterController(dependencies.converter, dependencies.validators.converterValidator)
}