package com.ekenya.rnd.tmd.data.repository

import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.tmd.data.network.CBKApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class LoginRepository @Inject constructor(
    val cbkApi: CBKApi
) {

    fun login(
        userName: String,
        passWord: String
    ) = flow {
        try {
            emit(Resource.Loading())

            val grantType = "password".toRequestBody(MultipartBody.FORM)
            val username = userName.toRequestBody(MultipartBody.FORM)
            val password = passWord.toRequestBody(MultipartBody.FORM)

            val response = cbkApi.login(
                grant_type = grantType,
                username = username,
                password = password
            )
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()))
            } else {
                emit(Resource.Error(Throwable(response.errorBody().toString())))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}
