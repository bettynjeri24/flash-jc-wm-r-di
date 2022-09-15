package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import io.eclectics.cargill.network.networkCallback.GeneralResponce
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import java.lang.Exception

object RequestResponse {

    /**
     * Login endpoint response
     * consume and check the current profile
     */
    fun loginResponseLiveData(
        json: JSONObject,
        endpoint: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        wrapperResult: ResultWrapper<GeneralResponce>,
        requireActivity: FragmentActivity
    ):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            //ViewModelWrapper.error(requireActivity.resources.getString(R.string.no_internet))
            is ResultWrapper.NetworkError ->SendUSSD.makeLoginCall(respLiveData,json,endpoint,requireActivity)//respLiveData.value =ViewModelWrapper.error(requireActivity.resources.getString(R.string.no_internet))//SendUSSD.makeLoginCall(respLiveData,json,endpoint,requireActivity)//  respLiveData.value=ViewModelWrapper.error(requireActivity.resources.getString(R.string.no_internet))//SendUSSD.makeLoginCall(respLiveData,json,endpoint,requireActivity)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }

    fun respData(mediatorData: MediatorLiveData<ViewModelWrapper<String>>, value: GeneralResponce) {
        var errorOccurred = "Error occurred, please try again "
        var responseMsg = ""
        try {
            lateinit var responseStatusMsg:String
            if(value.statusMsg.trim().isNotEmpty()){
                responseStatusMsg = value.statusMsg+" "
            }else{
                responseStatusMsg = NetworkUtility.emptyResponseMsg(value.statusCode.toString())
            }
            when {
                value.statusCode.toString().contentEquals("1") -> {
                    mediatorData.value =
                        ViewModelWrapper.response(value.getResponseDataObj().toString())
                    responseMsg = value.statusMsg
                }
                !value.statusCode.toString().contentEquals("1") -> {
                    mediatorData.value = ViewModelWrapper.error(responseStatusMsg)
                }
                else -> {
                    mediatorData.value = ViewModelWrapper.error(responseStatusMsg)
                }
            }
        }catch (e: Exception){
            mediatorData.value = ViewModelWrapper.error(errorOccurred)
        }

    }
//{"statusDescription":"Successfull","statusCode":1,"data":{"message":"Account Exists.Verify OTP"}}
    //response message
    fun respDataMessage(mediatorData: MediatorLiveData<ViewModelWrapper<String>>, value: GeneralResponce) {
        var errorOccurred = "Error occurred, please try again "
        var responseMsg = ""
        try {
            LoggerHelper.loggerError("elems","ele ${value.statusCode} and -${value.statusMsg}")
            lateinit var responseStatusMsg:String
            if(value.statusMsg.trim().isNotEmpty()){
                responseStatusMsg = value.statusMsg+" "
            }else{
                responseStatusMsg = NetworkUtility.emptyResponseMsg(value.statusCode.toString())
            }
            when {
                value.statusCode.toString().trim().contentEquals("0") -> {
                    mediatorData.value =
                        ViewModelWrapper.response(value.getResponseDataObj().toString())
                    responseMsg = value.statusMsg
                }
                !value.statusCode.toString().trim().contentEquals("0") -> {
                    responseStatusMsg = value.getErrorMsg()
                    mediatorData.value = ViewModelWrapper.error(responseStatusMsg)
                }
                else -> {

                    mediatorData.value = ViewModelWrapper.error(responseStatusMsg)
                }
            }
        }catch (e: Exception){
         //   mediatorData.value = ViewModelWrapper.error(responseStatusMsg)
          //  LoggerHelper.loggerError("requestError","error ${e.message}")
            mediatorData.value = ViewModelWrapper.error(errorOccurred)
        }

    }
    //EXTRACT FARMER REQUEST RESPONSE
    fun extractBuyerReqResponseLivedataMsg(
        endpoint:String,
        respLiveData:MediatorLiveData<ViewModelWrapper<String>>, wrapperResult: ResultWrapper<GeneralResponce>):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            is ResultWrapper.NetworkError -> respLiveData.value = ViewModelWrapper.error(" Pas de connexion Internet, vérifiez votre réseau et réessayez")
            //SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }


    //EXTRACT FARMER REQUEST RESPONSE
    fun extractGetReqResponseLivedataMsg(
        endpoint:String,
        respLiveData:MediatorLiveData<ViewModelWrapper<String>>, wrapperResult: ResultWrapper<GeneralResponce>):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            is ResultWrapper.NetworkError -> respLiveData.value = ViewModelWrapper.error(" Pas de connexion Internet, vérifiez votre réseau et réessayez")
            //SendUSSD.sendFarmergetReq(respLiveData,endpoint)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }

    /**
     * send pin reqeust
     */
    fun sendPinReqResponseLivedataMsg(
        json: JSONObject,
        endpoint: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        wrapperResult: ResultWrapper<GeneralResponce>,
        requireActivity: FragmentActivity
    ):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            //SendUSSD.makePinRequestCall(respLiveData,json,endpoint,requireActivity
            is ResultWrapper.NetworkError -> respLiveData.value = ViewModelWrapper.error(requireActivity.resources.getString(                R.string.no_internet))
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }

    fun extractReqResponseLivedataMsg(
        json: JSONObject,
        endpoint: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        wrapperResult: ResultWrapper<GeneralResponce>,
        requireActivity: FragmentActivity
    ):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            //SendUSSD.phonelookUp(respLiveData,json,endpoint,requireActivity)
            is ResultWrapper.NetworkError -> respLiveData.value =ViewModelWrapper.error(requireActivity.resources.getString(    R.string.no_internet))// SendUSSD.phonelookUp(respLiveData,json,endpoint,requireActivity)//respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }

    //extract server response and update livedata accordingly

    fun extractUpdateLiveDataMsg(respLiveData:MediatorLiveData<ViewModelWrapper<String>>, wrapperResult: ResultWrapper<GeneralResponce>):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            is ResultWrapper.NetworkError -> respLiveData.value = ViewModelWrapper.error(" Pas de connexion Internet, vérifiez votre réseau et réessayez")
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }
    /**
     * send pin reqeust
     */
    fun sendWalletBILivedataMsg(
        json: JSONObject,
        endpoint: String,
        respLiveData: MediatorLiveData<ViewModelWrapper<String>>,
        wrapperResult: ResultWrapper<GeneralResponce>,
        requireActivity: FragmentActivity
    ):MediatorLiveData<ViewModelWrapper<String>>{
        when(wrapperResult){
            is ResultWrapper.NetworkError -> respLiveData.value =ViewModelWrapper.error(requireActivity.resources.getString(
                R.string.no_internet))// respLiveData.value = ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> respLiveData.value = ViewModelWrapper.error("Error $wrapperResult")
            is ResultWrapper.Success -> respDataMessage(respLiveData,wrapperResult.value)
        }

        return respLiveData
    }
}