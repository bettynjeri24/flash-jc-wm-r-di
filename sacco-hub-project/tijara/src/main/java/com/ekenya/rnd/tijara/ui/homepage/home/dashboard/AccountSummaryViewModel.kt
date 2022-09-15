package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.AccountSummary
import com.ekenya.rnd.tijara.network.model.TransactionDetails
import com.ekenya.rnd.tijara.requestDTO.AccountSummaryDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class AccountSummaryViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode

    private var _transactionDetails = MutableLiveData<List<TransactionDetails>>()
    val transactionDetails: LiveData<List<TransactionDetails>?>
        get() = _transactionDetails
    private var _alltransDetails= MutableLiveData<AccountSummary>()
    val alltransDetails: LiveData<AccountSummary>
        get() = _alltransDetails
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isVisible.value=true
        getTransactionSummary()

    }
    fun stopObserving() {
        _statusCode.value = null
    }
    private fun getTransactionSummary(){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val accountSummaryDTO= AccountSummaryDTO()
            accountSummaryDTO.accountId= Constants.ACCOUNTID
                accountSummaryDTO.productId= Constants.SAVINGPRODUCTID
            val summaryDetails= SaccoApi.retrofitService.getUserAccountsSummary(accountSummaryDTO)
            try {
                val summaryResponse=summaryDetails.await()
                    Timber.d("SUMMARY LISTS RESPONSE $summaryResponse")
                    if (summaryResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCode.value=summaryResponse.status
                        if (summaryResponse.data.transactions.isNotEmpty()){
                            _alltransDetails.value=summaryResponse
                            _transactionDetails.value= summaryResponse.data.transactions
                        }else{
                            _isVisible.value=false
                            _isEmpty.value=true
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
