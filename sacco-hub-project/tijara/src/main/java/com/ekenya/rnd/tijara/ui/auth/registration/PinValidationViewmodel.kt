package com.ekenya.rnd.tijara.ui.auth.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.requestDTO.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.io.File

class PinValidationViewmodel( private val app: Application) : AndroidViewModel(app) {

    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _formId = MutableLiveData<Int?>()
    val formId: MutableLiveData<Int?>
        get() = _formId

    private var _username = MutableLiveData<String?>()
    val username: MutableLiveData<String?>
        get() = _username
    fun setFormId(value:Int){
        _formId.value=value
    }
    var frontIdPhoto: File?=null
    var backIdPhoto: File?=null
    var selfiePhoto: File?=null

    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _photoMessage = MutableLiveData<String>()
    val photoMessage: LiveData<String>
        get() = _photoMessage
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _otpstatus = MutableLiveData<Int?>()
    val otpstatus: LiveData<Int?>
        get() = _otpstatus
    private var _otpMessage = MutableLiveData<String>()
    val otpMessage: LiveData<String>
        get() = _otpMessage
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _status.value=null
        _otpstatus.value=null
        _statusCode.value=null
        _isObserving.value = false
    }
    fun verifyDefaultPin(defaultPinDTO: DefaultPinDTO){
        uiScope.launch {
            val getOtpResults= SaccoApi.retrofitService.defaultPin(defaultPinDTO )
            try {
                val otpResult=getOtpResults.await()
                if (otpResult.toString().isNotEmpty()){
                    Timber.d("OTP DETAILS RESPONSE $otpResult")
                    if (otpResult.status==1){
                        _otpMessage.value=otpResult.message
                        _otpstatus.value=1
                    }
                    if (otpResult.status==0){
                        _otpMessage.value=otpResult.message
                        _otpstatus.value=otpResult.status
                    }

                }
            }catch (e: HttpException){
                _otpstatus.value=e.code()
                Timber.e("OTP ERROR ${e.message}")
            }
        }
    }

    fun setNewPIn(newPinDTO: NewPinDTO) {
        uiScope.launch {
            val getNewPassword= SaccoApi.retrofitService.newPin(newPinDTO)
            try {
                val passwordResults=getNewPassword.await()
                if (passwordResults.status==1){
                    _statusCode.value = passwordResults.status
                    Timber.d("PASSWORD RESPONSE $passwordResults")
                }else if (passwordResults.status==0){
                    _statusMessage.value=passwordResults.message
                    _statusCode.value=0
                }

            }catch (e:HttpException){
                _statusCode.value=e.code()
                Timber.e("NEW PASSWORD ERROR ${e.message.toString()}")
            }

        }
    }
    fun stopObserving(){
        _status.value=null
        _statusCode.value=null
        _otpstatus.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}