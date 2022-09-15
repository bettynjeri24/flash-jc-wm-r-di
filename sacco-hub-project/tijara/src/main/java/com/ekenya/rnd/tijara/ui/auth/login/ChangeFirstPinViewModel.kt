package com.ekenya.rnd.tijara.ui.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.requestDTO.ChangeFirstPinDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class ChangeFirstPinViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null

    }
    fun stopObserving(){
        _statusCode.value=null
    }

    fun setFirstPin(changeFirstPinDTO: ChangeFirstPinDTO) {
        uiScope.launch {
            _responseStatus.value = GeneralResponseStatus.LOADING
            Timber.d("REQUEST DATA $changeFirstPinDTO")
            val getFirstPinResults = SaccoApi.retrofitService.firstTimePassword(changeFirstPinDTO)
            try {
                val passResults = getFirstPinResults.await()
                if (passResults.toString().isNotEmpty()) {
                    _responseStatus.value = GeneralResponseStatus.DONE
                    if (passResults.status==1) {
                        _statusCode.value=passResults.status
                    }else if (passResults.status==0){
                        _statusMessage.value=passResults.message
                        _statusCode.value=0
                    }
                }
            } catch (e: HttpException) {
                _responseStatus.value = GeneralResponseStatus.ERROR
                if (e.code()==403){
                    _statusCode.value=403
                }else if (e.code()==422){
                    _statusCode.value=422
                }
                Timber.e("ERROR ${e.message.toString()}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}