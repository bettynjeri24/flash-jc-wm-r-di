package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ActivatePhoneResponse
import com.ekenya.rnd.tijara.network.model.PesalinkBank
import com.ekenya.rnd.tijara.network.model.PesalinkPCheckResponse
import com.ekenya.rnd.tijara.requestDTO.PesalinkPhoneCheckDTO
import com.ekenya.rnd.tijara.requestDTO.PhoneActivateDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.utils.PrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class PesalinkPhoneCheckViewModel(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _allPhoneResponse= MutableLiveData<PesalinkPCheckResponse>()
    val allPhoneResponse: LiveData<PesalinkPCheckResponse>
        get() = _allPhoneResponse

    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    var name = MutableLiveData<String?>()
    fun setName(value:String){
        name.value=value
    }
    var phone = MutableLiveData<String>()
    fun setPhone(value:String){
        phone.value=value
    }




    init {
        _status.value=null
        _isObserving.value = false
    }
    fun stopObserving() {
        _status.value = null
        _isObserving.value = false
    }
    fun pesaLinkPhoneCheck(pesalinkPhoneCheckDTO: PesalinkPhoneCheckDTO){
        uiScope.launch {
            _isObserving.value = true
            val phoneDetails= SaccoApi.retrofitService.pesalinkPhoneCheck(pesalinkPhoneCheckDTO)
            try {
                val phoneResult=phoneDetails.await()

                if (phoneResult.status==1){
                    phone.value=phoneResult.data.phoneNumber
                    name.value=phoneResult.data.name
                    _status.value=phoneResult.status
                    if (phoneResult.data.banks.isNotEmpty()){

                    }else{
                        //empty banks
                    }
                }else if (phoneResult.status==0){
                    _statusMessage.value=phoneResult.message
                    _status.value=0

                }
            }catch (e: HttpException){
                when {
                    e.code()==500 -> {
                        _status.value=e.code()
                    }
                    else -> {
                        Timber.e("PESALINK ERROR ${e.message}")
                    }
                }
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    /**get saving account dropdown items*/

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}