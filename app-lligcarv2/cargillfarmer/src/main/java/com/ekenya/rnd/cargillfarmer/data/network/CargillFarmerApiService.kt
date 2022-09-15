package com.ekenya.rnd.cargillfarmer.data.network

import com.ekenya.rnd.cargillfarmer.data.responses.*
import com.ekenya.rnd.cargillfarmer.data.responses.farmer.FarmerBalanceInquiryResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


/**retrofit interface, where to define all our fun*/
interface CargillFarmerApiService {

    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/balanceinquiry")
    suspend fun balanceInquiry(
        @Body requestBody: RequestBody
    ): Response<FarmerBalanceInquiryResponse>

    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/latestTransactions")
    suspend fun latestTransactions(
        @Body requestBody: RequestBody
    ): Response<FarmerLatestTransactionResponse>

    /**
     * mycashoutchannels
     */
    @POST("apigateway/mobileapp/mycashoutchannels")
    suspend fun myCashOutChannels(
        @Body requestBody: RequestBody
    ): Response<MyCashOutChannelsResponse>

    @POST("apigateway/mobileapp/mycashoutchannels")
    suspend fun myCashOutChannels2(
        @Body requestBody: RequestBody
    ): Response<MyCashOutChannelsResponse>

    /**
     * farmercashout
     */
    @POST("apigateway/mobileapp/farmercashout")
    suspend fun farmercashout(
        @Body requestBody: RequestBody
    ): Response<FarmerCashoutResponse>

    /**
     * AddBeneficiaryAccount
     */
    @POST("apigateway/mobileapp/cashoutchannelrequest")
    suspend fun addBeneficiaryAccount(
        @Body requestBody: RequestBody
    ):  Response<AddBeneficiaryAccountResponse>

    /**
     * Remove AccountData
     */
    @POST("apigateway/mobileapp/removebeneficiary")
    suspend fun removeBeneficiary(
        @Body requestBody: RequestBody
    ): Response<RemoveAccountResponse>

    /**
     * Remove AccountData
     */
    @POST("apigateway/mobileapp/verifychannel")
    suspend fun verifyBeneficiaryAccount(
        @Body requestBody: RequestBody
    ): Response<VerifyAddAccountResponse>


}
