package com.ekenya.rnd.tmd.data.repository

import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.tmd.data.network.CBKApi
import com.ekenya.rnd.tmd.data.network.request.NewUserRequest
import com.ekenya.rnd.tmd.data.network.request.OtpRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class RegisterUserRepo @Inject constructor(
    private val cbkApi: CBKApi
) {

    fun sentOtp(otpRequest: OtpRequest) = flow {
        try {
            emit(Resource.Loading(null))
            val response = cbkApi.verifyOtp(otpRequest)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e, null))
        }
    }

    fun registerUser(userRequest: NewUserRequest, files: List<File>) = flow {
        try {
            emit(Resource.Loading())
            val userRequestBody: RequestBody = userRequest.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val multiParts = files.map { file ->
                MultipartBody.Part
                    .createFormData(
                        name = "files",
                        filename = file.name,
                        body = file.asRequestBody()
                    )
            }
            delay(3000)
            val response = cbkApi.registerUser(userRequestBody, multiParts)
            if (response.isSuccessful) {
                emit(Resource.Success("Success"))
            } else {
                emit(Resource.Error(Throwable(response.errorBody().toString())))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e, null))
        }
    }.flowOn(Dispatchers.IO)
}
