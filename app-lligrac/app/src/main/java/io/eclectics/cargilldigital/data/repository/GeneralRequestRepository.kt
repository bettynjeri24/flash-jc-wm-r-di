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

interface GeneralRepository {

    suspend fun phonelookupRequest(
        json: JSONObject,
        endPoint: String
    ): ResultWrapper<GeneralResponce>

    suspend fun generalPinRequest(
        json: JSONObject,
        endPoint: String
    ): ResultWrapper<GeneralResponce>

}

class GeneralRequestRepository(
    private val service: Webservice,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GeneralRepository {
    private val mtype = "application/json; charset=utf-8".toMediaType()

    override suspend fun phonelookupRequest(
        json: JSONObject,
        endPoint: String
    ): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.generalProfileReq(
                HashMap(),
                json.toString().toRequestBody(mtype),
                endPoint
            )
        }
    }

    override suspend fun generalPinRequest(
        json: JSONObject,
        endPoint: String
    ): ResultWrapper<GeneralResponce> {
        return SafeCall(dispatcher) {
            service.generalProfileReq(
                ApiEndpointObj.getHashToken(),
                json.toString().toRequestBody(mtype),
                endPoint
            )
        }
    }

}