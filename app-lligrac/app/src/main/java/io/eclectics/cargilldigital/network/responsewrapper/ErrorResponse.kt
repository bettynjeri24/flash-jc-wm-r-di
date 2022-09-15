package io.eclectics.agritech.cargill.responsewrapper

class ErrorResponse (val code: Int = 400,
                     message: String = "Error response $code")
