package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ChequeData
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.StandingOrderData
import com.ekenya.rnd.tijara.network.model.StandingOrderFrequenyData
import com.ekenya.rnd.tijara.requestDTO.ChequeDTO
import com.ekenya.rnd.tijara.requestDTO.SavingAccDTO
import com.ekenya.rnd.tijara.requestDTO.StandingOrderDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class StandindOrderViewModel (application:Application) : AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _accountCode = MutableLiveData<Int?>()
    val accountCode: MutableLiveData<Int?>
        get() = _accountCode
    private var _loadingAccount = MutableLiveData<Boolean?>()
    val loadingAccount: MutableLiveData<Boolean?>
        get() = _loadingAccount
    private var _statusCheque = MutableLiveData<Int?>()
    val statusCheque: MutableLiveData<Int?>
        get() = _statusCheque
    private var _commitCheque = MutableLiveData<Int?>()
    val commitCheque: MutableLiveData<Int?>
        get() = _commitCheque
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _commiitCheMessage = MutableLiveData<String>()
    val commiitCheMessage: LiveData<String>
        get() = _commiitCheMessage
    private var _chequeMessage = MutableLiveData<String>()
    val chequeMessage: LiveData<String>
        get() = _chequeMessage
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusCommit = MutableLiveData<Int?>()
    val statusCommit: MutableLiveData<Int?>
        get() = _statusCommit
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _standingOrderProperties = MutableLiveData<List<StandingOrderData>>()
    val standingOrderProperties: LiveData<List<StandingOrderData>>
        get() = _standingOrderProperties
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _amount = MutableLiveData<String>()
    val amount: LiveData<String>
        get() = _amount
     var _standingOrderName = MutableLiveData<String?>()
     private var _bankName = MutableLiveData<String>()
      val bankName :LiveData<String>
        get() = _bankName
     var _payFromName = MutableLiveData<String>()
    private var _frequencyName = MutableLiveData<String>()
     val frequencyName: LiveData<String>
        get() = _frequencyName
     var _charges = MutableLiveData<String>()
     var _payFromId = MutableLiveData<String>()
    private var _accountNumberS = MutableLiveData<String>()
    val accountNumberS: LiveData<String>
        get() = _accountNumberS

    fun setBankName(value: String){
        _bankName.value=value
    }
    fun setAmount(value: String){
        _amount.value=value
    }
    fun setAccNumberStandingOrder(value:String){
        _accountNumberS.value=value
    }
    fun setStandingOrderName(value: String){
        _standingOrderName.value=value
    }
    fun setPayFromName(value: String){
        _payFromName.value=value
    }
    fun setFrequency(value: String){
       _frequencyName.value=value
    }
    fun setPayFromId(value: String){
        _payFromId.value=value
    }
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _formId = MutableLiveData<Int>()
    val formId: LiveData<Int>
        get() = _formId
    private var _chequeFormId = MutableLiveData<Int>()
    val chequeFormId: LiveData<Int>
        get() = _chequeFormId
     var _payee = MutableLiveData<String>()
     var _date = MutableLiveData<String>()
    private var _refId = MutableLiveData<String>()

    val refId: LiveData<String>
        get() = _refId
    fun setRefId(value:String){
        _refId.value=value
    }
    fun setDate(value:String){
        _date.value=value
    }
    fun setPayee(value:String){
        _payee.value=value
    }
    private var _savingPrimeProperties = MutableLiveData<List<SavingAccountData>>()
    val savingPrimeProperties: LiveData<List<SavingAccountData>?>
        get() = _savingPrimeProperties
    private var _frequency = MutableLiveData<List<StandingOrderFrequenyData>>()
    val frequency: LiveData<List<StandingOrderFrequenyData>?>
        get() = _frequency
    private var _chequeBranch = MutableLiveData<List<ChequeData>>()
    val chequeBranch: LiveData<List<ChequeData>?>
        get() = _chequeBranch
    private var _savingAlphaProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAlphaProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAlphaProperties
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        _statusCode.value=null
        _statusCommit.value=null
        _statusCheque.value=null
        _accountCode.value=null
        _loadingAccount.value=null
        _status.value=null
        loadStandingOrderAccount()
        loadFrequency()
        loadPrimeSavingAccount()
        loadAlphaSavingAccount()
        loadChequeData()
    }
    fun stopObserving(){
        _statusCode.value=null
        _statusCheque.value=null
        _statusCommit.value=null
        _commitCheque.value=null
        _accountCode.value=null
        _loadingAccount.value=null
        _status.value=null
        _isObserving.value = false
    }
    /**get saving account dropdown items*/
    private fun loadStandingOrderAccount(){
        _standingOrderProperties.value= arrayListOf()
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val savingProperties= SaccoApi.retrofitService.getStandingOrderAccAsync()
            try {
                val savingAccResult=savingProperties.await()
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        if (savingAccResult.data.isNullOrEmpty()){
                            _standingOrderProperties.value= arrayListOf()
                            Log.d("TAG","CHECK !")
                            _isEmpty.value=true

                        }else{
                            _standingOrderProperties.value=savingAccResult.data
                            _isEmpty.value=false
                        }
                        _status.value=savingAccResult.status
                    }else if (savingAccResult.status==0){
                        _status.value=savingAccResult.status
                        _responseStatus.value= GeneralResponseStatus.DONE

                    }

            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.DONE

                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    /**get saving account dropdown items*/
    private fun loadPrimeSavingAccount(){
        _loadingAccount.value=true
        uiScope.launch {
            val savingAccDTO= SavingAccDTO()
            savingAccDTO.isTransactional=1
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            Timber.d("Saving Account$savingAccDTO")
            try {
                val savingAccResult=savingProperties.await()
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
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/

                        _savingAlphaProperties.value=savingAccResult.data


                    }

            }catch (e:Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    private fun loadFrequency(){
        uiScope.launch {
            val frequencyRequest= SaccoApi.retrofitService.getStandingOrderFrequencyAccAsync()
            try {
                val frequencyResponse=frequencyRequest.await()
                    if(frequencyResponse.status==1){
                        /**for the first item in the list*/
                        _frequency.value=frequencyResponse.data
                    }

            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    fun createStandingOrder(standingOrderDTO: StandingOrderDTO){
        uiScope.launch {
            val createStOrder= SaccoApi.retrofitService.createStandingOrderAsync(standingOrderDTO)
            try {
                val createStOrderResponse=createStOrder.await()
                    when (createStOrderResponse.status) {
                        1 -> {
                            _charges.value= createStOrderResponse.data.charges.toString()
                         _formId.value= createStOrderResponse.data.formId
                            _statusCode.value=createStOrderResponse.status
                        }
                        0 -> {
                            _statusMessage.value=createStOrderResponse.message
                            _statusCode.value=0
                        }


                }
            }catch (e: Exception){
                _status.value = e.hashCode()

            }
        }

    }
    fun commitStandingOrder(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId= _formId.value!!
            val commitRequest= SaccoApi.retrofitService.commitStandingOrderAsync(statementDTO)

            try {
                val commitResponse=commitRequest.await()
                    if (commitResponse.status==1){
                        _statusCommit.value=1

                        _refId.value=commitResponse.data.transactionCode

                    }else if (commitResponse.status==0){
                        _statusCMessage.value=commitResponse.message
                        _statusCommit.value=commitResponse.status
                    }

            }catch (e: Exception){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _statusCommit.value=e.hashCode()
            }

        }
    }
    fun createChequePreview(chequeDTO: ChequeDTO){
        uiScope.launch {
            val chequeRequest= SaccoApi.retrofitService.createChequePreviewAsync(chequeDTO)
            try {
                val chequeResponse=chequeRequest.await()
                    when (chequeResponse.status) {
                        1 -> {
                            Constants.CHARGES = chequeResponse.data.charges
                            _chequeFormId.value= chequeResponse.data.formId
                            _statusCheque.value=chequeResponse.status
                        }
                        0 -> {
                            _chequeMessage.value=chequeResponse.message
                            _statusCheque.value=0
                        }
                    }


            }catch (e: Exception){
                _status.value = e.hashCode()

            }
        }

    }
    fun commitCheque(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId= _chequeFormId.value!!
            val commitRequest= SaccoApi.retrofitService.commitCard(statementDTO)

            try {
                val commitResponse=commitRequest.await()
                    if (commitResponse.status==1){
                        _commitCheque.value=1
                    }else if (commitResponse.status==0){
                        _commiitCheMessage.value=commitResponse.message
                        _commitCheque.value=commitResponse.status
                    }

            }catch (e: Exception){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _commitCheque.value=e.hashCode()
            }

        }
    }
    private fun loadChequeData(){
        uiScope.launch {
            val cheRequest= SaccoApi.retrofitService.getChequeBranches()
            try {
                val chequeBResponse=cheRequest.await()
                    if(chequeBResponse.status==1){
                        /**for the first item in the list*/
                        _chequeBranch.value=chequeBResponse.data


                    }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}