package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.ShareDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class  ShareViewModel @Inject constructor(application: Application):AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _shareAccount = MutableLiveData<List<SavingAccountData>>()
    val shareAccount: LiveData<List<SavingAccountData>?>
        get() = _shareAccount
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving

    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        loadShareAccount()
    }
    fun stopObserving(){
        _statusCode.value=null
        _isObserving.value = false
    }


    /**get saving account dropdown items*/
    private fun loadShareAccount(){
        uiScope.launch {
            val shareProperties= SaccoApi.retrofitService.getSavingAccount()
            try {
                val shareAccResult=shareProperties.await()
                if (shareAccResult.toString().isNotEmpty()){
                    Timber.d("SHARE RESULTS $shareAccResult")
                    if(shareAccResult.status==1){
                        /**for the first item in the list*/
                        _shareAccount.value=shareAccResult.data
                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
}