package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.pesalink

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.PesalinkBank
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.PesalinkPhoneCheckDTO
import com.ekenya.rnd.tijara.requestDTO.STPhoneDTO
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

class SendToPhoneViewmodel @Inject constructor(application: Application) : AndroidViewModel(application) {
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

    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _bankName = MutableLiveData<String>()
    val bankName: LiveData<String>
        get() = _bankName
    private var _amount = MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount
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
    private var _charges = MutableLiveData<Int>()
    val charges: LiveData<Int>
        get() = _charges
    fun setBankName(value:String){
        _bankName.value=value
    }
    fun setAmount(value: String){
        _amount.value=value
    }
    fun setAccountName(value:String){
        _accountName.value=value
    }
    private var _pesalinkBankProperties = MutableLiveData<List<PesalinkBank>>()
    val pesalinkBankProperties: LiveData<List<PesalinkBank>?>
        get() = _pesalinkBankProperties
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        _statusCode.value=null
        _statusCommit.value=null
        _loadingAccount.value=null
        _accountCode.value=null
        _isObserving.value = false
        loadSavingAccountName()

    }
    fun sendMoneyToPhone(stPhoneDTO: STPhoneDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val phoneProperties= SaccoApi.retrofitService.sendToPhone(stPhoneDTO)

            try {
                val phoneResponse=phoneProperties.await()
                if (phoneResponse.toString().isNotEmpty()){
                    Timber.d("SEND TO PHONE RESPONSE $phoneResponse")
                    if (phoneResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _formId.value=phoneResponse.data.formId
                        _charges.value=phoneResponse.data.charges
                        _statusCode.value=1
                    }else if (phoneResponse.status==0){
                        _statusMessage.value=phoneResponse.message
                        _statusCode.value=0
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

            }catch (e: HttpException){
                _statusCommit.value=e.code()
                Timber.e("ERROR Exception${e.message}")
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
                _loadingAccount.value=null
                _accountCode.value=0
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    fun loadPesalinkBankName(pesalinkPhoneCheckDTO:PesalinkPhoneCheckDTO){
        uiScope.launch {
            val pesalinkBProperties= SaccoApi.retrofitService.pesalinkPhoneCheck(pesalinkPhoneCheckDTO)
            try {
                val pesalinkBResult=pesalinkBProperties.await()
                if (pesalinkBResult.toString().isNotEmpty()){
                    Timber.d("PESALINK RESULTS $pesalinkBResult")
                    if(pesalinkBResult.status==1){
                        _pesalinkBankProperties.value=pesalinkBResult.data.banks
                    }
                }
            }catch (e: HttpException){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }


    fun stopObserving(){
        _statusCode.value=null
        _accountCode.value=null
        _loadingAccount.value=null
        _statusCommit.value=null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}