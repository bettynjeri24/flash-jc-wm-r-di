package com.ekenya.lamparam.repository

import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.LamparamApi
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

/**
 * Used by all fragments in OnboardingActivity
 */
class OnboardingRepository @Inject constructor(private val api: LamparamApi) {

    /**
     * @return response from api
     */
    suspend fun sendRequest(requestBody: JSONObject): Response<DefaultResponse> {
        return api.sendRequest(requestBody.toString())
    }

    /**
     * @return response from api
     */
    suspend fun appToken(username: String, password: String): Response<DefaultResponse> {
        return api.appToken(username, password)
    }

}