package com.ekenya.rnd.onboarding.dataonboarding.api

import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface  ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserData>
    @POST("MobileWebService")
    suspend fun registerUser(@Body user: MainDataObject?): RegistrationResponse2
    @POST("MobileWebService")
    suspend fun ChangePassword(@Body user: MainDataObject?): Call<RegistrationResponse2?>?
    @POST("MobileWebService")
    suspend fun login(@Body user: MainDataObject?): Call<RegistrationResponse2?>?
    @POST("MobileWebService")
    suspend fun getWalletBalance(@Body user: MainDataObject?): Call<RegistrationResponse2?>?
}