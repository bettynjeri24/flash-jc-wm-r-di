package com.ekenya.rnd.tijara.ui.homepage.statement

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.network.model.MiniStatementData
import com.ekenya.rnd.tijara.requestDTO.MiniStatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MiniStatementViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _ministatDetails = MutableLiveData<List<MiniStatementData>>()
    val miniStatDetails: LiveData<List<MiniStatementData>>
        get() = _ministatDetails
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isEmptyValue = MutableLiveData<Boolean>()
    val isEmptyValue: LiveData<Boolean>
        get() = _isEmptyValue
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage

    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isEmptyValue.value=false
        _isVisible.value=true


    }
    fun stopObserving() {
        _statusCode.value = null
    }
     fun getMiniStatement(miniStatementDTO: MiniStatementDTO){
         _ministatDetails.postValue(arrayListOf())
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val ministat= SaccoApi.retrofitService.getMiniStatement(miniStatementDTO)
            try {
                val miniSResponse=ministat.await()
                    Timber.d("MINISTAT RESPONSE $miniSResponse")
                    if (miniSResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCode.value=miniSResponse.status
                        if (miniSResponse.data.isNotEmpty()){
                            _ministatDetails.value=miniSResponse.data
                            Timber.d("MINISTATEMENT RESPONSE ${miniSResponse.data}")
                        }else{
                            _ministatDetails.postValue(arrayListOf())
                            _isEmptyValue.value=true
                        }
                    }else if (miniSResponse.status==0){
                        _statusMessage.value=miniSResponse.message
                        _statusCode.value=miniSResponse.status
                        _responseStatus.value= GeneralResponseStatus.DONE

                    }

            }catch (e: Exception){
                _statusCode.value=e.hashCode()
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
