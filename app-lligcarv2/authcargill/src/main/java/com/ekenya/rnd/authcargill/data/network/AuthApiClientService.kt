package com.ekenya.rnd.authcargill.data.network

import com.ekenya.rnd.authcargill.data.responses.AccountIdLookupMobileResponse
import com.ekenya.rnd.authcargill.data.responses.AccountMobileLookUpResponse
import com.ekenya.rnd.authcargill.data.responses.SetPinResponse
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


//@todo retrofit interface, where to define all our fun

interface AuthApiClientService {
//todo MobileLookUp TO SYSTEM


    @POST("auth/accountlookupmobile")
    suspend fun mobileLookUp(
        @Body requestBody: RequestBody
    ): AccountMobileLookUpResponse

    //Todo  cooparativeIdLookUp TO SYSTEM


    @POST("auth/accountidlookupmobile")
    suspend fun cooparativeIdLookUp2(
        @Body requestBody: RequestBody
    ): AccountIdLookupMobileResponse

    @POST("auth/accountidlookupmobile")
    suspend fun cooparativeIdLookUp3(
        @Body requestBody: RequestBody
    ): ResponseBody


    @POST("auth/accountidlookupmobile")
    suspend fun cooparativeIdLookUp(
        @Body requestBody: RequestBody
    ): Response<AccountIdLookupMobileResponse>

// cooparativeIdLookUp TO SYSTEM


    @POST("auth/createpin")
    suspend fun setNewAccountPin(
        @Body requestBody: RequestBody
    ): Response<SetPinResponse>

// verifyOtpForAccountSetUp TO SYSTEM


    @POST("auth/verifyotp")
    suspend fun verifyOtpForAccountSetUp(
        @Body requestBody: RequestBody
    ): Response<AccountIdLookupMobileResponse>


// LOGIN TO SYSTEM


    @POST("auth/mobileapplogin")
    suspend fun login(
        @Body requestBody: RequestBody
    ): CargillUserLoginResponse


}
