package io.eclectics.agritech.cargill.repositories

import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargill.network.Webservice
import io.eclectics.cargill.network.networkCallback.GeneralResponce
import io.eclectics.cargill.network.responsewrapper.SafeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

interface Repository {

    suspend fun loginRequest(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun registerFarmer(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun buycocoa(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun getFarmerList(): ResultWrapper<GeneralResponce> //getFarmerTransaction
    suspend fun getFarmerTransaction(): ResultWrapper<GeneralResponce> //merchantPayReq
    suspend fun buyAirtimeReq(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun getFarmerWalletBalance(): ResultWrapper<GeneralResponce>
    suspend fun merchantPayReq(json: JSONObject): ResultWrapper<GeneralResponce>
}
class MainRequestRepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : Repository {
    val mtype = "application/json; charset=utf-8".toMediaType()
    override suspend fun loginRequest(json: JSONObject): ResultWrapper<GeneralResponce> {
       // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.generalRequest(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }

    override suspend fun registerFarmer(json: JSONObject): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.registerFarmer(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }

    override suspend fun buycocoa(json: JSONObject): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.buycocoa(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }

    override suspend fun getFarmerList(): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.getFarmerlist(
            HashMap()
            )
        }
    }
    override suspend fun getFarmerWalletBalance(): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.getFarmerWalletBalance(
                HashMap()
            )
        }
    }
    override suspend fun getFarmerTransaction(): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.getFarmerTransaction(
                HashMap()
            )
        }
    }
    //buy airtime
    override suspend fun buyAirtimeReq(json: JSONObject): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.buyAirtime(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }

    //pay merchant
    //buy airtime
    override suspend fun merchantPayReq(json: JSONObject): ResultWrapper<GeneralResponce> {
        // LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.generalTestOnEnd(
                HashMap(),
                json.toString().toRequestBody(mtype),
                "merchantpay"
            )
        }
    }
}