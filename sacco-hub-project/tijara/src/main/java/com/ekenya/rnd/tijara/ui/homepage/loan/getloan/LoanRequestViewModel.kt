package com.ekenya.rnd.tijara.ui.homepage.loan.getloan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.TempGuarantor
import com.ekenya.rnd.tijara.requestDTO.*
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class LoanRequestViewModel(application:Application) :AndroidViewModel(application) {
    private var _applyStatusCode = MutableLiveData<Int?>()
    val applyStatusCode: MutableLiveData<Int?>
        get() = _applyStatusCode
    private var _topupStatusCode = MutableLiveData<Int?>()
    val topupStatusCode: MutableLiveData<Int?>
        get() = _topupStatusCode
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _savingStatus = MutableLiveData<GeneralResponseStatus>()
    val savingStatus: LiveData<GeneralResponseStatus>
        get() = _savingStatus

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _removeMessage = MutableLiveData<String>()
    val removeMessage: LiveData<String>
        get() = _removeMessage
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties

    private var _tempGuarantors = MutableLiveData<List<TempGuarantor>>()
    val tempGuarantors: LiveData<List<TempGuarantor>?>
        get() = _tempGuarantors
    private var _statusCommit = MutableLiveData<Int?>()
    val statusCommit: MutableLiveData<Int?>
        get() = _statusCommit
    private var _statusGCommit = MutableLiveData<Int?>()
    val statusGCommit: MutableLiveData<Int?>
        get() = _statusGCommit
    private var _statusGMessage = MutableLiveData<String>()
    val statusGMessage: LiveData<String>
        get() = _statusGMessage
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status

    private var _statusRemove = MutableLiveData<Int?>()
    val statusRemove: MutableLiveData<Int?>
        get() = _statusRemove
    private var _deleteGuanator = MutableLiveData<Int?>()
    val deleteGuanator: MutableLiveData<Int?>
        get() = _deleteGuanator
    private var _commitMessage = MutableLiveData<String>()
    val commitMessage: LiveData<String>
        get() = _commitMessage
    private var _updateMessage = MutableLiveData<String>()
    val updateMessage: LiveData<String>
        get() = _updateMessage
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusUpdate = MutableLiveData<Int?>()
    val statusUpdate: MutableLiveData<Int?>
        get() = _statusUpdate
    private var _authSuccess = MutableLiveData<Boolean>()
    val authSuccess: LiveData<Boolean>
        get() = _authSuccess
    var accountNumber=MutableLiveData<String>()
    var accountName=MutableLiveData<String>()
    var formId=MutableLiveData<String>()
    var charges=MutableLiveData<String>()
    var refCode=MutableLiveData<String>()
    var amount=MutableLiveData<String>()
    var gFormId=MutableLiveData<String>()
    var gMemberName=MutableLiveData<String>()

    init {
        _applyStatusCode.value=null
        _statusCommit.value=null
        _statusGCommit.value=null
        _statusUpdate.value=null
        _status.value=null
        _statusRemove.value=null
        _statusCode.value=null
        loadSavingAccountName()

    }
    fun stopObserving(){
        _applyStatusCode.value=null
        _statusCommit.value=null
        _statusGCommit.value=null
        _statusUpdate.value=null
        _status.value=null
        _statusRemove.value=null
        _statusCode.value=null
    }
    fun applyLoan(loanRequestDTO: LoanRequestDTO){
        uiScope.launch {
            val applyLoanDetails= SaccoApi.retrofitService.applyLoanPreview(loanRequestDTO)
            try {
                val applyloanResponse=applyLoanDetails.await()
                if (applyloanResponse.status==1){
                    formId.value= applyloanResponse.data.formId.toString()
                    charges.value= applyloanResponse.data.charges.toString()
                    _applyStatusCode.value=applyloanResponse.status
                }else if (applyloanResponse.status==0){
                    _statusMessage.value=applyloanResponse.message
                    _applyStatusCode.value=applyloanResponse.status
                }
            }catch (e: HttpException){
                _applyStatusCode.value=e.code()
                Timber.e("ERROR ${e.message}")
            }
        }
    }
    fun loanApplyCommit(){
        uiScope.launch {
            val statementDTO= StatementDTO()
            statementDTO.formId=formId.value!!.toInt()
            val loanRequest= SaccoApi.retrofitService.applyLoanCommit(statementDTO)

            try {
                val loanResponse=loanRequest.await()
                    if (loanResponse.status==1){
                       refCode.value=loanResponse.data.transactionCode
                        _statusCommit.value=1
                        //

                    }else if (loanResponse.status==0){
                        _statusCMessage.value=loanResponse.message
                        _statusCommit.value=0
                    }
            }catch (e: HttpException){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _statusCommit.value=e.code()
            }

        }
    }

    /**get saving account dropdown items*/
    private fun loadSavingAccountName(){
        uiScope.launch {
            _savingStatus.value=GeneralResponseStatus.LOADING
            val savingAccDTO = SavingAccDTO()
            savingAccDTO.isTransactional = 1
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            try {
                val savingAccResult=savingProperties.await()
                    if(savingAccResult.status==1){
                        _savingStatus.value=GeneralResponseStatus.DONE
                        /**for the first item in the list*/
                        _savingAccountProperties.value=savingAccResult.data
                    }else{
                        _savingStatus.value=GeneralResponseStatus.DONE
                    }

            }catch (e: Exception){
                _savingStatus.value=GeneralResponseStatus.DONE
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }

    fun addGuarantors(adGuarantorDTO: AdGuarantorDTO){
        uiScope.launch {
            val guarontorsProperties= SaccoApi.retrofitService.addGuarantorPreveiw(adGuarantorDTO)
            try {
                val guarResponse=guarontorsProperties.await()
                    if (guarResponse.status==1){
                        Timber.d("Guarantors RESPONSE $guarResponse")
                        gFormId.value= guarResponse.data.formId.toString()
                        gMemberName.value=guarResponse.data.memberName
                        _status.value=1

                    }else{
                        _commitMessage.value=guarResponse.message
                        _status.value=0
                }

            }catch (e: HttpException){
                _status.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun addGuarantorCommit(){
        uiScope.launch {
            val statementDTO= StatementDTO()
            statementDTO.formId=gFormId.value!!.toInt()
            val guarontorsRequest= SaccoApi.retrofitService.addGuarantorCommit(statementDTO)
            try {
                val guarantorsResponse=guarontorsRequest.await()
                    if (guarantorsResponse.status==1){
                        Timber.d("RESPONSE $guarantorsResponse")
                        _statusGCommit.value=guarantorsResponse.status

                    }else{
                        _statusGMessage.value=guarantorsResponse.message
                        _statusGCommit.value=0
                }

            }catch (e: HttpException){
                _statusGCommit.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun getTempGuarantors(loanDetailsDTO: LoanDetailsDTO){
        uiScope.launch {
            val guarontorsProperties= SaccoApi.retrofitService.getTempGuarantors(loanDetailsDTO)
            try {
                val guarResponse=guarontorsProperties.await()
                if (guarResponse.toString().isNotEmpty()){
                    Timber.d("LOANS RESPONSE $guarResponse")
                    if (guarResponse.status==1){
                        _statusCode.value=guarResponse.status
                        _tempGuarantors.value= guarResponse.data
                    }
                }

            }catch (e: Exception){
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun removeTempGuarantors(remGuranDTO: RemGuranDTO){
        uiScope.launch {
            val remGuarantor= SaccoApi.retrofitService.removeTempGuarantor(remGuranDTO)
            try {
                val remGuarResponse=remGuarantor.await()
                if (remGuarResponse.toString().isNotEmpty()){

                    if (remGuarResponse.status==1){
                        Timber.d("Guarantors RESPONSE $remGuarResponse")
                        _statusRemove.value=remGuarResponse.status

                    }else if(remGuarResponse.status==0){
                        _removeMessage.value=remGuarResponse.message
                        _statusRemove.value=0
                    }
                }

            }catch (e: HttpException){
                _statusRemove.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun deleteTempGuarantor(loanDetailsDTO: LoanDetailsDTO){
        uiScope.launch {
            val remGuarantor= SaccoApi.retrofitService.deleteTempGuarantor(loanDetailsDTO)
            try {
                val remGuarResponse=remGuarantor.await()
                if (remGuarResponse.toString().isNotEmpty()){

                    if (remGuarResponse.status==1){
                        Timber.d("Guarantors RESPONSE $remGuarResponse")
                        _deleteGuanator.value=remGuarResponse.status

                    }else if(remGuarResponse.status==0){
                        _removeMessage.value=remGuarResponse.message
                        _deleteGuanator.value=0
                    }
                }

            }catch (e: HttpException){
                _statusRemove.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun updateTempGuarantors(updateGuraDTO: UpdateGuraDTO){
        uiScope.launch {
            val updateRequest= SaccoApi.retrofitService.updateTempGuarantor(updateGuraDTO)
            try {
                val updateResponse=updateRequest.await()
                if (updateResponse.toString().isNotEmpty()){

                    if (updateResponse.status==1){
                        Timber.d("Guarantors RESPONSE $updateResponse")
                        _statusUpdate.value=updateResponse.status

                    }else if(updateResponse.status==0){
                        _updateMessage.value=updateResponse.message
                        _statusUpdate.value=0
                    }
                }

            }catch (e: HttpException){
                _statusRemove.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
}