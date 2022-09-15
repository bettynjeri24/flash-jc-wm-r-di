package com.ekenya.rnd.onboarding.dataonboarding.Remote

import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse
import com.ekenya.rnd.onboarding.dataonboarding.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    //getotp
    @POST("registeredCustomers")
    fun registerUser(@Body user: User?): Call<RegistrationResponse?>?
}