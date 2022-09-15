package io.eclectics.cargill.network

import io.eclectics.cargill.network.networkCallback.GeneralResponce
import okhttp3.RequestBody
import retrofit2.http.*


interface Webservice {
    //Geneal profile request
    @GET
    suspend fun generalProfileGetReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun generalProfileReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    //farmer profile
    @GET
    suspend fun farmerProfileGetReq(
        @HeaderMap headers: Map<String, String>,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun farmerProfileReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun farmerAddAccReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce


    //Buyer profile
    //gerneral buyer profile
    @POST
    suspend fun generalBuyerReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun buyerTransactionReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun requestFunds(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    //Cooperative profile
    //gerneral coop profile
    @POST
    suspend fun generalCoopReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun coopTransactionReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce

    @POST
    suspend fun coopBuyerListReq(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce


    //General Request  @POST("crownBeverageMobile/")
    @POST("userlogin")
    suspend fun generalRequest(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    @POST("registerfarmer")
    suspend fun registerFarmer(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    @POST("buycocoa")
    suspend fun buycocoa(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    @GET("farmerlist")
    suspend fun getFarmerlist(@HeaderMap headers: Map<String, String>): GeneralResponce

    @GET("farmerwallet")
    suspend fun getFarmerWalletBalance(@HeaderMap headers: Map<String, String>): GeneralResponce

    @GET("farmertransactions")
    suspend fun getFarmerTransaction(@HeaderMap headers: Map<String, String>): GeneralResponce

    @POST("buyairtime")
    suspend fun buyAirtime(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    //agent request funds
    @POST("agentfloatrequest")
    suspend fun requestFunds(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    @GET("agentwallet")
    suspend fun getAgentWalletBal(@HeaderMap headers: Map<String, String>): GeneralResponce

    //test one webservice interface
    @POST
    suspend fun generalTestOnEnd(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody,
        @Url endpoint: String
    ): GeneralResponce
}
