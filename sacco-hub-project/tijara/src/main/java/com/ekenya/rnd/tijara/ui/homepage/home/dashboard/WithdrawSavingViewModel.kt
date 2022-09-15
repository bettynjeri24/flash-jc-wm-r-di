package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.requestDTO.WithdrawSavingDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class WithdrawSavingViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties
    private var _sProviderProperties = MutableLiveData<List<ServiceProviderItem>>()
    val sProviderProperties: LiveData<List<ServiceProviderItem>?>
        get() = _sProviderProperties
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _isObserving.value = false
        loadSavingAccountName()
        loadServiceProvider()
    }
    fun withdrawCash(withdrawSavingDTO: WithdrawSavingDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val withdrawProperties= SaccoApi.retrofitService.withdrawCash(withdrawSavingDTO)

            try {
                val withdrawResponse=withdrawProperties.await()
                if (withdrawResponse.toString().isNotEmpty()){
                    Timber.d("WITHDRAW RESPONSE $withdrawResponse")
                    if (withdrawResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _statusCode.value=1
                    }else if (withdrawResponse.status==0){
                        _statusMessage.value=withdrawResponse.message
                        _statusCode.value=withdrawResponse.status
                    }
                }
            }catch (e: HttpException){
                _responseStatus.value= GeneralResponseStatus.ERROR
                if (e.code()==403){
                    _statusCode.value=403
                }else {
                    _statusCode.value = 400
                }
            }

        }
    }


    /**get saving account dropdown items*/
    private fun loadSavingAccountName(){
        uiScope.launch {
            val savingProperties= SaccoApi.retrofitService.getSavingAccount()
            try {
                val savingAccResult=savingProperties.await()
                if (savingAccResult.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/
                        /*val first= SavingAccountData(0,"Select Account")
                        val firstItem=ArrayList<SavingAccountData>()
                        firstItem.add(first)
                        firstItem.addAll(savingAccResult.data)*/
                        _savingAccountProperties.value=savingAccResult.data


                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    /**get Service Provider dropdown items*/
    private fun loadServiceProvider(){
        uiScope.launch {
            val serviceProviderProperties= SaccoApi.retrofitService.getServiceProvider()
            try {
                val sProviderAccResult=serviceProviderProperties.await()
                if (sProviderAccResult.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $sProviderAccResult")
                    if(sProviderAccResult.status==1){
                        /**for the first item in the list*/
                        _sProviderProperties.value=sProviderAccResult.data


                    }
                }
            }catch (e: HttpException){
                Timber.e("ERROR SERVICE PROVIDER ACC ${e.message}")
            }
        }
    }
    fun stopObserving(){
        _statusCode.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}