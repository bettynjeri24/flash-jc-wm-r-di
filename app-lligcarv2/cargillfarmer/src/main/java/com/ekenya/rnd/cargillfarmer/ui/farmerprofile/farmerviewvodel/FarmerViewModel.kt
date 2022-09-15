package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.farmerviewvodel
/*

import android.app.Application
import androidx.lifecycle.*
import com.ekenya.rnd.cargillfarmer.data.repository.FarmerRepository
import com.ekenya.rnd.cargillfarmer.data.responses.*
import com.ekenya.rnd.cargillfarmer.data.responses.farmer.FarmerBalanceInquiryResponse
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.utils.custom.Coroutines
import com.ekenya.rnd.common.utils.custom.deviceSessionUUID
import com.ekenya.rnd.common.utils.custom.getDeviceId
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class FarmerViewModel @Inject constructor(
    private val repository: FarmerRepository,
    private val app: Application
) : AndroidViewModel(app) {
// *********************************************************************************
    */
/**
 * MyCashOutChannelsLisFromRoom
 *//*

    fun requestMyCashOutChannelsLisFromRoom(jsonObject: JSONObject): Deferred<LiveData<List<MyCashOutChannelsData>>> {
        jsonObject.put(
            "deviceUUId",
            deviceSessionUUID()
        )
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        val myCashOutChannelsList by Coroutines.lazyDeferred {
            repository.getDataFromRoom(requestBody)
        }
        return myCashOutChannelsList
    }

    */
/**
 * FarmerBalanceInquiryResponse
 *//*

    suspend fun requestFarmerBalanceInquiryResponse(requestBody: RequestBody): FarmerBalanceInquiryResponse {
        return repository.balanceInquiry(requestBody)
    }
// *********************************************************************************

    */
/**
 * getLatestTransactions
 *//*

    suspend fun requestLatestTransactionsResponse(requestBody: RequestBody): FarmerLatestTransactionResponse {
        return repository.latestTransactions(requestBody)
    }

    // *********************************************************************************

    */
/**
 * getMyCashOutChannels
 *//*

    suspend fun requestMyCashOutChannelsData(requestBody: RequestBody): MyCashOutChannelsResponse {
        return repository.myCashOutChannels(requestBody)
    }
    // *********************************************************************************

    */
/**
 * GET FarmerCashout
 *//*

    suspend fun requestFarmerCashoutData(jsonObject: JSONObject): FarmerCashoutResponse {
        jsonObject.put(
            "deviceUUId",
            deviceSessionUUID()
        )
        jsonObject.put(
            "deviceId",
            app.getDeviceId()
        )
        jsonObject.put(
            "language",
            "fr"
        )
        Timber.e("==========jsonObject============$jsonObject")
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)

        return repository.farmercashout(requestBody)
    }
    // *********************************************************************************
    */
/**
 * Get AddBeneficiaryAccount
 *//*


    suspend fun requestAddBeneficiaryAccountData(jsonObject: JSONObject): AddBeneficiaryAccountResponse {
        jsonObject.put("bankName", "")
        jsonObject.put("cardNumber", "")
        jsonObject.put("expiryDate", "")
        jsonObject.put("cvc", "")
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.addBeneficiaryAccount(requestBody)
    }

    // ***************************Save To Room******************************************************
    suspend fun saveVmRoom(list: List<FarmerLatestTransactionData>) {
        */
/*withContext(Dispatchers.IO) {*//*

        return repository.saveInRoom(list)
        // }
    }

    // *************************Get To Room********************************************************
    fun getFromRoom() = repository.getRoomData().asLiveData()

    // *********************************************************************************
    */
/**
 * getRemoveBeneficiary
 *//*

    suspend fun requestRemoveBeneficiary(jsonObject: JSONObject): RemoveAccountResponse {
        jsonObject.put("deviceId", app.getDeviceId())
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.removeBeneficiary(requestBody)
    }

    // *********************************************************************************
    */
/**
 * getRemoveBeneficiary
 *//*

    suspend fun requestVerifyAddAccountData(jsonObject: JSONObject): VerifyAddAccountResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.verifyBeneficiaryAccount(requestBody)
    }

    // GET USERDATA
    fun getUserVmRoom() = repository.getUserInRoom().asLiveData()
}
*/
