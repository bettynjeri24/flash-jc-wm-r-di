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

interface CooperativeRepository {
    suspend fun coopBuyerListReq(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce>
   // suspend fun buyerFundsRequest(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce>

}
class CooperativeProfilerepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    CooperativeRepository {
    val mtype = "application/json; charset=utf-8".toMediaType()
    override suspend fun coopBuyerListReq(json: JSONObject, endPoint:String): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.buyerTransactionReq(
                ApiEndpointObj.getHashToken() ,
                json.toString().toRequestBody(mtype),
                endPoint
            )
        }
    }

}