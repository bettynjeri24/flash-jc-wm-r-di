package com.ekenya.rnd.cargillbuyer.data.network

import com.ekenya.rnd.cargillbuyer.data.responses.*
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**retrofit interface, where to define all our fun*/
interface CargillBuyerApiService {
    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/balanceinquiry")
    suspend fun balanceInquiry(
        @Body requestBody: RequestBody
    ): Response<BuyerBalanceInquiryResponse>

    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/ffPendingPayments")
    suspend fun ffPendingPayments(
        @Body requestBody: RequestBody
    ): Response<FfPendingPaymentsResponse>

    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/latestTransactions")
    suspend fun latestTransactions(
        @Body requestBody: RequestBody
    ): Response<LatestTransactionsResponse>

    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/ffPayments")
    suspend fun ffPayments(
        @Body requestBody: RequestBody
    ): Response<GeneralResponse>

    /**
     * BALANCE INQUIRY
     */
    @POST("apigateway/mobileapp/buyertopuprequests")
    suspend fun buyertopuprequests(
        @Body requestBody: RequestBody
    ): Response<BuyerTopUpRequestsResponse>

    /**
     * getfarmerslist
     */
    @POST("apigateway/mobileapp/getfarmerslist")
    suspend fun getfarmerslist(
        @Body requestBody: RequestBody
    ): Response<FarmerDetailsResponse>

    /**
     * getfarmerslist
     */
    @POST("apigateway/mobileapp/cocoapurchase")
    suspend fun requestFarmerPurchase(
        @Body requestBody: RequestBody
    ): Response<BuyerPurchaseResponse>

    /**
     * getfarmerslist
     */
    @POST("apigateway/mobileapp/fundsrequest")
    suspend fun requestFundsTopUp(
        @Body requestBody: RequestBody
    ): Response<BuyerPurchaseResponse>
}
