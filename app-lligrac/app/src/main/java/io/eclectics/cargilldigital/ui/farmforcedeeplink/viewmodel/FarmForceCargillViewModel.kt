package io.eclectics.cargilldigital.ui.farmforcedeeplink.viewmodel


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.eclectics.cargilldigital.data.db.entity.FarmForceData
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.data.repository.ffrepos.FarmForceRepository
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponse
import io.eclectics.cargilldigital.data.responses.ffresponses.PaymentTransactionsResponse
import io.eclectics.cargilldigital.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

val MEDIA_TYPE_JSON = "application/json; charset=utf-8".toMediaType()

class FarmForceCargillViewModel @Inject constructor(
    application: Application,
    private val repository: FarmForceRepository
) : BaseViewModel(application) {


// *********************************************************************************
    /**
     * LOGIN TO SYSTEM
     */

    private val _getloggedInUser: MutableLiveData<ResourceNetwork<CargillUserLoginResponse>> =
        MutableLiveData()
    val getloggedInUser: LiveData<ResourceNetwork<CargillUserLoginResponse>>
        get() = _getloggedInUser

    fun loginUser(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        _getloggedInUser.value = ResourceNetwork.Loading
        _getloggedInUser.value = repository.loginUser(requestBody)
    }

//*********************************************************************************
    /**
     * mobileNumberLookUp
     */
    private val _paymentTransactions: MutableLiveData<ResourceNetwork<PaymentTransactionsResponse>> =
        MutableLiveData()
    val getResponsePaymentTransactions: LiveData<ResourceNetwork<PaymentTransactionsResponse>>
        get() = _paymentTransactions

    // [Yesterday 10:06] Daniel Kimani
    fun requestPaymentTransactions(jsonObject: JSONObject) = viewModelScope.launch {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        _paymentTransactions.value = repository.paymentTransactions(requestBody)
    }


    //*********************************************************************************
    suspend fun saveUserVmRoom(user: FarmForceData) = repository.saveUserInRoom(user)

    //
    fun getUserVmRoom() = repository.getAllFarmForceDataWithFlow().asLiveData()
    //*********************************************************************************
}