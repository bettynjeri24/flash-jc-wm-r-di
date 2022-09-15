package io.eclectics.cargilldigital.data.network

import okhttp3.ResponseBody

sealed class ResourceNetwork<out T> {
    data class Success<out T>(val value: T) : ResourceNetwork<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?,
        val errorString: String?
    ) : ResourceNetwork<Nothing>()

    object Loading : ResourceNetwork<Nothing>()
}

