package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.requestDTO.MakeDepositDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class MakeDepositViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _accountCode = MutableLiveData<Int?>()
    val accountCode: MutableLiveData<Int?>
        get() = _accountCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>>
        get() = _savingAccountProperties
    private var _sProviderProperties = MutableLiveData<List<ServiceProviderItem>>()
    val sProviderProperties: LiveData<List<ServiceProviderItem>>
        get() = _sProviderProperties
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    //
    init {
        _statusCode.value=null
        _accountCode.value=null
        _status.value=null
        loadSavingAccountName()
        loadServiceProvider()
    }
    fun stopObserving(){
        _statusCode.value=null
        _accountCode.value=null
        _status.value=null
        _isObserving.value = false
    }

    fun makeDepositToSavingAccount(makeDepositDTO: MakeDepositDTO) {
        uiScope.launch {
            _responseStatus.value = GeneralResponseStatus.LOADING
            Timber.d("REQUEST DATA $makeDepositDTO")
            val makeDepositRequest = SaccoApi.retrofitService.makeDepositToSavingAcc(makeDepositDTO)
            try {
                val depositResults = makeDepositRequest.await()
                if (depositResults.toString().isNotEmpty()) {
                    _responseStatus.value = GeneralResponseStatus.DONE
                    if (depositResults.status==1) {
                        Constants.CHARGES=depositResults.data.charges
                        Constants.EDUTY=depositResults.data.exerciseDuty
                        Constants.FORMID=depositResults.data.formId
                        _statusCode.value=depositResults.status
                    }else if (depositResults.status==0){
                        _statusMessage.value=depositResults.message
                        _statusCode.value=depositResults.status
                    }
                }
            } catch (e: Exception) {
                _responseStatus.value = GeneralResponseStatus.ERROR
                _statusCode.value=e.hashCode()
                Timber.e("ERROR ${e.message.toString()}")
            }
        }
    }
    fun commitDeposit(statementDTO: StatementDTO){
        uiScope.launch {
            val airtimeRequest= SaccoApi.retrofitService.commitDeposit(statementDTO)

            try {
                val airtimeCResponse=airtimeRequest.await()
                if (airtimeCResponse.toString().isNotEmpty()){
                    Timber.d("AIRTIME COMMIT RESPONSE $airtimeCResponse")
                    if (airtimeCResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        Constants.SELFREFID=airtimeCResponse.data.transactionCode
                        Log.d("TAG","${airtimeCResponse.data.transactionCode}")
                        _status.value=1

                    }else if (airtimeCResponse.status==0){
                        _statusCMessage.value=airtimeCResponse.message
                        _status.value=airtimeCResponse.status
                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _status.value=e.hashCode()
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
                        _accountCode.value=1


                    }
                }
            }catch (e: Exception){
                _accountCode.value=1
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
                        /* val first= ServiceProviderItem(0,"Select Service Provider")
                         val firstItem=ArrayList<ServiceProviderItem>()
                         firstItem.add(first)
                         firstItem.addAll(sProviderAccResult.data)*/
                        _sProviderProperties.value=sProviderAccResult.data


                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR SERVICE PROVIDER ACC ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}