package com.ekenya.lamparam.networkCallback.network

import co.ekenya.pepsidistributor.networkCallback.respcallback.GeneralResponce
import com.ekenya.lamparam.model.CompleteTransactionReq
import com.ekenya.lamparam.model.FundsTransferReceiveMoneyReq
import com.ekenya.lamparam.model.RemittanceTransactionLookupReq
import com.ekenya.lamparam.model.response.ConfirmTransactionResponse
import com.ekenya.lamparam.model.response.RemittanceTransactionLookupResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface Webservice {

    //General Request
    @Headers("x-message-type: 0")
    @POST("MobileWebService")
    suspend fun generalRequest(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    //General Request
    @Headers("x-message-type: 0")
    @POST("CGB-sms-notification")
    suspend fun pushSMSRequest(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): GeneralResponce

    @POST("transactions/transaction-lookup")
    fun transactionLookupRemittance(@Body data:RemittanceTransactionLookupReq): Call<RemittanceTransactionLookupResponse>

    @POST("transactions/confirm-transaction")
    fun completeTransReq(@Body data: CompleteTransactionReq): Call<ConfirmTransactionResponse>

    @POST("transactions/funds-transfer")
    fun doFundsTransferReq(@Body data: FundsTransferReceiveMoneyReq): Call<ConfirmTransactionResponse>
}


