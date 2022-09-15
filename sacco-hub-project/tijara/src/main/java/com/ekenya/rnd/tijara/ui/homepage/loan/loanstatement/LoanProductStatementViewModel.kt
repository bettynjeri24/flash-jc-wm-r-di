package com.ekenya.rnd.tijara.ui.homepage.loan.loanstatement

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ActivesLoan
import com.ekenya.rnd.tijara.network.model.LoanStatement
import com.ekenya.rnd.tijara.network.model.LoanStatementData
import com.ekenya.rnd.tijara.network.model.LoanSummaryResponse
import com.ekenya.rnd.tijara.requestDTO.LoanStatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class LoanProductStatementViewModel (application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status

    private var _activeLoan = MutableLiveData<List<ActivesLoan>>()
    val activeLoan: LiveData<List<ActivesLoan>>
        get() = _activeLoan

    private var _transactionLoanDetails = MutableLiveData<List<LoanStatement>>()
    val transactionLoanDetails: LiveData<List<LoanStatement>>
        get() = _transactionLoanDetails
    private var _allLoantransDetails= MutableLiveData<LoanSummaryResponse>()
    val allLoantransDetails: LiveData<LoanSummaryResponse>
        get() = _allLoantransDetails
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _responseLoading = MutableLiveData<GeneralResponseStatus>()
    val responseLoading: LiveData<GeneralResponseStatus>
        get() = _responseLoading
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isEmptyLoan = MutableLiveData<Boolean>()
    val isEmptyLoan: LiveData<Boolean>
        get() = _isEmptyLoan
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private var _accountId = MutableLiveData<Int>()
    val accountId: LiveData<Int>
        get() = _accountId
    private var _accountName = MutableLiveData<String>()
    val accountName: LiveData<String>
        get() = _accountName
    fun setAccountId(value:Int){
        _accountId.value=value
    }
    fun setProductName(value:String){
        _accountName.value=value
    }
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isVisible.value=true
        getActiveLoans()
        getLoanStatement()
    }
    fun stopObserving() {
        _statusCode.value = null
    }
    private fun getActiveLoans(){
        _activeLoan.postValue(arrayListOf())
        uiScope.launch {
            _responseLoading.value= GeneralResponseStatus.LOADING
            val loanProperties= SaccoApi.retrofitService.getActiveLoans()
            try {
                val loanResponse=loanProperties.await()
                    if (loanResponse.status==1){
                        _responseLoading.value = GeneralResponseStatus.DONE
                        _statusCode.value=loanResponse.status
                        if (loanResponse.data.isNotEmpty()){
                            _activeLoan.value= loanResponse.data
                            Timber.d("LOANS RESPONSE $loanResponse")
                            _isVisible.value=true
                        }else{
                            _responseLoading.value= GeneralResponseStatus.ERROR
                            _isVisible.value=false
                            _activeLoan.postValue(arrayListOf())
                            Log.d("TAG","ACTIVES")
                            _isEmptyLoan.value=true
                        }

                    }else{
                        _responseLoading.value= GeneralResponseStatus.ERROR

                }

            }catch (e: Exception){
                _responseLoading.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun getLoanStatement(){
        _transactionLoanDetails.value= arrayListOf()
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val loanStatementDTO= LoanStatementDTO()
            loanStatementDTO.loanAccountId= _accountId.value?.toString().toString()
            val loanSummaryDetails= SaccoApi.retrofitService.getLoanSummary(loanStatementDTO)
            try {
                val summaryResponse=loanSummaryDetails.await()
                    Timber.d("Loan LISTS RESPONSE $summaryResponse")
                    if (summaryResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        if (summaryResponse.data.transactions.isNotEmpty()){
                            _allLoantransDetails.value=summaryResponse
                            _transactionLoanDetails.value= summaryResponse.data.transactions
                        }else{
                            _transactionLoanDetails.value= arrayListOf()
                            Log.d("TAG","ACTI")
                            _isVisible.value=false
                            _isEmpty.value=true
                        }
                        _status.value=summaryResponse.status

                    }else if (summaryResponse.status==0){
                        _status.value=summaryResponse.status
                        _responseStatus.value= GeneralResponseStatus.ERROR
                    }


            }catch (e: Exception){
                _status.value=e.hashCode()
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
