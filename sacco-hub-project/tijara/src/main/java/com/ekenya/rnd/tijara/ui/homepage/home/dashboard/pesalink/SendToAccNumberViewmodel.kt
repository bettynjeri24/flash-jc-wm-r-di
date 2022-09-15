package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi

import com.ekenya.rnd.tijara.network.model.BankList
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.STAccountDTO
import com.ekenya.rnd.tijara.requestDTO.SavingAccDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class SendToAccNumberViewmodel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _accountCode = MutableLiveData<Int?>()
    val accountCode: MutableLiveData<Int?>
        get() = _accountCode
    private var _loadingAccount = MutableLiveData<Boolean?>()
    val loadingAccount: MutableLiveData<Boolean?>
        get() = _loadingAccount
    private var _statusCommit = MutableLiveData<Int?>()
    val statusCommit: MutableLiveData<Int?>
        get() = _statusCommit
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties
    private var _bankListProperties = MutableLiveData<List<BankList>>()
    val bankListProperties: LiveData<List<BankList>>
        get() = _bankListProperties
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _formId = MutableLiveData<Int?>()
    val formId: LiveData<Int?>
        get() = _formId
    fun setAccNumber(value:String){
        _accountNumber.value=value
    }
    private var _accountName = MutableLiveData<String>()
    val accountName: LiveData<String>
        get() = _accountName
    private var _accountNumber = MutableLiveData<String>()
    val accountNumber: LiveData<String>
        get() = _accountNumber
    private var _refId = MutableLiveData<String>()
    val refId: LiveData<String>
        get() = _refId
    private var _charges = MutableLiveData<String>()
    val charges: LiveData<String>
        get() = _charges
    private var _bankName = MutableLiveData<String>()
    val bankName: LiveData<String>
        get() = _bankName
    private var _recName = MutableLiveData<String>()
    val recName: LiveData<String>
        get() = _recName
    private var _amount= MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount
    private var _resAccNo= MutableLiveData<String>()
    val resAccNo: LiveData<String>
        get() = _resAccNo
    fun setAmount(value:String){
        _amount.value=value
    }
    fun setBankName(value:String){
        _bankName.value=value
    }
    fun setRecName(value:String){
        _recName.value=value
    }
    fun setResAccNo(value:String){
        _resAccNo.value=value
    }
    fun setAccountName(value:String){
        _accountName.value=value
    }

    init {
        _statusCode.value=null
        _accountCode.value=null
        _statusCommit.value=null
        _isObserving.value = false
        loadSavingAccountName()
        getBanks()
    }
    fun sendMoneyToAccount(stAccountDTO: STAccountDTO){
        uiScope.launch {
            val accountProperties= SaccoApi.retrofitService.sendToAccount(stAccountDTO)

            try {
                val accountResponse=accountProperties.await()
                if (accountResponse.toString().isNotEmpty()){
                    Timber.d("SEND TO ACCOUNT RESPONSE $accountResponse")
                    if (accountResponse.status==1){
                        _formId.value=accountResponse.data.formId
                        _charges.value=accountResponse.data.charges.toString()
                        _statusCode.value=1
                    }else if (accountResponse.status==0){
                        _statusMessage.value=accountResponse.message
                        _statusCode.value=0
                    }
                }
            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                    _statusCode.value = e.hashCode()

            }

        }
    }
    fun commitToAccount() {
        uiScope.launch {
            _isObserving.value = true
            val statementDTO=StatementDTO()
            statementDTO.formId=_formId.value!!
            val commitRequest= SaccoApi.retrofitService.commitToAccount(statementDTO)
            try {
                val commitResponse=commitRequest.await()
                if (commitResponse.toString().isNotEmpty()){
                    Timber.d("COMMIT  RESPONSE $commitResponse")
                    if (commitResponse.status==1){
                        _refId.value=commitResponse.data.transactionCode
                        _statusCommit.value=commitResponse.status

                    }else if (commitResponse.status==0){
                        _statusCMessage.value=commitResponse.message
                        _statusCommit.value=commitResponse.status
                    }
                }

            }catch (e: Exception){
                _statusCommit.value=e.hashCode()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    private fun getBanks(){
        uiScope.launch {
            val bankProperties= SaccoApi.retrofitService.getBankList()
            try {
                val bankResult=bankProperties.await()
                if (bankResult.toString().isNotEmpty()){
                    Timber.d("BANK RESULTS $bankResult")
                    _responseStatus.value= GeneralResponseStatus.DONE
                    /**for the first item in the list*/
                    _bankListProperties.value=bankResult.data
                }
            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR BANK ${e.message}")
            }
        }

    }
    /**get saving account dropdown items*/
    private fun loadSavingAccountName(){
        _loadingAccount.value=true
        uiScope.launch {
            val savingAccDTO= SavingAccDTO()
            savingAccDTO.isTransactional=1

            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            try {
                val savingAccResult=savingProperties.await()
                if (savingAccResult.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/
                        _savingAccountProperties.value=savingAccResult.data
                        _loadingAccount.value=false
                        _accountCode.value=1
                    }else{
                        _loadingAccount.value=false
                        _accountCode.value=0
                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
                _loadingAccount.value=false
                _accountCode.value=0
            }
        }
    }

    fun stopObserving(){
        _statusCode.value=null
        _statusCommit.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}