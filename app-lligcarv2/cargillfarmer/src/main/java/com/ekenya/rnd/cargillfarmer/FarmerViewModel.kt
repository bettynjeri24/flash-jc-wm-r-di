package com.ekenya.rnd.cargillfarmer

import android.app.Application
import android.content.Context
import android.os.*
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.ekenya.rnd.cargillfarmer.data.repository.FarmerRepository
import com.ekenya.rnd.cargillfarmer.data.responses.*
import com.ekenya.rnd.cargillfarmer.data.responses.farmer.FarmerBalanceInquiryResponse
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.utils.custom.Coroutines
import com.ekenya.rnd.common.utils.custom.deviceSessionUUID
import com.ekenya.rnd.common.utils.custom.getDeviceId
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import javax.inject.Inject

class FarmerViewModel @Inject constructor(
    private val repository: FarmerRepository,
    private val app: Application
) : AndroidViewModel(app) {
// *********************************************************************************
    /**
     * MyCashOutChannelsLisFromRoom
     */
    fun requestMyCashOutChannelsLisFromRoom(jsonObject: JSONObject): Deferred<LiveData<List<MyCashOutChannelsData>>> {
        jsonObject.put(
            "deviceUUId",
            deviceSessionUUID()
        )
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        val myCashOutChannelsList by Coroutines.lazyDeferred {
            repository.getDataFromRoom(requestBody)
        }
        return myCashOutChannelsList
    }

    /**
     * FarmerBalanceInquiryResponse
     */
    suspend fun requestFarmerBalanceInquiryResponse(requestBody: RequestBody): FarmerBalanceInquiryResponse {
        return repository.balanceInquiry(requestBody)
    }
// *********************************************************************************

    /**
     * getLatestTransactions
     */
    suspend fun requestLatestTransactionsResponse(requestBody: RequestBody): FarmerLatestTransactionResponse {
        return repository.latestTransactions(requestBody)
    }

    // *********************************************************************************

    /**
     * getMyCashOutChannels
     */
    suspend fun requestMyCashOutChannelsData(requestBody: RequestBody): MyCashOutChannelsResponse {
        return repository.myCashOutChannels(requestBody)
    }
    // *********************************************************************************

    /**
     * GET FarmerCashout
     */
    suspend fun requestFarmerCashoutData(jsonObject: JSONObject): FarmerCashoutResponse {
        jsonObject.put(
            "deviceUUId",
            deviceSessionUUID()
        )
        jsonObject.put(
            "deviceId",
            app.getDeviceId()
        )
        jsonObject.put(
            "language",
            "fr"
        )
        Timber.e("==========jsonObject============$jsonObject")
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)

        return repository.farmercashout(requestBody)
    }
    // *********************************************************************************
    /**
     * Get AddBeneficiaryAccount
     */

    suspend fun requestAddBeneficiaryAccountData(jsonObject: JSONObject): AddBeneficiaryAccountResponse {
        jsonObject.put("bankName", "")
        jsonObject.put("cardNumber", "")
        jsonObject.put("expiryDate", "")
        jsonObject.put("cvc", "")
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.addBeneficiaryAccount(requestBody)
    }

    // ***************************Save To Room******************************************************
    suspend fun saveVmRoom(list: List<FarmerLatestTransactionData>) {
        /*withContext(Dispatchers.IO) {*/
        return repository.saveInRoom(list)
        // }
    }

    // *************************Get To Room********************************************************
    fun getFromRoom() = repository.getRoomData().asLiveData()

    // *********************************************************************************
    /**
     * getRemoveBeneficiary
     */
    suspend fun requestRemoveBeneficiary(jsonObject: JSONObject): RemoveAccountResponse {
        jsonObject.put("deviceId", app.getDeviceId())
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.removeBeneficiary(requestBody)
    }

    // *********************************************************************************
    /**
     * getRemoveBeneficiary
     */
    suspend fun requestVerifyAddAccountData(jsonObject: JSONObject): VerifyAddAccountResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.verifyBeneficiaryAccount(requestBody)
    }

    // GET USERDATA
    fun getUserVmRoom() = repository.getUserInRoom().asLiveData()
}

//

internal interface UssdResultNotifiable {
    fun notifyUssdResult(request: String?, returnMessage: String?, resultCode: Int)
}

internal class USSDSessionHandler(parent: Context, private val client: UssdResultNotifiable) {
    var tm: TelephonyManager?
    private var handleUssdRequest: Method? = null
    private var iTelephony: Any? = null

    // Get the internal ITelephony object
    @get:Throws(
        ClassNotFoundException::class,
        NoSuchMethodException::class,
        InvocationTargetException::class,
        IllegalAccessException::class
    )
    private val ussdRequestMethod: Unit
        private get() {
            if (tm != null) {
                val telephonyManagerClass = Class.forName(tm!!.javaClass.name)
                if (telephonyManagerClass != null) {
                    val getITelephony: Method =
                        telephonyManagerClass.getDeclaredMethod("getITelephony")
                    getITelephony.setAccessible(true)
                    iTelephony = getITelephony.invoke(tm) // Get the internal ITelephony object
                    val methodList: Array<Method> = iTelephony!!.javaClass.methods
                    handleUssdRequest = null
                    for (_m in methodList) if (_m.getName().equals("handleUssdRequest")) {
                        handleUssdRequest = _m
                        break
                    }
                }
            }
        }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun doSession(ussdRequest: String?) {
        try {
            if (handleUssdRequest != null) {
                handleUssdRequest!!.setAccessible(true)
                handleUssdRequest!!.invoke(
                    iTelephony,
                    SubscriptionManager.getDefaultSubscriptionId(),
                    ussdRequest,
                    object : ResultReceiver(Handler(Looper.getMainLooper())) {
                        protected override fun onReceiveResult(
                            resultCode: Int,
                            ussdResponse: Bundle
                        ) {
                            val p: Any? = ussdResponse.getParcelable("USSD_RESPONSE")
                            if (p != null) {
                                val methodList: Array<Method> = p.javaClass.methods
                                for (m in methodList) {
                                    Timber.e("TAG", "onReceiveResult: " + m.getName())
                                }
                                try {
                                    val returnMessage = p.javaClass.getMethod("getReturnMessage")
                                        .invoke(p) as CharSequence
                                    val request = p.javaClass.getMethod("getUssdRequest")
                                        .invoke(p) as CharSequence
                                    client.notifyUssdResult(
                                        "" + request,
                                        "" + returnMessage,
                                        resultCode
                                    )
                                    // they could be null
                                } catch (e: NoSuchMethodException) {
                                    e.printStackTrace()
                                } catch (e: IllegalAccessException) {
                                    e.printStackTrace()
                                } catch (e: InvocationTargetException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                )
            }
        } catch (e1: IllegalAccessException) {
            e1.printStackTrace()
        } catch (e1: InvocationTargetException) {
            e1.printStackTrace()
        }
    }

    init {
        tm = parent.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
        try {
            ussdRequestMethod
        } catch (ex: Exception) {
            // log
        }
    }
}

/**

private fun onUssdSend() {
hdl = USSDSessionHandler(requireActivity(), ussdResultNotifiable)
hdl.doSession("*605*200#")

}

private var ussdResultNotifiable = object : UssdResultNotifiable {
override fun notifyUssdResult(request: String?, returnMessage: String?, resultCode: Int) {
requireActivity().runOnUiThread(Runnable {
Toast.makeText(
requireActivity(),
"Request was ${request.toString()} " +
"response is ${returnMessage.toString()} " +
"result code is $resultCode",
Toast.LENGTH_LONG
).show()
Timber.e("TAG", "ussd result hit! Response is = $returnMessage")
hdl.doSession("1")
})
}
}

 */
