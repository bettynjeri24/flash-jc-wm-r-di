package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.data.repository.BuyerRoomRepository
import io.eclectics.cargilldigital.data.repository.FarmerProfileRepository
import io.eclectics.cargilldigital.data.repository.GeneralRequestRepository
import io.eclectics.cargilldigital.network.RestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor() : ViewModel() {
    var phoneLookupLivedata: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var pinRequestLivedata: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var loginRequetsLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var recentTransactionLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var balanceEnquiryLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()

    /**
     * submit login data
     * make a ussd call if internet issue encountered
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun sendLoginRequest(
        json: JSONObject,
        endPoint: String,
        requireActivity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        loginRequetsLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = GeneralRequestRepository(RestClient.apiService).phonelookupRequest(
            json,
            endPoint
        )
        //TODO HANDLE THIS TRY CATCH IURGEMT
        return RequestResponse.loginResponseLiveData(
            json,
            endPoint,
            loginRequetsLiveData,
            results,
            requireActivity
        )
    }

    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun phoneLookupRequest(
        json: JSONObject,
        endPoint: String,
        requireActivity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        phoneLookupLivedata = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = GeneralRequestRepository(RestClient.apiService).phonelookupRequest(
            json,
            endPoint
        )
        return RequestResponse.extractReqResponseLivedataMsg(
            json,
            endPoint,
            phoneLookupLivedata,
            results,
            requireActivity
        )
    }

    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun generalPinRequest(
        json: JSONObject,
        endPoint: String,
        requireActivity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        pinRequestLivedata = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = GeneralRequestRepository(RestClient.apiService).generalPinRequest(
            json,
            endPoint
        )
        return RequestResponse.sendPinReqResponseLivedataMsg(
            json,
            endPoint,
            pinRequestLivedata,
            results,
            requireActivity
        )
    }


    /**
     * get wallte api balance
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun walletBalanceRequest(
        json: JSONObject,
        endPoint: String,
        requireActivity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        pinRequestLivedata = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = GeneralRequestRepository(RestClient.apiService).generalPinRequest(
            json,
            endPoint
        )
        return RequestResponse.sendWalletBILivedataMsg(
            json,
            endPoint,
            pinRequestLivedata,
            results,
            requireActivity
        )
    }


    /**
     * Remove beneficiary account
     */
    suspend fun getRecentTransactions(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        recentTransactionLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = FarmerProfileRepository(RestClient.apiService).addBeneficiaryRequest(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> recentTransactionLiveData.value =
                ViewModelWrapper.error("Error")//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> recentTransactionLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                recentTransactionLiveData,
                results.value
            )
        }
        return recentTransactionLiveData
    }


    /**
     * Save user loggin data to db
     */
    suspend fun insertLoginData(activity: FragmentActivity, json: String) {
        GlobalScope.launch(Dispatchers.IO) {
            BuyerRoomRepository(RestClient.apiService).saveLoginData(activity, json)
        }
    }
}
