package com.ekenya.rnd.tijara.ui.auth.changepassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.requestDTO.VerifyUserDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class PinViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private var _statusVCode = MutableLiveData<Int?>()
    val statusVCode: MutableLiveData<Int?>
        get() = _statusVCode
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties
    private var _sProviderProperties = MutableLiveData<List<ServiceProviderItem>>()
    val sProviderProperties: LiveData<List<ServiceProviderItem>?>
        get() = _sProviderProperties
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private var _authSuccess = MutableLiveData<Boolean?>()
    val authSuccess: LiveData<Boolean?>
        get() = _authSuccess
    fun unsetAuthSuccess(){
        _authSuccess.value=null
    }


    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private val _bioMetric = MutableLiveData<Boolean>()
    val bioMetric: LiveData<Boolean>
        get() = _bioMetric
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusVCode.value=null
        _bioMetric.value = false
        _authSuccess.value=null
        _isObserving.value = false

    }
    fun bioMetrics() {
        _bioMetric.value = true
    }

    fun checkedBiometrics(){
        _bioMetric.value = false
    }
    fun verifyUser(verifyUserDTO: VerifyUserDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val vUserProperties= SaccoApi.retrofitService.verifyUser(verifyUserDTO)

            try {
                val vUserResponse=vUserProperties.await()
                if (vUserResponse.toString().isNotEmpty()){
                    Timber.d("VERIFY USER RESPONSE $vUserResponse")
                    if (vUserResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _authSuccess.value=true
                        _statusVCode.value=1
                    }else if (vUserResponse.status==0){
                        _statusMessage.value=vUserResponse.message
                        _statusVCode.value=vUserResponse.status
                    }
                }
            }catch (e:Exception){
                _statusVCode.value=e.hashCode()
            }

        }
    }
    fun stopObserving(){
        _statusVCode.value=null
        _authSuccess.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}