package com.ekenya.rnd.dashboard.datadashboard.api

import com.ekenya.rnd.common.data.model.*
import com.ekenya.rnd.dashboard.datadashboard.model.*
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService2 {
    @POST("account_lookup")
    suspend fun checkIfUserIsRegistered( @Body phone_number: AccountLookUpPayload?): AccountLookUpResponse
 @POST("bpc_account_lookup")
    suspend fun doBpcMetreNumberLookUp( @Body bpc_account: MetreNumber?):MetreNoLookupResponse

    @POST("get_stats")
    suspend fun getStatistics( @Body data: StatisticPayload):StatisticResponse

    @POST("verify_otp")
    suspend fun verifyDefaultPin( @Body body: VerifyDefaultPinPayload?): AccountLookUpResponse
    @POST("resend_token")
    suspend fun resendDeviceToken( @Body phone_number: AccountLookUpPayload?): AccountLookUpResponse
    @POST("device_lookup")
    suspend fun doDeviceLookUp( @Body data: DeviceLookUpPayload?): AccountLookUpResponse
    @POST("verify_device")
    suspend fun verifyDeviceOtp( @Body data: VerifyDevicePayload?): AccountLookUpResponse
    @POST("verify_device")
    suspend fun verifyDeviceOtpLookUP( @Body data: VerifyDevicePayload?): AccountLookUpResponse
    @POST("traffic_fines")
    suspend fun doTrafficChargesLookup(@Body data: TrafficFinesChargesLookupReq): TrafficChargesLookupResponse
    @POST("traffic_fines")
    suspend fun doTrafficFinesLookup(@Body data: TrafficFinesLookupReq): TrafficLookupResponse
    @POST("traffic_fines")
    suspend fun doSubmitTrafficCharges(@Body data:SubmitTrafficFineReq): TrafficLookupResponse



    /*@POST("customer_registration")
    suspend fun registerUser(@Body user: MainDataObject?): RegistrationResponse2 */

    //multipart endpoint
    @Multipart
    @POST("customer_registration_update")
    suspend fun registerUser(@Part("data") data: UserData?,
                             @Part files: List<MultipartBody.Part?>
    ): RegistrationResponse2





}