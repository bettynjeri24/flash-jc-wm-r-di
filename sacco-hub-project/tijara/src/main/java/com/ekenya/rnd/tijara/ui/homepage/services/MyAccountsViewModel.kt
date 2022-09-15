package com.ekenya.rnd.tijara.ui.homepage.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.SelectSaccoResponse
import com.ekenya.rnd.tijara.requestDTO.LoginSaccoDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.utils.PrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MyAccountsViewModel(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _saccodetails = MutableLiveData<List<SaccoDetail>>()
    val saccodetails: LiveData<List<SaccoDetail>>
        get() = _saccodetails
    private var _loginSaccos = MutableLiveData<List<SaccoDetail>>()
    val loginSaccos: LiveData<List<SaccoDetail>>
        get() = _loginSaccos

    private var _allSaccoDetails= MutableLiveData<SelectSaccoResponse>()
    val allSaccoDetails: LiveData<SelectSaccoResponse>
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
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        _status.value=null
        _isEmpty.value=false
        _isError.value=false
        _isVisible.value=true
        _isObserving.value = false
        getAssociatedSaccos()
    }
    fun stopObserving() {
        _status.value = null
        _isObserving.value = false
    }

    private fun getAssociatedSaccos(){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val phon = PrefUtils.getPreferences(app.applicationContext, "mobile")!!
            val loginSaccoDTO= LoginSaccoDTO()
            loginSaccoDTO.phone=phon
            loginSaccoDTO.isLogin=1
            val saccoDetails= SaccoApi.retrofitService.getAssociatedSacco(loginSaccoDTO)
            try {
                val saccoResponse=saccoDetails.await()
                if (saccoResponse.status==1){
                    _responseStatus.value = GeneralResponseStatus.DONE
                    Timber.d("SACCO LISTS RESPONSE $saccoResponse")
                    _loginSaccos.postValue(saccoResponse.data)
                    _status.value=1
                }else if (saccoResponse.status==0){
                    _responseStatus.value = GeneralResponseStatus.DONE
                    _status.value=0
                }

            }catch (e: Exception){
                _isError.value=true
                _responseStatus.value= GeneralResponseStatus.ERROR
                _status.value=e.hashCode()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}