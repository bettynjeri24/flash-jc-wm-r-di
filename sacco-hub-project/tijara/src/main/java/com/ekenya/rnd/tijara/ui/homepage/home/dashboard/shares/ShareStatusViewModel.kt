package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.RequestsReceived
import com.ekenya.rnd.tijara.network.model.RequestsSent
import com.ekenya.rnd.tijara.network.model.ShareBalance
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ShareStatusViewModel (application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode

    private var _shareProperties = MutableLiveData<List<RequestsReceived>>()
    val shareProperties: LiveData<List<RequestsReceived>?>
        get() = _shareProperties
    private var _shareBalance = MutableLiveData<ShareBalance>()
    val shareBalance: LiveData<ShareBalance>
        get() = _shareBalance
    private var _shareSentProperties = MutableLiveData<List<RequestsSent>>()
    val shareSentProperties: LiveData<List<RequestsSent>?>
        get() = _shareSentProperties

    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isEmptySent = MutableLiveData<Boolean>()
    val isEmptySent: LiveData<Boolean>
        get() = _isEmptySent
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _isVisible.value=true
        getShareStatus()
    }
    fun stopObserving() {
        _statusCode.value = null
    }
     private fun getShareStatus(){
         _shareSentProperties.value= arrayListOf()
         _shareProperties.value= arrayListOf()
        uiScope.launch {
            val shareRequest= SaccoApi.retrofitService.requestShareStatus()
            try {
                val shareResponse=shareRequest.await()
                    Timber.d("SHARE RESPONSE $shareResponse")
                    if (shareResponse.status==1){
                        _shareBalance.value=shareResponse.data.shareBalance
                        if (shareResponse.data.requestsReceived.isNotEmpty()){
                            _shareProperties.value= shareResponse.data.requestsReceived
                            _isVisible.value=true
                            _isEmpty.value=false
                        }else{
                            Log.d("TAG","CHECK !")
                            _shareProperties.value= arrayListOf()
                            _isVisible.value=false
                            _isEmpty.value=true
                        }
                        if (shareResponse.data.requestsSent.isNotEmpty()){
                            _shareSentProperties.value= shareResponse.data.requestsSent
                            _isEmptySent.value=false
                            _isVisible.value=true
                        }else{
                            Log.d("TAG","CHECK 2")
                            _shareSentProperties.value= arrayListOf()
                            _isEmptySent.value=true
                            _isVisible.value=false
                        }
                        _statusCode.value=shareResponse.status
                    }else if (shareResponse.status==0){
                        _responseStatus.value= GeneralResponseStatus.ERROR
                        _statusCode.value=shareResponse.status
                    }


            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                _statusCode.value=e.hashCode()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
