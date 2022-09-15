package io.eclectics.cargilldigital.data.repository.baserepo


import io.eclectics.cargilldigital.data.network.ResourceNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

interface BaseRepository {
    /**
     * Wrapping network routing using
     * This help to avoid handling failure on the UI
     * Also handle how responses will be handle after success or failure
     */
    suspend fun <T : Any> apiRequestByResponse(api: suspend (() -> Response<T>)): T {
        val response = api.invoke()
        if (response.isSuccessful) {
            Timber.d("BASE REPOSITORY----->>>>>>>>>>>>>>>>>> ${response.body()}")
            return response.body()!!
        } else {
            val error = response.errorBody()!!.string()
            val message = StringBuilder()
            Timber.d("BASE REPOSITORY----->>>>>>>>>>>>>>>>>> $error")
            error.let {
                try {
                    message.append(JSONObject(it).getString("responseMessage"))
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            message.append("Please contact Eclectics to report incidence :${response.code()}")
            //throw ApiExceptions("SERVICE UNDER MAINTENANCE")
            throw ApiExceptions(message.toString())
        }
//        throw ApiExceptions("SERVICE UNDER MAINTENANCE")
    }

    /**
     * Wrapping network routing using
     */
    suspend fun <T> apiRequestByResource(api: suspend () -> T): ResourceNetwork<T> {
        return withContext(Dispatchers.IO) {
            try {
                ResourceNetwork.Success(api.invoke())
            } catch (throwable: Throwable) {
                Timber.e("HttpException  throwable.message() ${throwable.message}")
                if (throwable is HttpException) {

                    Timber.e("HttpException  throwable.response() ${throwable.response().toString()}")

                    //---------------------------------------------------------------------

                    val error = throwable.response()?.errorBody()!!.string()
                    val message = StringBuilder()

                    error.let {
                        try {
                            message.append(JSONObject(it).getString("error_description"))
                        } catch (e: JSONException) {
                        }
                        message.append("\n")
                    }

                    //Timber.e("BASE REPOSITORY----->>>>>>>>>>>>>>>>>> $message")
                    //---------------------------------------------------------------------
                    ResourceNetwork.Failure(
                        false,
                        throwable.code(),
                        throwable.response()?.errorBody(),
                        error
                    )
                } else {
                    Timber.d("HttpException ${throwable.message.toString()}")
                    Timber.d("HttpException ${throwable.cause}")
                    Timber.d("HttpException ${throwable}")
                    ResourceNetwork.Failure(true, null, null, "NO NETWORK FOUND")
                }
            }

        }

    }

}

class ApiExceptions(message:String): IOException(message)