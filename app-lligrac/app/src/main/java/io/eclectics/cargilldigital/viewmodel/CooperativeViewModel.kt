package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.data.repository.*
import io.eclectics.cargilldigital.ui.buyerprofile.fundrequest.RoomdbFR
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class CooperativeViewModel @Inject constructor():ViewModel(){
    var coopBuyerListLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    //RoomdbFR.getOfflineFarmers()
    var buyerFundsRequestLiveData:MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()


    /**
     * get funds request
     */
    suspend fun getBuyerFundsRequest(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        buyerFundsRequestLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = CooperativeProfilerepository(RestClient.apiService).coopBuyerListReq(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> RoomdbFR.getBuyerFundsRequest(
                buyerFundsRequestLiveData,
                activity
            )//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> buyerFundsRequestLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                buyerFundsRequestLiveData,
                results.value
            )
        }
        return  buyerFundsRequestLiveData
    }

    /**
     * Remove beneficiary account
     */
    suspend fun getCoopBuyerlist(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        coopBuyerListLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = CooperativeProfilerepository(RestClient.apiService).coopBuyerListReq(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> getOfflineBuyerList(
                coopBuyerListLiveData,
                activity
            )//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> coopBuyerListLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                coopBuyerListLiveData,
                results.value
            )
        }
        return  coopBuyerListLiveData
    }

    suspend fun getOfflineBuyerList(coopBuyerListLiveData: MediatorLiveData<ViewModelWrapper<String>>, activity: FragmentActivity) {
        var listToBEObserved = CooperativeRoomRepository(RestClient.apiService).getBuyersList(activity)
        if (!listToBEObserved.hasActiveObservers()) {
            listToBEObserved.observe(activity, Observer {
                if (it.isNotEmpty()) {
                    // if (listToBEObserved.value!! == it)
                    LoggerHelper.loggerError("offlineCoolersts", "offline produsts with data ")
                    var generalResponce = NetworkUtility.getJsonParser().toJson(it)
                    coopBuyerListLiveData.value = ViewModelWrapper.response(generalResponce)
                    coopBuyerListLiveData.removeObservers(activity)
                    listToBEObserved.removeObservers(activity)


                } else {
                    LoggerHelper.loggerError("offlineFarmers", "offline farmers empty ")
                    coopBuyerListLiveData.value =
                        ViewModelWrapper.error("No Internet connection check your network and try again")
                }
            })
            // }
        }
    }
}