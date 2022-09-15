package io.eclectics.cargilldigital.data.repository

import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargill.network.Webservice
import io.eclectics.cargill.network.networkCallback.GeneralResponce
import io.eclectics.cargill.network.responsewrapper.SafeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

interface FarmerRepository {

    suspend fun addBeneficiaryRequest(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce>
    suspend fun farmerGeneralRequest(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce>
    suspend fun farmerGeneralGetRequest(endPoint:String): ResultWrapper<GeneralResponce>
//farmerProfileGetReq


}
class FarmerProfileRepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    FarmerRepository {
    val mtype = "application/json; charset=utf-8".toMediaType()
    override suspend fun farmerGeneralRequest(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.farmerProfileReq(
               ApiEndpointObj.getHashToken() ,
                json.toString().toRequestBody(mtype),
                endPoint
            )
        }
    }
    override suspend fun farmerGeneralGetRequest(endPoint:String): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.farmerProfileGetReq(
                ApiEndpointObj.getHashToken(),
                endPoint
            )
        }
    }
    override suspend fun addBeneficiaryRequest(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.farmerAddAccReq(
                ApiEndpointObj.getHashToken(),
                json.toString().toRequestBody(mtype),
                endPoint
            )
        }
    }

}