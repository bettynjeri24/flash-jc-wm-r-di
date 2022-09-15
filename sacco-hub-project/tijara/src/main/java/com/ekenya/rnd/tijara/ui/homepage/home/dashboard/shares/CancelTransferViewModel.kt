package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.PesalinkPCheckResponse
import com.ekenya.rnd.tijara.requestDTO.CancelTransferDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class CancelTransferViewModel(application: Application) : AndroidViewModel(application) {
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



    init {
        _status.value=null
        _isObserving.value = false
    }
    fun stopObserving() {
        _status.value = null
        _isObserving.value = false
    }
    fun cancelShareTransfer(cancelTransferDTO: CancelTransferDTO){
        uiScope.launch {
            _isObserving.value = true
            val cancelRequest= SaccoApi.retrofitService.cancelTransfer(cancelTransferDTO)
            try {
                val cancelResponse=cancelRequest.await()

                if (cancelResponse.status==1){
                    _status.value=cancelResponse.status
                }else if (cancelResponse.status==0){
                    _statusMessage.value=cancelResponse.message
                    _status.value=0

                }
            }catch (e: HttpException){
                when {
                    e.code()==500 -> {
                        _status.value=e.code()
                    }
                    else -> {
                        Timber.e(" ERROR ${e.message}")
                    }
                }
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}