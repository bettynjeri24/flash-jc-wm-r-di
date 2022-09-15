package com.ekenya.rnd.tijara.ui.homepage.loan.payloan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.network.model.ServiceProviderItem
import com.ekenya.rnd.tijara.requestDTO.PayFrmAccDTO
import com.ekenya.rnd.tijara.requestDTO.PayLoanDTO
import com.ekenya.rnd.tijara.requestDTO.SavingAccDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class LoanRepaymentViewModel (application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode

    private var _savingPStatus = MutableLiveData<GeneralResponseStatus>()
    val savingPStatus: LiveData<GeneralResponseStatus>
        get() = _savingPStatus
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _statusPay = MutableLiveData<Int?>()
    val statusPay: MutableLiveData<Int?>
        get() = _statusPay
    private var _messagePay = MutableLiveData<String>()
    val messagePay: LiveData<String>
        get() = _messagePay
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties
    private var _sProviderProperties = MutableLiveData<List<ServiceProviderItem>>()
    val sProviderProperties: LiveData<List<ServiceProviderItem>?>
        get() = _sProviderProperties
    private val viewModelJob = Job()
    var accountNumber=MutableLiveData<String>()
    var accountName=MutableLiveData<String>()
    var formId=MutableLiveData<String>()
    var charges=MutableLiveData<String>()
    var refCode=MutableLiveData<String>()
    var amountValue=MutableLiveData<String>()
    var phone=MutableLiveData<String>()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusPay.value = null
        _statusCode.value=null
        loadServiceProvider()
        loadSavingAccountName()
    }
    fun stopObserving() {
        _statusCode.value = null
        _statusPay.value = null

    }
    fun payFromAccount(payFrmAccDTO: PayFrmAccDTO){
        uiScope.launch {
            val repayLoanDetails= SaccoApi.retrofitService.payLoanAcc(payFrmAccDTO)
            try {
                val repayLoanResponse=repayLoanDetails.await()
                if (repayLoanResponse.status==1){
                    formId.value= repayLoanResponse.data.formId.toString()
                    charges.value=repayLoanResponse.data.charges.toString()
                    _statusCode.value=repayLoanResponse.status
                }else if (repayLoanResponse.status==0){
                    _statusMessage.value=repayLoanResponse.message
                    _statusCode.value=0
                }
            }catch (e: HttpException){
                _statusCode.value=e.code()

            }
        }
    }
    fun repayLoan(payLoanDTO: PayLoanDTO){
        uiScope.launch {
            val repayLoanDetails= SaccoApi.retrofitService.repayLoan(payLoanDTO)
            try {
                val repayLoanResponse=repayLoanDetails.await()
                if (repayLoanResponse.status==1){
                    formId.value= repayLoanResponse.data.formId.toString()
                    charges.value=repayLoanResponse.data.charges.toString()
                    _statusCode.value=repayLoanResponse.status
                }else if (repayLoanResponse.status==0){
                    _statusMessage.value=repayLoanResponse.message
                    _statusCode.value=0
                }
            }catch (e: HttpException){
                  _statusCode.value=e.code()


            }
        }
    }
    fun loanRepaymentCommit(){
        uiScope.launch {
            val statementDTO=StatementDTO()
            statementDTO.formId=formId.value!!.toInt()
            val loanRequest= SaccoApi.retrofitService.payLoanCommit(statementDTO)

            try {
                val loanResponse=loanRequest.await()
                if (loanResponse.toString().isNotEmpty()){
                    Timber.d("LOAN APP COMMIT RESPONSE $loanResponse")
                    if (loanResponse.status==1){
                        refCode.value=loanResponse.data.transactionCode
                        _statusPay.value=1

                    }else if (loanResponse.status==0){
                        _messagePay.value=loanResponse.message
                        _statusPay.value=0
                    }
                }
            }catch (e: HttpException){
                Timber.e("ERROR COMIT ACC ${e.message}")
                _statusPay.value=e.code()
            }

        }
    }
    /**get saving account dropdown items*/
    private fun loadSavingAccountName(){
        uiScope.launch {
            val savingAccDTO= SavingAccDTO()
            savingAccDTO.isTransactional=1
            _savingPStatus.value=GeneralResponseStatus.LOADING
            val savingProperties= SaccoApi.retrofitService.getSavingAcc(savingAccDTO)
            Timber.d("Saving Account$savingAccDTO")
            try {
                val savingAccResult=savingProperties.await()
                    if(savingAccResult.status==1){
                        /**for the first item in the list*/
                  _savingAccountProperties.value=savingAccResult.data
                        _savingPStatus.value=GeneralResponseStatus.DONE
                }else{
                        _savingPStatus.value=GeneralResponseStatus.DONE
                }
            }catch (e:Exception){
                _savingPStatus.value=GeneralResponseStatus.DONE
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
