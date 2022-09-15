package io.eclectics.cargill.repository


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


interface AgentRepository {

    suspend fun requestfund(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun getAgentWalletBalance(): ResultWrapper<GeneralResponce>
    suspend fun fundsTopupRequest(json: JSONObject): ResultWrapper<GeneralResponce>


}
class AgentRequestRepository (private val service: Webservice, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    AgentRepository {
    val mtype = "application/json; charset=utf-8".toMediaType()
    override suspend fun requestfund(json: JSONObject): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.requestFunds(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }

    override suspend fun getAgentWalletBalance(): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.getAgentWalletBal(
                HashMap()
            )
        }
    }

    override suspend fun fundsTopupRequest(json: JSONObject): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.generalBuyerReq(
                HashMap(),
                json.toString().toRequestBody(mtype),
                ApiEndpointObj.agentRequestFund
            )
        }
    }
}