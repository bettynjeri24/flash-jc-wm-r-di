package com.ekenya.lamparam.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.ekenya.pepsidistributor.networkCallback.respcallback.GeneralResponce
import co.ekenya.pepsidistributor.responsewrapper.ResultWrapper
import com.ekenya.lamparam.networkCallback.network.RestClient
import com.ekenya.lamparam.repository.MainRepositoryOld
import com.ekenya.lamparam.utilities.LoggerHelper
import com.ekenya.lamparam.utilities.NetworkUtility
import com.ekenya.kcbpocr.viewModel.ViewModelWrapper
import com.ekenya.lamparam.model.CompleteTransactionReq
import com.ekenya.lamparam.model.FundsTransferReceiveMoneyReq
import com.ekenya.lamparam.model.RemittanceTransactionLookupReq
import com.ekenya.lamparam.repository.MainRepository
import com.ekenya.lamparam.repository.MyApiResponse
import com.google.gson.JsonElement
import org.json.JSONObject

class LampMainViewModel : ViewModel() {

    var loginLivedata: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var registerLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var sendSMSLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    //
    var walletBalance = MutableLiveData<Double>()
    //
    var expectedAmount = MutableLiveData<Double>()
    //
    var senderIdNumber = MutableLiveData<String>()
    var senderName = MutableLiveData<String>()
    var senderPhoneNumber = MutableLiveData<String>()
    //
    var receiverPhoneNumber = MutableLiveData<String>()
    var receiverName = MutableLiveData<String>()
    var receiverIdNumber = MutableLiveData<String>()
    var purpose = MutableLiveData<String>()
    var regUserLiveData: LiveData<MyApiResponse>? = null


    suspend fun registerRequest(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        registerLiveData = MediatorLiveData()
        val results = MainRepositoryOld(RestClient.apiService).loginRequest(json)
        LoggerHelper.loggerSuccess("login",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> registerLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> registerLiveData.value = ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> results.value.data?.response?.let { returnData(registerLiveData, it) }//LoggerHelper.loggerSuccess("dataserver","data ${results.value.data.response.toString()}")//returnLoginData(registerLiveData,results.value)
        }
        //No Internet connection check your network and try again

        return registerLiveData
    }

    suspend fun sendSMSRequest(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        if (sendSMSLiveData == null){
            sendSMSLiveData = MediatorLiveData()
        }
        val results = MainRepositoryOld(RestClient.apiService).sendSMSRequest(json)
        LoggerHelper.loggerSuccess("sendSMS",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> sendSMSLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> sendSMSLiveData.value = ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> results.value.data?.response?.let { returnData(sendSMSLiveData, it) }//LoggerHelper.loggerSuccess("dataserver","data ${results.value.data.response.toString()}")//returnLoginData(registerLiveData,results.value)
        }
        //No Internet connection check your network and try again

        return sendSMSLiveData
    }

    private fun returnData(registerLiveData: MediatorLiveData<ViewModelWrapper<String>>, response: JsonElement) {
        LoggerHelper.loggerSuccess("response"," $response")
        var responseObj:GeneralResponce.ApiResponse= NetworkUtility.jsonResponse(response.toString())
        LoggerHelper.loggerSuccess("code","code ${responseObj.code}")
        when{
            responseObj.code.contentEquals("00")->{
                LoggerHelper.loggerSuccess("code","code ${responseObj.code}")
            }
            else -> {
                registerLiveData.value = ViewModelWrapper.error(responseObj.message)
            }
        }
    }
    val repo = MainRepositoryOld(RestClient.apiService)
    fun doTransactionLookup(req: RemittanceTransactionLookupReq): LiveData<MyApiResponse> {

        regUserLiveData = repo.doTransactionLookupReq(req)

        return regUserLiveData!!
    }

    fun doCompleteTransReq(req: CompleteTransactionReq): LiveData<MyApiResponse> {

            regUserLiveData = repo.doCompleteTransReq(req)

            return regUserLiveData!!
    }

    fun doFundsTransferReq(req: FundsTransferReceiveMoneyReq): LiveData<MyApiResponse> {

                regUserLiveData = repo.doFundsTransferReq(req)

                return regUserLiveData!!
    }

}