package com.ekenya.rnd.tijara.ui.auth.registration

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.OrganizationResponse
import com.ekenya.rnd.tijara.network.model.SaccoList
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class SaccoListViewModel @Inject constructor(application: Application): AndroidViewModel(application){
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage

    private var _saccoList = MutableLiveData<List<SaccoList>>()
    val saccoList: LiveData<List<SaccoList>?>
        get() = _saccoList
    private var _allSaccoList= MutableLiveData<OrganizationResponse>()
    val allSaccoList: LiveData<OrganizationResponse>
        get() = _allSaccoList

    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>

        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isError.value=false
        getAllSaccos()
    }
    fun stopObserving(){
        _statusCode.value=null
        _isObserving.value = false
    }
    fun getAllSaccos(){
        uiScope.launch {
           _responseStatus.value= GeneralResponseStatus.LOADING
            val saccoDetails= SaccoApi.retrofitService.getOrganizationList()
            try {
                val saccoResponse=saccoDetails.await()
                if (saccoResponse.status==1){
                   _responseStatus.value = GeneralResponseStatus.DONE
                    Timber.d("SACCO LISTS RESPONSE $saccoResponse")
                    _allSaccoList.postValue(saccoResponse)
                    _saccoList.postValue(saccoResponse.data)
                    _statusCode.value=1
                }else if (saccoResponse.status==0){
                    _statusCode.value=0
                }

            }catch (e: HttpException){
                _statusCode.value=e.code()
                _isError.value=true
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