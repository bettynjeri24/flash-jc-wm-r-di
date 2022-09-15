package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MobileMoneyViewmodel(application:Application):AndroidViewModel(application){
val app=application
private var _mstatusCode = MutableLiveData<Int?>()
val mstatusCode: MutableLiveData<Int?>
    get() = _mstatusCode
private var _mobiledetails = MutableLiveData<List<ServiceProviderItem>>()
val mobiledetails: LiveData<List<ServiceProviderItem>?>
    get() = _mobiledetails

private var _isObserving = MutableLiveData<Boolean>()
val isObserving: LiveData<Boolean>
    get() = _isObserving
private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
val responseStatus: LiveData<GeneralResponseStatus>
    get() = _responseStatus
private var _isError = MutableLiveData<Boolean>()
val isError: LiveData<Boolean>
    get() = _isError
private var _isEmpty = MutableLiveData<Boolean>()
val isEmpty: LiveData<Boolean>
    get() = _isEmpty
private var _isVisible = MutableLiveData<Boolean>()
val isVisible: LiveData<Boolean>
    get() = _isVisible
private val viewModelJob = Job()
private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)



init {
    _mstatusCode.value=null
    _isEmpty.value=false
    _isError.value=false
    _isVisible.value=true
    _isObserving.value = false
    getMobileMoneyList()

}
fun stopObserving() {
    _mstatusCode.value = null
    _isObserving.value = false
}
private fun getMobileMoneyList(){
    uiScope.launch {
        _isObserving.value = true
        val mobileDetails= SaccoApi.retrofitService.getServiceProvider()
        try {
            val mobileResponse=mobileDetails.await()
                Timber.d("BANK LISTS RESPONSE $mobileResponse")
                if (mobileResponse.status==1){
                    _mstatusCode.value=mobileResponse.status
                    if (mobileResponse.data.isNotEmpty()){
                        _mobiledetails.value= mobileResponse.data
                    }else{
                        _isVisible.value=false
                        _isEmpty.value=true
                    }
                
            }

        }catch (e: Exception){
            _responseStatus.value= GeneralResponseStatus.ERROR
            Timber.e("ERROR Exception${e.message}")
        }
    }
}
override fun onCleared() {
    super.onCleared()
    viewModelJob.cancel()
}
}