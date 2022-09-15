package com.ekenya.lamparam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.ekenya.pepsidistributor.networkCallback.respcallback.GeneralResponce
import co.ekenya.pepsidistributor.responsewrapper.ResultWrapper
import co.ekenya.pepsidistributor.responsewrapper.SafeCall
import com.ekenya.lamparam.model.APIErrorResponse
import com.ekenya.lamparam.model.CompleteTransactionReq
import com.ekenya.lamparam.model.FundsTransferReceiveMoneyReq
import com.ekenya.lamparam.model.RemittanceTransactionLookupReq
import com.ekenya.lamparam.model.response.ConfirmTransactionResponse
import com.ekenya.lamparam.model.response.RemittanceTransactionLookupResponse
import com.ekenya.lamparam.networkCallback.network.Webservice
import com.ekenya.lamparam.utilities.LoggerHelper
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

interface Repository {
    suspend fun loginRequest(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun generalRequest(json: JSONObject): ResultWrapper<GeneralResponce>
    suspend fun sendSMSRequest(json: JSONObject): ResultWrapper<GeneralResponce>
}

class MainRepositoryOld(
    private val service: Webservice,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {
    val mtype = "application/json; charset=utf-8".toMediaType()
    override suspend fun loginRequest(json: JSONObject): ResultWrapper<GeneralResponce> {
        LoggerHelper.loggerSuccess("loginoffline", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.generalRequest(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
        //try returning general class sample
        /* var genResp = loginDataResponse()
          return  SafeCall(dispatcher){genResp}*/
    }

    override suspend fun generalRequest(json: JSONObject): ResultWrapper<GeneralResponce> {
        LoggerHelper.loggerSuccess("offlinegeneral", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.generalRequest(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }

    override suspend fun sendSMSRequest(json: JSONObject): ResultWrapper<GeneralResponce> {
        LoggerHelper.loggerSuccess("sendSMSgeneral", "can be accesssed while offline")
        return SafeCall(dispatcher) {
            service.pushSMSRequest(
                HashMap(),
                json.toString().toRequestBody(mtype)
            )
        }
    }


    fun doTransactionLookupReq(mPhoneNumberLookupReq: RemittanceTransactionLookupReq): LiveData<MyApiResponse>
    {
        val data = MutableLiveData<MyApiResponse>()

        service.transactionLookupRemittance(mPhoneNumberLookupReq)
            .enqueue(object : Callback<RemittanceTransactionLookupResponse> {
                override fun onResponse(
                    call: Call<RemittanceTransactionLookupResponse>,
                    response: Response<RemittanceTransactionLookupResponse>
                ) {

                    if(response.code() == 200)
                    {
                        response.toString()
                        data.postValue(
                            MyApiResponse(
                                "RemittanceTransactionLookupReq",
                                response.body() as RemittanceTransactionLookupResponse,
                                true,
                                response.code()
                            )
                        )
                    }
                    else
                    {
                        response.toString()
                        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), APIErrorResponse::class.java)
                        data.postValue(
                            MyApiResponse(
                                "RemittanceTransactionLookupReq",
                                errorResponse,
                                true,
                                response.code()
                            ))

                    }

                }

                override fun onFailure(call: Call<RemittanceTransactionLookupResponse>, t: Throwable) {
                    handleAPIError(t,data,"RemittanceTransactionLookupReq")
                }
            })
        return data
    }

    fun doCompleteTransReq(mCompleteTransactionReq: CompleteTransactionReq): LiveData<MyApiResponse>
    {
        val data = MutableLiveData<MyApiResponse>()

        service.completeTransReq(mCompleteTransactionReq)
            .enqueue(object : Callback<ConfirmTransactionResponse> {
                override fun onResponse(
                    call: Call<ConfirmTransactionResponse>,
                    response: Response<ConfirmTransactionResponse>
                ) {

                    if(response.code() == 200)
                    {
                        response.toString()
                        data.postValue(
                            MyApiResponse(
                                "CompleteTransactionReq",
                                response.body() as ConfirmTransactionResponse,
                                true,
                                response.code()
                            )
                        )
                    }
                    else
                    {
                        response.toString()
                        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), APIErrorResponse::class.java)
                        data.postValue(
                            MyApiResponse(
                                "CompleteTransactionReq",
                                errorResponse,
                                true,
                                response.code()
                            ))

                    }

                }

                override fun onFailure(call: Call<ConfirmTransactionResponse>, t: Throwable) {
                    handleAPIError(t,data,"CompleteTransactionReq")
                }
            })
        return data
    }

    fun doFundsTransferReq(req: FundsTransferReceiveMoneyReq): LiveData<MyApiResponse>
    {
        val data = MutableLiveData<MyApiResponse>()

        service.doFundsTransferReq(req)
            .enqueue(object : Callback<ConfirmTransactionResponse> {
                override fun onResponse(
                    call: Call<ConfirmTransactionResponse>,
                    response: Response<ConfirmTransactionResponse>
                ) {

                    if(response.code() == 200)
                    {
                        response.toString()
                        data.postValue(
                            MyApiResponse(
                                "mFundsTransferReceiveMoneyReq",
                                response.body() as ConfirmTransactionResponse,
                                true,
                                response.code()
                            )
                        )
                    }
                    else
                    {
                        response.toString()
                        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), APIErrorResponse::class.java)
                        data.postValue(
                            MyApiResponse(
                                "mFundsTransferReceiveMoneyReq",
                                errorResponse,
                                true,
                                response.code()
                            ))

                    }

                }

                override fun onFailure(call: Call<ConfirmTransactionResponse>, t: Throwable) {
                    handleAPIError(t,data,"mFundsTransferReceiveMoneyReq")
                }
            })
        return data
    }

    fun handleAPIError(t: Throwable, mData: MutableLiveData<MyApiResponse>,req:String) {
        if(t is IOException || t is UnknownHostException)
        {
            mData.postValue(
                MyApiResponse(
                    req,
                    null,
                    false,
                    300
                )
            )

        }
        else if (t is HttpException)
        {
            val jerror: String = t.response()?.errorBody()?.string()!!
            mData.postValue(
                MyApiResponse(
                    req,
                    jerror,
                    false,
                    t.code()
                )
            )
        }


    }

}
