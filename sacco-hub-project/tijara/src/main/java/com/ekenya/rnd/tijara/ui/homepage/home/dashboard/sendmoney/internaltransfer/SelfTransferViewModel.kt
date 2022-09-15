package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.sendmoney.internaltransfer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.requestDTO.InternalTDTO
import com.ekenya.rnd.tijara.requestDTO.MemberTDTO
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

class SelfTransferViewModel @Inject constructor (application: Application) : AndroidViewModel(application) {
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
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties
    private var _savingAlphaProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAlphaProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAlphaProperties
    private var _savingPrimeProperties = MutableLiveData<List<SavingAccountData>>()
    val savingPrimeProperties: LiveData<List<SavingAccountData>?>
        get() = _savingPrimeProperties
    private var _sProviderProperties = MutableLiveData<List<ServiceProviderItem>>()
    val sProviderProperties: LiveData<List<ServiceProviderItem>?>
        get() = _sProviderProperties
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _formId = MutableLiveData<Int?>()
    val formId: LiveData<Int?>
        get() = _formId
    fun setAccNumber(value:String){
        _accountNumber.value=value
    }
    fun setAccNumberTo(value:String){
        _accountNumberTo.value=value
    }
    private var _accountName = MutableLiveData<String>()
    val accountName: LiveData<String>
        get() = _accountName
    private var _accountNameTo = MutableLiveData<String>()
    val accountNameTo: LiveData<String>
        get() = _accountNameTo
    private var _accountNumber = MutableLiveData<String>()
    val accountNumber: LiveData<String>
        get() = _accountNumber
    private var _accountNumberTo = MutableLiveData<String>()
    val accountNumberTo: LiveData<String>
        get() = _accountNumberTo
    private var _refId = MutableLiveData<String>()
    val refId: LiveData<String>
        get() = _refId
    private var _charges = MutableLiveData<String>()
    val charges: LiveData<String>
        get() = _charges
    private var _amount= MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount
    private var _resAccNo= MutableLiveData<String>()
    val resAccNo: LiveData<String>
        get() = _resAccNo
    private var _recName = MutableLiveData<String>()
    val recName: LiveData<String>
        get() = _recName

    fun setAmount(value:String){
        _amount.value=value
    }
    fun setAccountName(value:String){
        _accountName.value=value
    }
    fun setAccountNameTo(value:String){
        _accountNameTo.value=value
    }

    init {
        _statusCode.value=null
        _accountCode.value=null
        _loadingAccount.value=null
        _statusCommit.value=null
        loadPrimeSavingAccount()
        loadAlphaSavingAccount()
    }
    fun stopObserving(){
        _statusCode.value=null
        _statusCommit.value=null
        _isObserving.value = false
    }

    fun selfTransferPreview(internalTDTO: InternalTDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val selfRequest= SaccoApi.retrofitService.selfTransfer(internalTDTO)

            try {
                val selfResponse=selfRequest.await()
                if (selfResponse.toString().isNotEmpty()){
                    Timber.d(" RESPONSE $selfResponse")
                    if (selfResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _formId.value=selfResponse.data.formId
                        _charges.value= selfResponse.data.charges.toString()
                        _statusCode.value=1
                    }else if (selfResponse.status==0){
                        _statusMessage.value=selfResponse.message
                        _statusCode.value=selfResponse.status
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
    fun commitSelfTransfer() {
        uiScope.launch {
            _isObserving.value = true
            val statementDTO=StatementDTO()
            statementDTO.formId=_formId.value!!
            val commitRequest= SaccoApi.retrofitService.commitSelfTransfer(statementDTO)
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
    fun memberTransferPreview(memberTDTO: MemberTDTO){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val memberRequest= SaccoApi.retrofitService.memberTransfer(memberTDTO)

            try {
                val memberResponse=memberRequest.await()
                if (memberResponse.toString().isNotEmpty()){
                    Timber.d(" RESPONSE $memberResponse")
                    if (memberResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _formId.value=memberResponse.data.formId
                        _charges.value=memberResponse.data.charges.toString()
                        _resAccNo.value=memberResponse.data.toAccountNumber
                        _recName.value=memberResponse.data.toClientName
                        _statusCode.value=1
                    }else if (memberResponse.status==0){
                        _statusMessage.value=memberResponse.message
                        _statusCode.value=memberResponse.status
                    }
                }
            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                _statusCode.value = e.hashCode()

            }

        }
    }
    fun commitMemberTransfer() {
        uiScope.launch {
            _isObserving.value = true
            val statementDTO=StatementDTO()
            statementDTO.formId=_formId.value!!
            val commitRequest= SaccoApi.retrofitService.commitSelfTransfer(statementDTO)
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
    private fun loadPrimeSavingAccount(){
        _loadingAccount.value=true
        uiScope.launch {
            val savingAccDTO=SavingAccDTO()
            savingAccDTO.isTransactional=1
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            Timber.d("Saving Account$savingAccDTO")
            try {
                val savingAccResult=savingProperties.await()
                if (savingAccResult.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/
                        _savingPrimeProperties.value=savingAccResult.data
                        _accountCode.value=1
                        _loadingAccount.value=false

                    }else{
                        _loadingAccount.value=false
                        _accountCode.value=0

                    }
                }
            }catch (e: Exception){
                _loadingAccount.value=false
                _accountCode.value=0

                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    private fun loadAlphaSavingAccount(){
        uiScope.launch {
            val savingAccDTO=SavingAccDTO()
            savingAccDTO.isTransactional=0
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            Timber.d("Saving Account$savingAccDTO")
            try {
                val savingAccResult=savingProperties.await()
                if (savingAccResult.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $savingAccResult")
                        /**for the first item in the list*/
                        _savingAlphaProperties.value=savingAccResult.data

                }
            }catch (e:Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}