package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.customerrequest

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ChequeData
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.StandingOrderData
import com.ekenya.rnd.tijara.requestDTO.*
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MemberShipCardViewModel(application: Application) : AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusEmaile = MutableLiveData<Int?>()
    val statusEmaile: MutableLiveData<Int?>
        get() = _statusEmaile
    private var _emailMessage = MutableLiveData<String>()
    val emailMessage: LiveData<String>
        get() = _emailMessage
    private var _statusATM = MutableLiveData<Int?>()
    val statusATM: MutableLiveData<Int?>
        get() = _statusATM
    private var _atmMessage = MutableLiveData<String>()
    val atmMessage: LiveData<String>
        get() = _atmMessage
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
    var _bankName = MutableLiveData<String>()
    var _payFromName = MutableLiveData<String>()
    var _frequencyName = MutableLiveData<String>()
    var _charges = MutableLiveData<String>()
    var _payFromId = MutableLiveData<String>()

    fun setBankName(value: String){
        _bankName.value=value
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

    private var _chequeBranch = MutableLiveData<List<ChequeData>>()
    val chequeBranch: LiveData<List<ChequeData>?>
        get() = _chequeBranch
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        _statusCode.value=null
        _statusCommit.value=null
        _statusEmaile.value=null
        _statusATM.value=null
        _status.value=null
        loadChequeData()
        loadSavingAccount()
    }
    fun stopObserving(){
        _statusCode.value=null
        _statusEmaile.value=null
        _statusCommit.value=null
        _statusATM.value=null
        _commitCheque.value=null
        _status.value=null
        _isObserving.value = false
    }
    fun createMemberCard(mCardDTO: MCardDTO){
        uiScope.launch {
            val createRequest= SaccoApi.retrofitService.createMemCardAsync(mCardDTO)
            try {
                val creatrResponse=createRequest.await()
                if (creatrResponse.toString().isNotEmpty()){
                    when (creatrResponse.status) {
                        1 -> {
                            _charges.value= creatrResponse.data.charges.toString()
                            _formId.value= creatrResponse.data.formId
                            _statusCode.value=creatrResponse.status
                        }
                        0 -> {
                            _statusMessage.value=creatrResponse.message
                            _statusCode.value=0
                        }
                    }

                }
            }catch (e: Exception){
                _statusCode.value = e.hashCode()

            }
        }

    }
    fun commitMemCard(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId= _formId.value!!
            val commitRequest= SaccoApi.retrofitService.commitCard(statementDTO)

            try {
                val commitResponse=commitRequest.await()
                if (commitResponse.toString().isNotEmpty()){
                    if (commitResponse.status==1){
                        _commitCheque.value=1

                    }else if (commitResponse.status==0){
                        _commiitCheMessage.value=commitResponse.message
                        _commitCheque.value=commitResponse.status
                    }
                }
            }catch (e: Exception){
                Log.e("ERROR COMIT ACC"," ${e.message}")
                Log.e("ERROR COMIT ACC"," ${e.hashCode()}")
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
    fun createComplaint(complaintDTO: ComplaintDTO){
        uiScope.launch {
            val createRequest= SaccoApi.retrofitService.createComplainAsync(complaintDTO)
            try {
                val creatrResponse=createRequest.await()
                if (creatrResponse.toString().isNotEmpty()){
                    when (creatrResponse.status) {
                        1 -> {
                            _status.value=creatrResponse.status
                        }
                        0 -> {
                            _statusCMessage.value=creatrResponse.message
                            _status.value=0
                        }
                    }

                }
            }catch (e: Exception){
                _status.value = e.hashCode()

            }
        }

    }
    fun updateEmail(emailDTO: EmailDTO){
        uiScope.launch {
            val createRequest= SaccoApi.retrofitService.updateEmailAsync(emailDTO)
            try {
                val creatrResponse=createRequest.await()
                if (creatrResponse.toString().isNotEmpty()){
                    when (creatrResponse.status) {
                        1 -> {
                            _statusEmaile.value=creatrResponse.status
                        }
                        0 -> {
                            _emailMessage.value=creatrResponse.message
                            _statusEmaile.value=0
                        }
                    }

                }
            }catch (e: Exception){
                _statusEmaile.value = e.hashCode()

            }
        }

    }
    private fun loadSavingAccount(){
        uiScope.launch {
            val savingAccDTO=SavingAccDTO()
            savingAccDTO.isTransactional=1
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            try {
                val savingAccResult=savingProperties.await()
                if (savingAccResult.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/
                        _savingPrimeProperties.value=savingAccResult.data


                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }
    fun createATM(atmdto: ATMDTO){
        uiScope.launch {
            val createRequest= SaccoApi.retrofitService.createATM(atmdto)
            try {
                val creatrResponse=createRequest.await()
                if (creatrResponse.toString().isNotEmpty()){
                    when (creatrResponse.status) {
                        1 -> {
                            _charges.value= creatrResponse.data.charges.toString()
                            _formId.value= creatrResponse.data.formId
                            _statusATM.value=creatrResponse.status
                        }
                        0 -> {
                            _atmMessage.value=creatrResponse.message
                            _statusATM.value=0
                        }
                    }

                }
            }catch (e: Exception){
                _statusATM.value = e.hashCode()

            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}