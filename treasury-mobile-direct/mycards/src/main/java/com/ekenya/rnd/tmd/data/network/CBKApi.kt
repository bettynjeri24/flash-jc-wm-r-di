package com.ekenya.rnd.tmd.data.network

import android.util.Base64
import com.ekenya.rnd.tmd.data.network.request.*
import com.ekenya.rnd.tmd.data.network.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

private var CREDENTIALS: String = "cbk_clientid" + ":" + "Y2hhbWFzZWNyZXQ="

val BASIC = "Basic " + Base64.encodeToString(CREDENTIALS.toByteArray(), Base64.NO_WRAP)
interface CBKApi {

    @Multipart
    @POST("cbk-bond/api/v1/mobile/user/new")
    suspend fun registerUser(
        @Part("body") newUserRequest: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Any>

    @POST("cbk-bond/api/v1/mobile/user/verify-otp")
    suspend fun verifyOtp(
        @Body otpRequest: OtpRequest
    ): OtpResponse

    @Multipart
    @POST("cbk-bond/oauth/token")
    suspend fun login(
        @Header("Authorization") authorization: String = BASIC,
        @Part("grant_type") grant_type: RequestBody,
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody
    ): Response<LoginResponse>

    @POST("cbk-bond/api/v1/mobile/user/info")
    suspend fun loopUp(
        @Body lookUpRequest: LookUpRequest
    ): LookUpResponse

    @POST("cbk-bond/api/v1/bonds/all")
    suspend fun getbondsAndBills(
        @Header("Authorization") token: String,
        @Body bondsRequest: BondsRequest
    ): BillAndBondsListResponse

    @POST("cbk-bond/api/v1/bond/bid")
    suspend fun bidRequest(
        @Header("Authorization") token: String,
        @Body bidRequestRequest: BidRequestRequest
    ): BidRequestResponse

    @POST("cbk-bond/api/v1/bond/transactions")
    suspend fun getTransActions(
        @Header("Authorization") token: String,
        @Body transactionsRequest: TransactionsRequest
    ): TransactionsResponse

    @POST("cbk-bond/api/v1/bond/settle")
    suspend fun settleBond(
        @Body settleRequest: SettleRequest
    )
}
