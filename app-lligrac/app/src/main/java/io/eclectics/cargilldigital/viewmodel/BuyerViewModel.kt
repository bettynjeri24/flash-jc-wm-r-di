package io.eclectics.cargilldigital.viewmodel

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.data.repository.BuyerProfileRepository
import io.eclectics.cargilldigital.data.repository.BuyerRoomRepository
import io.eclectics.cargilldigital.data.repository.CooperativeProfilerepository
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class BuyerViewModel @Inject constructor(): ViewModel(){
    var buyerFundsRequestLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var buyerFarmerListLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerPendingPaymentLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()

    suspend fun getFarmerPendingPayment(
        json: JSONObject,
        endPoint: String,
        activity: FragmentActivity
    ): MediatorLiveData<ViewModelWrapper<String>> {
        farmerPendingPaymentLiveData = MediatorLiveData()
        var json = ApiEndpointObj.updateRequest(json)
        val results = CooperativeProfilerepository(RestClient.apiService).coopBuyerListReq(
            json,
            endPoint
        )
        when (results) {
            is ResultWrapper.NetworkError -> pendingpaymentList(
                farmerPendingPaymentLiveData,
                activity
            )//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> farmerPendingPaymentLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                farmerPendingPaymentLiveData,
                results.value
            )
        }
        return  farmerPendingPaymentLiveData
    }
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun getFarmersList(json: JSONObject, endPoint:String,activity: FragmentActivity): MediatorLiveData<ViewModelWrapper<String>> {
        buyerFarmerListLiveData = MediatorLiveData()
        //var json = ApiEndpointObj.updateRequest(json)
        val wrapperResult = BuyerProfileRepository(RestClient.apiService).buyerFundsRequest(json,
            endPoint)
       // return RequestResponse.extractBuyerReqResponseLivedataMsg(endPoint,buyerFarmerListLiveData, results)
       // respLiveData:MediatorLiveData<ViewModelWrapper<String>>, wrapperResult: ResultWrapper<GeneralResponce>):MediatorLiveData<ViewModelWrapper<String>>{
            when(wrapperResult){
                is ResultWrapper.NetworkError -> getOfflineFarmers(buyerFarmerListLiveData,activity)//SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
                is ResultWrapper.GenericError -> buyerFarmerListLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
                is ResultWrapper.Success -> RequestResponse.respDataMessage(
                    buyerFarmerListLiveData,
                    wrapperResult.value
                )
            }

        return buyerFarmerListLiveData
    }

    /*rivate fun getOfflineFarmers(
        buyerFarmerListLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        activity: Activity
    ) {

    }*/
    suspend fun getOfflineFarmers(buyerFarmerListLiveData: MediatorLiveData<ViewModelWrapper<String>>,
                                  activity: FragmentActivity){//:MediatorLiveData<ViewModelWrapper<String>>
        //withContext(Dispatchers.IO) {
        var listToBEObserved = BuyerRoomRepository(RestClient.apiService).getFarmerList(activity)
        if (!listToBEObserved.hasActiveObservers()) {
            listToBEObserved.observe(activity, Observer {
                if (it.isNotEmpty()) {
                    // if (listToBEObserved.value!! == it)
                    LoggerHelper.loggerError("offlineCoolersts", "offline produsts with data ")
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
/*
get pending payments
 */
suspend fun pendingpaymentList(buyerFarmerListLiveData: MediatorLiveData<ViewModelWrapper<String>>,
                              activity: FragmentActivity) {//:MediatorLiveData<ViewModelWrapper<String>>
    //withContext(Dispatchers.IO) {
    var listToBEObserved =
        BuyerRoomRepository(RestClient.apiService).getPendingPaymentRequest(activity)
    if (!listToBEObserved.hasActiveObservers()) {
        listToBEObserved.observe(activity, Observer {
            if (it.isNotEmpty()) {
                // if (listToBEObserved.value!! == it)
                LoggerHelper.loggerError("offlineCoolersts", "offline produsts with data ")
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
}



    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun sendFundsRequest(json: JSONObject, endPoint:String,activity: Activity): MediatorLiveData<ViewModelWrapper<String>> {
        buyerFundsRequestLiveData = MediatorLiveData()
        //var json = ApiEndpointObj.updateRequest(json)
        val results = BuyerProfileRepository(RestClient.apiService).buyerFundsRequest(json,
            endPoint)
        return RequestResponse.extractBuyerReqResponseLivedataMsg(endPoint,buyerFundsRequestLiveData, results)
    }

}