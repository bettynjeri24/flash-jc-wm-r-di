package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.model.ViewBankInfoList
import com.ekenya.rnd.tijara.network.model.ViewBankInfoResponse
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ViewBankInfoViewModel(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode

    private var _bankInfoList = MutableLiveData<List<ViewBankInfoList>>()
    val bankInfoList: LiveData<List<ViewBankInfoList>?>
        get() = _bankInfoList
    private var _allBankInfoList= MutableLiveData<ViewBankInfoResponse>()
    val allBankInfoList: LiveData<ViewBankInfoResponse>
        get() = _allBankInfoList

    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>

        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
        _statusCode.value=null
        _isEmpty.value=false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}