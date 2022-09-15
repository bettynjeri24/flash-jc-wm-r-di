package io.eclectics.cargilldigital.data.network


import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.data.responses.authresponses.AccountIdLookupMobileResponse
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponse
import io.eclectics.cargilldigital.data.responses.ffresponses.PaymentTransactionsResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST


//@todo retrofit interface, where to define all our fun

interface ApiClientService {

// LOGIN TO SYSTEM


    @POST("auth/mobileapplogin")
    suspend fun login(
        @Body requestBody: RequestBody
    ): CargillUserLoginResponse


    @POST("auth/paymentTransactions")
    suspend fun paymentTransactions(
        @Body requestBody: RequestBody
    ): PaymentTransactionsResponse


    @POST("auth/paymentTransactions")
    suspend fun paymentTransactions(
        @Body farmForceData: FarmForceData
    ): PaymentTransactionsResponse

    /**
     * Account lookup with Cooparative ID and Phone Number
     */
    @POST("auth/accountidlookupmobile")
    suspend fun cooparativeIdLookUp(
        @Body requestBody: RequestBody
    ): AccountIdLookupMobileResponse


}
