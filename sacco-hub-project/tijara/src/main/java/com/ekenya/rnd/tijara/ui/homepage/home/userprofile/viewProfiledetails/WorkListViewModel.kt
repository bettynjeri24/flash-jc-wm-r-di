package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ViewWorkInfoList
import com.ekenya.rnd.tijara.network.model.WorkInfo
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class WorkListViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _workdetails = MutableLiveData<List<WorkInfo>>()
    val workdetails: LiveData<List<WorkInfo>?>
        get() = _workdetails

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
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isVisible.value=true
        _isObserving.value = false
        getWorkInfo()

    }
    fun stopObserving() {
        _statusCode.value = null
        _isObserving.value = false
    }
    private fun getWorkInfo(){
        uiScope.launch {
            _isObserving.value = true
            _responseStatus.value= GeneralResponseStatus.LOADING
            val workDetails= SaccoApi.retrofitService.getPersonalInfo()
            try {
                val workResponse=workDetails.await()
                    Timber.d("WorkLISTS RESPONSE $workResponse")
                    if (workResponse.status == 1) {
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCode.value=workResponse.status
                        if (workResponse.data.workInfo.isNotEmpty()) {
                            _workdetails.value = workResponse.data.workInfo

                        } else {
                            _isVisible.value = false
                            _isEmpty.value = true
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