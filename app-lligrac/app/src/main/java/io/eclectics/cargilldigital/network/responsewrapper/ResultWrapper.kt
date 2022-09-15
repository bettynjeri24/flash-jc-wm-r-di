package io.eclectics.agritech.cargill.responsewrapper

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null): ResultWrapper<Nothing>()
//    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()

    object NetworkError: ResultWrapper<Nothing>()
}

//Room sealed class
sealed class RoomResultWrapper<out T> {
    data class Success<out T>(val value: T): RoomResultWrapper<T>()
    data class GenericError(val code: Int? = null, val error: String? = null): RoomResultWrapper<Nothing>()
//    data class GenericError(val code: Int? = null, val error: ErrorResponse? = null): ResultWrapper<Nothing>()

    object NetworkError: RoomResultWrapper<Nothing>()
}