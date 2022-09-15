package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.buyairtime

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.AirtimeData
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.requestDTO.BuyAirtimeDTO
import com.ekenya.rnd.tijara.requestDTO.MakeDepositDTO
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

class BuyAirtimeViewmodel @Inject constructor(application: Application) : AndroidViewModel(application) {
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
    private var _depositStatusCode = MutableLiveData<Int?>()
    val depositStatusCode: MutableLiveData<Int?>
        get() = _depositStatusCode
    private var _depositCommitCode = MutableLiveData<Int?>()
    val depositCommitCode: MutableLiveData<Int?>
        get() = _depositCommitCode
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _airtimeResponse = MutableLiveData<AirtimeData>()
    val airtimeResponse: LiveData<AirtimeData>
        get() = _airtimeResponse
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>>
        get() = _savingAccountProperties
    private var _sProviderProperties = MutableLiveData<List<ServiceProviderItem>>()
    val sProviderProperties: LiveData<List<ServiceProviderItem>>
        get() = _sProviderProperties
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _savingPrimeProperties = MutableLiveData<List<SavingAccountData>>()
    val savingPrimeProperties: LiveData<List<SavingAccountData>?>
        get() = _savingPrimeProperties
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
    private var _duty = MutableLiveData<Int>()
    val duty: LiveData<Int>
        get() = _duty
    private var _amount= MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount
    private var _phone= MutableLiveData<String>()
    val phone: LiveData<String>
        get() = _phone
    fun setAmount(value:String){
        _amount.value=value
    }
    fun setPhone(value:String){
        _phone.value=value
    }
    fun setAccountName(value:String){
        _accountName.value=value
    }

    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _depositCommitCode.value=null
        _depositStatusCode.value=null
        _status.value=null
        _isObserving.value = false
        _loadingAccount.value=null
        _accountCode.value=null
        loadSavingAccountName()
        loadServiceProvider()
        loadPrimeSavingAccount()
    }
    fun buyAirtimePreview(buyAirtimeDTO: BuyAirtimeDTO){
        _loadingAccount.value=true
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val airtimeProperties= SaccoApi.retrofitService.buyAirtimePreview(buyAirtimeDTO)

            try {
                val airtimeResponse=airtimeProperties.await()
                if (airtimeResponse.toString().isNotEmpty()){
                    Timber.d("AIRTIME RESPONSE $airtimeResponse")
                    if (airtimeResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _formId.value=airtimeResponse.data.formId
                        _duty.value= airtimeResponse.data.exerciseDuty
                        _charges.value=airtimeResponse.data.charges
                        _airtimeResponse.value=airtimeResponse.data
                        _statusCode.value=airtimeResponse.status
                    }else if (airtimeResponse.status==0){
                        _statusMessage.value=airtimeResponse.message
                        _statusCode.value=0
                    }
                }
            }catch (e: HttpException){
                Timber.e("ERROR ${e.message}")
                _statusCode.value=e.code()
            }

        }
    }
    fun airtimeCommit(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId= _formId.value!!
            val airtimeRequest= SaccoApi.retrofitService.airtimeCommit(statementDTO)

            try {
                val airtimeCResponse=airtimeRequest.await()
                if (airtimeCResponse.toString().isNotEmpty()){
                    Timber.d("AIRTIME COMMIT RESPONSE $airtimeCResponse")
                    if (airtimeCResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _refId.value=airtimeCResponse.data.transactionCode
                        _status.value=1

                    }else if (airtimeCResponse.status==0){
                        _statusCMessage.value=airtimeCResponse.message
                        _status.value=0

                    }
                }
            }catch (e: HttpException){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _status.value=e.code()
            }

        }
    }
    /**make deposit*/
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
                        _charges.value=depositResults.data.charges
                        _duty.value=depositResults.data.exerciseDuty
                        _formId.value= depositResults.data.formId
                        _depositStatusCode.value=depositResults.status
                    }else if (depositResults.status==0){
                        _statusMessage.value=depositResults.message
                        _depositStatusCode.value=depositResults.status
                    }
                }
            } catch (e: Exception) {
                _responseStatus.value = GeneralResponseStatus.ERROR
                _depositStatusCode.value=e.hashCode()
                Timber.e("ERROR ${e.message.toString()}")
            }
        }
    }
    fun commitDeposit(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId= _formId.value!!
            val airtimeRequest= SaccoApi.retrofitService.commitDeposit(statementDTO)

            try {
                val airtimeCResponse=airtimeRequest.await()
                if (airtimeCResponse.toString().isNotEmpty()){
                    Timber.d("AIRTIME COMMIT RESPONSE $airtimeCResponse")
                    if (airtimeCResponse.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _refId.value=airtimeCResponse.data.transactionCode
                        _depositCommitCode.value=1

                    }else if (airtimeCResponse.status==0){
                        _statusCMessage.value=airtimeCResponse.message
                        _depositCommitCode.value=airtimeCResponse.status
                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _depositCommitCode.value=e.hashCode()
            }

        }
    }


    /**get saving account dropdown items*/
    private fun loadSavingAccountName(){
        _loadingAccount.value=true

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
    /**get transactional account dropdown items*/
    private fun loadPrimeSavingAccount(){
        _loadingAccount.value=true
        uiScope.launch {
            val savingAccDTO= SavingAccDTO()
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
                        _loadingAccount.value=false
                        _accountCode.value=1
                    }else {
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
        _depositCommitCode.value=null
        _depositStatusCode.value=null
        _status.value=null
        _isObserving.value = false
      //  _loadingAccount.value=null
      //  _accountCode.value=null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}