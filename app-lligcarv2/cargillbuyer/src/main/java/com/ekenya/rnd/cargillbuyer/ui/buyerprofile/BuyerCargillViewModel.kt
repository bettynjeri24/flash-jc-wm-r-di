package com.ekenya.rnd.cargillbuyer.ui.buyerprofile

import android.app.Application
import androidx.lifecycle.asLiveData
import com.ekenya.rnd.cargillbuyer.data.repository.BuyerRepository
import com.ekenya.rnd.cargillbuyer.data.responses.*
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.utils.base.viewmodel.BaseViewModel
import com.ekenya.rnd.common.utils.custom.getDeviceId
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class BuyerCargillViewModel @Inject constructor(
    private val repository: BuyerRepository,
    private val app: Application
) : BaseViewModel(app) {

    //
    fun getUserVmRoom() = repository.getUserInRoom().asLiveData()

    /**
     * FarmerBalanceInquiryResponse
     */

    suspend fun requestFarmerBalanceInquiryResponse(requestBody: RequestBody): BuyerBalanceInquiryResponse {
        return repository.balanceInquiry(requestBody)
    }

    /**
     * requestFfPendingPayments
     */
    suspend fun requestFfPendingPayments(jsonObject: JSONObject): FfPendingPaymentsResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.ffPendingPayments(requestBody)
    }

    /**
     * requestlatestTransactions
     */
    suspend fun requestlatestTransactions(jsonObject: JSONObject): LatestTransactionsResponse {
        jsonObject.put("userId", app.getDeviceId())
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.latestTransactions(requestBody)
    }

    /**
     * requestlatestTransactions
     */
    suspend fun requestffPayments(jsonObject: JSONObject): GeneralResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.ffPayments(requestBody)
    }

    /**
     * requestlatestTransactions
     */
    suspend fun requestbuyertopuprequests(jsonObject: JSONObject): BuyerTopUpRequestsResponse {
        jsonObject.put("userId", app.getDeviceId())
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.buyertopuprequests(requestBody)
    }

    /**
     * requestlatestTransactions
     */
    suspend fun requestGetFarmersList(jsonObject: JSONObject): FarmerDetailsResponse {
        jsonObject.put("userId", app.getDeviceId())
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.getfarmerslist(requestBody)
    }

    /**
     * cocoapurchase
     */
    suspend fun requestFarmerPurchase(jsonObject: JSONObject): BuyerPurchaseResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.requestFarmerPurchase(requestBody)
    }

    /**
     * cocoapurchase
     */
    suspend fun requestFundsTopUp(jsonObject: JSONObject): BuyerPurchaseResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.requestFundsTopUp(requestBody)
    }
}
