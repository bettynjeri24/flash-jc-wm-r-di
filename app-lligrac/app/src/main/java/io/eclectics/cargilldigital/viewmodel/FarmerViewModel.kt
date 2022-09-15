package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.data.repository.FarmerProfileRepository
import io.eclectics.cargilldigital.data.repository.FarmerRoomRepository
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class FarmerViewModel @Inject constructor(): ViewModel(){
    var fetchAccChannelLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerAddBeneficiaryAccLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerGetBenefiaryAccLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerGetChannelListLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerRemoveBeneficiaryAccLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerWalletBalanceLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()

    /**
     * Remove beneficiary account
     */
    suspend fun getFarmerWalletBalance(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        farmerRemoveBeneficiaryAccLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = FarmerProfileRepository(RestClient.apiService).addBeneficiaryRequest(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> getOfflineBenefiaryAccList(
                farmerRemoveBeneficiaryAccLiveData,
                activity
            )//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> farmerRemoveBeneficiaryAccLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                farmerRemoveBeneficiaryAccLiveData,
                results.value
            )
        }
        return  farmerRemoveBeneficiaryAccLiveData
    }

    //farmerGeneralGetRequest
    /**
     * Get added beneficiary acc list
     */
    suspend fun getAddedBenefiaryAccount(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        farmerGetBenefiaryAccLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = FarmerProfileRepository(RestClient.apiService).farmerGeneralRequest(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> getOfflineBenefiaryAccList(
                farmerGetBenefiaryAccLiveData,
                activity
            )//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> farmerGetBenefiaryAccLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                farmerGetBenefiaryAccLiveData,
                results.value
            )
        }
        return  farmerGetBenefiaryAccLiveData
    }
    /**
     * submit get channel list data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun getFarmerChannellist(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        farmerAddBeneficiaryAccLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = FarmerProfileRepository(RestClient.apiService).farmerGeneralRequest(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> getOfflineChannelList(
                fetchAccChannelLiveData,
                activity
            )//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> fetchAccChannelLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                fetchAccChannelLiveData,
                results.value
            )
        }
        return  fetchAccChannelLiveData
    }





    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun addBeneficiaryAccReq(
        json: JSONObject,
        endPoint: String,
        requireActivity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        farmerAddBeneficiaryAccLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = FarmerProfileRepository(RestClient.apiService).addBeneficiaryRequest(json,
            endPoint)
        return RequestResponse.extractReqResponseLivedataMsg(
            json,
            endPoint,
            farmerAddBeneficiaryAccLiveData,
            results,
            requireActivity
        )
    }
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun farmerGeneralRequest(
        json: JSONObject,
        endPoint: String,
        requireActivity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        farmerAddBeneficiaryAccLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = FarmerProfileRepository(RestClient.apiService).farmerGeneralRequest(json,
            endPoint)
        return RequestResponse.extractReqResponseLivedataMsg(
            json,
            endPoint,
            farmerAddBeneficiaryAccLiveData,
            results,
            requireActivity
        )
    }

    suspend fun getOfflineChannelList(buyerFarmerListLiveData: MediatorLiveData<ViewModelWrapper<String>>,
                                  activity: FragmentActivity){//:MediatorLiveData<ViewModelWrapper<String>>
        //withContext(Dispatchers.IO) {
        var listToBEObserved = FarmerRoomRepository(RestClient.apiService).getChannelList(activity)
        if (!listToBEObserved.hasActiveObservers()) {
            listToBEObserved.observe(activity, Observer {
                if (it.isNotEmpty()) {
                    // if (listToBEObserved.value!! == it)
                    LoggerHelper.loggerError("offlinechannel", "offline channel list")
                    var generalResponce = NetworkUtility.getJsonParser().toJson(it)
                    buyerFarmerListLiveData.value = ViewModelWrapper.response(generalResponce)
                    buyerFarmerListLiveData.removeObservers(activity)
                    listToBEObserved.removeObservers(activity)


                } else {
                    LoggerHelper.loggerError("offlineFarmers", "offline farmers empty ")
                    buyerFarmerListLiveData.value =
                        ViewModelWrapper.error("No Internet connection check your network and try again")
                }
            })
            // }
        }
        //return stockRefLivedata
    }


    suspend fun getOfflineBenefiaryAccList(buyerFarmerListLiveData: MediatorLiveData<ViewModelWrapper<String>>,
                                      activity: FragmentActivity){//:MediatorLiveData<ViewModelWrapper<String>>
        //withContext(Dispatchers.IO) {
        var listToBEObserved = FarmerRoomRepository(RestClient.apiService).getBeneficiaryAccList(activity)
        if (!listToBEObserved.hasActiveObservers()) {
            listToBEObserved.observe(activity, Observer {
                if (it.isNotEmpty()) {
                    // if (listToBEObserved.value!! == it)
                    LoggerHelper.loggerError("offlineAccount", "offline benefiary acc list")
                    var generalResponce = NetworkUtility.getJsonParser().toJson(it)
                    buyerFarmerListLiveData.value = ViewModelWrapper.response(generalResponce)
                    buyerFarmerListLiveData.removeObservers(activity)
                    listToBEObserved.removeObservers(activity)


                } else {
                    LoggerHelper.loggerError("offlineFarmers", "offline farmers empty ")
                    buyerFarmerListLiveData.value =
                        ViewModelWrapper.error("No Accessible data connect to internet network and try again")
                }
            })
            // }
        }
        //return stockRefLivedata
    }

}
