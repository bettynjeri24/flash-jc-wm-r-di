package co.ekenya.pepsidistributor.responsewrapper

class ErrorResponse (val code: Int = 400,
                     message: String = "Error response $code")
