package com.ekenya.rnd.dashboard.datadashboard.api

import com.ekenya.rnd.common.data.model.BuyAirtimeReqWrapper
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.dashboard.datadashboard.model.*
import com.ekenya.rnd.onboarding.dataonboarding.model.RegistrationResponse2
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceDashBoard {

    @POST("MobileWebService")
    suspend fun login(@Body data: MainDataObject?): LoginResponse

    @POST("mobile/register-customer")
    fun registerUserReq(@Body data: RegisterUserReq): Call<RegisterUserResp>

    @POST("mobile/account-lookup")
    fun phoneNumberLookupReq(@Body data: PhoneNumberLookupReq): Call<RegisterUserResp>

    @POST("mobile/authenticate")
    fun loginUserReq(@Body data: LoginUserReq): Call<LoginUserResp>

    @POST("transactions/wallet-balance")
    fun walletBalanceReq(@Body data: LoginUserReq): Call<WalletBalanceResp>

    @POST("MobileWebService")
    suspend fun getWalletBalance(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): AccountBalanceResponse

    @POST("MobileWebService")
    suspend fun getMiniStatement(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): MiniStateMentResponse

    @POST("MobileWebService")
    suspend fun checkIfUserIsRegistered(@Body data: MainDataObject?): MiniStateMentResponse

    @POST("MobileWebService")
    suspend fun topUpWallet(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): TopUpWalletResponse

    @POST("MobileWebService")
    suspend fun confirmUserRegistration(@Body data: MainDataObject?): LoginResponse

    @POST("MobileWebService")
    suspend fun buyAirtime(
        @Header("Authorization") value: String,
        @Body data: BuyAirtimeReqWrapper?
    ): LoginResponse

    @POST("MobileWebService")
    suspend fun payBotswanaPower(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): LoginResponse

    @POST("MobileWebService")
    suspend fun getSavingsAccounts(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): LoginResponse

    @POST("MobileWebService")
    suspend fun scanMerchantQRCode(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): LoginResponse

    @POST("MobileWebService")
    suspend fun getFullStatementViaEmail(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): LoginResponse

    @POST("MobileWebService")
    suspend fun withDrawMoneytoMobileMoney(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): LoginResponse

    @POST("MobileWebService")
    suspend fun doAccountLookUP(
        @Header("Authorization") value: String,
        @Body data: MainDataObject?
    ): TopUpWalletResponse

    @Multipart
    @POST("MobileWebService")
    suspend fun fileUpload(
        @Part("data") data: UserData?,
        @Part files: MultipartBody.Part?
    ): RegistrationResponse2
}
