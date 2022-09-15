package io.eclectics.cargill.network.responsewrapper

import android.util.Log
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

//class SafeCall {
    suspend fun <T> SafeCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        Log.e("responseCrEr",throwable.message()+"sm"+throwable.message)
                        val errorResponse = "http code $code error"
                        //val errorResponse = convertErrorBody(throwable)
                       // ResultWrapper.GenericError(code, ErrorResponse(code,errorResponse))
                        ResultWrapper.GenericError(code, errorResponse)
                    }
                    else -> {
                        //"${throwable.message}"
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }

  /*  private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }*/
//}