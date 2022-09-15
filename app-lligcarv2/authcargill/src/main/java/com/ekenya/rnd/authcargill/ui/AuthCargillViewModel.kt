package com.ekenya.rnd.authcargill.ui

import android.app.Application
import androidx.lifecycle.*
import com.ekenya.rnd.authcargill.data.repository.AuthRepository
import com.ekenya.rnd.authcargill.data.responses.AccountIdLookupMobileResponse
import com.ekenya.rnd.authcargill.data.responses.AccountMobileLookUpResponse
import com.ekenya.rnd.common.MEDIA_TYPE_JSON
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponse
import com.ekenya.rnd.common.data.db.entity.CargillUserLoginResponseData
import com.ekenya.rnd.common.data.network.ResourceNetwork
import com.ekenya.rnd.common.utils.base.viewmodel.BaseViewModel
import com.ekenya.rnd.common.utils.custom.getDeviceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class AuthCargillViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val app: Application
) : BaseViewModel(app) {
// *********************************************************************************
    /**
     * mobileNumberLookUp
     */
    private val _mobileNumberLookUp: MutableLiveData<ResourceNetwork<AccountMobileLookUpResponse>> =
        MutableLiveData()
    val mobileNumberLookUp: LiveData<ResourceNetwork<AccountMobileLookUpResponse>>
        get() = _mobileNumberLookUp

    fun mobileNumberLookUp(jsonObject: JSONObject) = viewModelScope.launch {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)

        _mobileNumberLookUp.value = repository.mobileNumberLookUp(requestBody)
        _mobileNumberLookUp.value = ResourceNetwork.Loading
    }
// *********************************************************************************
    /**
     * ActivateMobileApp
     */
    suspend fun cooparativeIdLookUp(jsonObject: JSONObject): AccountIdLookupMobileResponse {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        return repository.cooparativeIdLookUp(requestBody)
    }

    private val _responseCooparativeIdLookUp: MutableLiveData<ResourceNetwork<AccountIdLookupMobileResponse>> =
        MutableLiveData()
    val responseCooparativeIdLookUp: LiveData<ResourceNetwork<AccountIdLookupMobileResponse>>
        get() = _responseCooparativeIdLookUp

    fun sendCooparativeIdLookUp(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        _responseCooparativeIdLookUp.value = ResourceNetwork.Loading
        _responseCooparativeIdLookUp.value = repository.cooparativeIdLookUp2(requestBody)
    }
// *********************************************************************************
    // *********************************************************************************
    /**
     * ActivateMobileApp
     */
    suspend fun verifyOtpForAccountSetUp(jsonObject: JSONObject) = withContext(Dispatchers.IO) {
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        repository.verifyOtpForAccountSetUp(requestBody)
    }
    // *********************************************************************************
    /**
     * ActivateMobileApp
     */
    suspend fun setNewAccountPin(jsonObject: JSONObject) = withContext(Dispatchers.IO) {
        jsonObject.put("deviceId", app.getDeviceId())
        val requestBody = jsonObject.toString().toRequestBody(MEDIA_TYPE_JSON)
        repository.setNewAccountPin(requestBody)
    }

// *********************************************************************************

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
// *********************************************************************************

    suspend fun saveUserVmRoom(user: CargillUserLoginResponseData) = repository.saveUserInRoom(user)

    //
    fun getUserVmRoom() = repository.getUserInRoom().asLiveData()
}
