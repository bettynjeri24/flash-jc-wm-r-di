package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ActivatePhoneResponse
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.SelectSaccoResponse
import com.ekenya.rnd.tijara.requestDTO.PhoneActivateDTO
import com.ekenya.rnd.tijara.requestDTO.SelectSaccoDTO
import com.ekenya.rnd.tijara.requestDTO.VerifyOtpDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.isNetwork
import com.ekenya.rnd.tijara.utils.onNoNetworkDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class DeviceVerificationViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusSacco = MutableLiveData<Int?>()
    val statusSacco: MutableLiveData<Int?>
        get() = _statusSacco
    private var _otpstatus = MutableLiveData<Int?>()
    val otpstatus: LiveData<Int?>
        get() = _otpstatus
    private var _otpMessage = MutableLiveData<String>()
    val otpMessage: LiveData<String>
        get() = _otpMessage
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _saccoMessage = MutableLiveData<String>()
    val saccoMessage: LiveData<String>
        get() = _saccoMessage

    private var _allPhoneResponse=MutableLiveData<ActivatePhoneResponse>()
    val allPhoneResponse:LiveData<ActivatePhoneResponse>
        get() = _allPhoneResponse
    private var _saccodetails = MutableLiveData<List<SaccoDetail>>()
    val saccodetails: LiveData<List<SaccoDetail>?>
        get() = _saccodetails

    private var _allSaccoDetails=MutableLiveData<SelectSaccoResponse>()
    val allSaccoDetails:LiveData<SelectSaccoResponse>
        get() = _allSaccoDetails

    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError
    val phoneNumber=MutableLiveData<String?>()
    val orgId=MutableLiveData<String?>()
    val username=MutableLiveData<String?>()
    fun setOrgId(value:String){
        orgId.value=value
    }
    fun setUserName(value:String){
        username.value=value
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _otpstatus.value=null
        _status.value=null
        _isVisible.value=true
        _statusSacco.value = null
      //  getSaccos()

    }
    fun stopObserving() {
        _status.value = null
        _isEmpty.value=false
        _otpstatus.value = null
        _statusSacco.value = null
    }
    fun activatePhone(phoneActivateDTO: PhoneActivateDTO){
        uiScope.launch {
            _isObserving.value = true
            val phoneDetails= SaccoApi.retrofitService.getPhoneNumber(phoneActivateDTO)
            try {
                val phoneResult=phoneDetails.await()
                    if (phoneResult.status==1){
                        phoneNumber.value=phoneResult.data.phone
                        PrefUtils.setPreference(app.applicationContext, "mobile", phoneResult.data.phone)
                        _status.value=phoneResult.status
                    }else if (phoneResult.status==0){
                        _statusMessage.value=phoneResult.message
                        _status.value=0

                    }



            }catch (e:HttpException){
                _status.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun verifyOTP(verifyOtpDTO:VerifyOtpDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val getOtpResults= SaccoApi.retrofitService.verifyOTP(verifyOtpDTO )
            try {
                val otpResult=getOtpResults.await()
                if (otpResult.toString().isNotEmpty()){
                    Timber.d("OTP DETAILS RESPONSE $otpResult")
                    if (otpResult.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
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
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("OTP ERROR ${e.message}")
            }
        }
    }
    /*fun getSaccos(){
       val mobile= PrefUtils.getPreferences(app.applicationContext, "mobile")
        val selectSaccoDTO= SelectSaccoDTO()

        selectSaccoDTO.phone=mobile.toString()
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val pSaccosDetails= SaccoApi.retrofitService.getSaccos(selectSaccoDTO)
            try {
                val pSaccoResult=pSaccosDetails.await()
                if (pSaccoResult.toString().isNotEmpty()){
                    Timber.d("SACCO DETAILS RESPONSE $pSaccoResult")
                    *//**for use in our recyclerview which is listing all the sacco one is registered into"*//*
                    if (pSaccoResult.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _allSaccoDetails.value=pSaccoResult
                        _saccoMessage.value=pSaccoResult.message
                        _statusSacco.value=pSaccoResult.status
                        if (pSaccoResult.data.isNotEmpty()){
                            _isEmpty.value=false
                            _saccodetails.value=pSaccoResult.data
                        }else{
                            _isEmpty.value=true
                        }

                    }else if (pSaccoResult.status==0){
                        _saccoMessage.value=pSaccoResult.message
                        _isError.value=true
                        _statusSacco.value=0

                        //  _isVisible.value=false
                    }

                }else{
                    _isError.value=true
                    //  _isVisible.value=false
                }

            }catch (e:HttpException){
                _responseStatus.value= GeneralResponseStatus.ERROR
                _isError.value=true
                _statusSacco.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }*/
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}