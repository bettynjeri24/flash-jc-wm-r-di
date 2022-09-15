package com.ekenya.rnd.tijara.ui.homepage.loan.payloan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ActivesLoan
import com.ekenya.rnd.tijara.requestDTO.PendLoanDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class ActiveLoanViewModel(application: Application):AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _activeLoan = MutableLiveData<List<ActivesLoan>>()
    val activeLoan: LiveData<List<ActivesLoan>>
        get() = _activeLoan
    private var _pendingLoan = MutableLiveData<List<ActivesLoan>>()
    val pendingLoan: LiveData<List<ActivesLoan>>
        get() = _pendingLoan
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isEmptyP = MutableLiveData<Boolean>()
    val isEmptyP: LiveData<Boolean>
        get() = _isEmptyP
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    var amountBorrowed=MutableLiveData<String>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isEmptyP.value=false
        _isVisible.value=true
        getActiveLoans()
        getPendingLoans()
    }
    fun stopObserving() {
        _statusCode.value = null
    }
    private fun getActiveLoans(){
        _activeLoan.postValue(arrayListOf())
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val loanProperties= SaccoApi.retrofitService.getActiveLoans()
            try {
                val loanResponse=loanProperties.await()

                    if (loanResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE

                        _statusCode.value=loanResponse.status
                        if (loanResponse.data.isNotEmpty()){
                            _activeLoan.value= loanResponse.data
                            Timber.d("LOANS RESPONSE $loanResponse")
                            // _isVisible.value=true
                        }else{
                            _responseStatus.value = GeneralResponseStatus.DONE
                            _isVisible.value=false
                            _activeLoan.postValue(arrayListOf())
                            _isEmpty.value=true
                        }


                }

            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.DONE
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    private fun getPendingLoans(){
        _pendingLoan.postValue(arrayListOf())
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val pendLoanDTO=PendLoanDTO()
            pendLoanDTO.pendingDisbursement=1
            val loanProperties= SaccoApi.retrofitService.getpendingLoans(pendLoanDTO)
            try {
                val loanResponse=loanProperties.await()

                    if (loanResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE

                        _statusCode.value=loanResponse.status
                        if (loanResponse.data.isNotEmpty()){
                            _pendingLoan.value= loanResponse.data
                            Timber.d("LOANS RESPONSE $loanResponse")
                            // _isVisible.value=true
                        }else{
                            _isVisible.value=false
                            _pendingLoan.postValue(arrayListOf())
                            _isEmptyP.value=true
                        }


                }

            }catch (e: Exception){
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
