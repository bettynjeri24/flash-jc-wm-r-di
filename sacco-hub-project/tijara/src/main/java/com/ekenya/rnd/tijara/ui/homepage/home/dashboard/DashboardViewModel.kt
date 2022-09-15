package com.ekenya.rnd.tijara.ui.homepage.home.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.AccountSummary
import com.ekenya.rnd.tijara.network.model.Accounts
import com.ekenya.rnd.tijara.network.model.AccountsResponse
import com.ekenya.rnd.tijara.network.model.TransactionDetails
import com.ekenya.rnd.tijara.requestDTO.AccountSummaryDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DashboardViewModel @Inject constructor(private val app: Application):AndroidViewModel(app) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _accList = MutableLiveData<List<Accounts>>()
    val accList: LiveData<List<Accounts>>
        get() = _accList
    private var _accResponse= MutableLiveData<AccountsResponse>()
    val accResponse: LiveData<AccountsResponse>
        get() = _accResponse

    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private var _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError
    private var _refresh = MutableLiveData<Boolean>()
    val refresh: LiveData<Boolean>
        get() = _refresh
    private var _accountId = MutableLiveData<Int>()
    val accountId: LiveData<Int>
        get() = _accountId
    private var _productId = MutableLiveData<String>()
    val productId: LiveData<String>
        get() = _productId
    private var _accountName = MutableLiveData<String>()
    val accountName: LiveData<String>
        get() = _accountName
    fun setRefresh(value:Boolean){
        _refresh.value=value
    }
    fun setAccountId(value:Int){
        _accountId.value=value
    }
    fun setProductId(value:String){
        _productId.value=value
    }
    fun setProductName(value:String){
        _accountName.value=value
    }
    private var _transactionDetails = MutableLiveData<List<TransactionDetails>>()
    val transactionDetails: LiveData<List<TransactionDetails>>
        get() = _transactionDetails
    private var _alltransDetails= MutableLiveData<AccountSummary>()
    val alltransDetails: LiveData<AccountSummary>
        get() = _alltransDetails
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

        init {
            _statusCode.value=null
            _isObserving.value = false
            _isEmpty.value=false
            _isError.value=false
            _isVisible.value=true
            getAccountsData()
         }

         fun getAccountsData(){
            uiScope.launch {
                _responseStatus.value= GeneralResponseStatus.LOADING
                val accDetails= SaccoApi.retrofitService.getUserSavingAccounts()
                try {
                    val accResponse=accDetails.await()
                        if (accResponse.status==1){
                            _responseStatus.value = GeneralResponseStatus.DONE
                            _statusCode.value=accResponse.status
                            if (accResponse.data.isNotEmpty()){
                                _accList.value=(accResponse.data)
                                _accResponse.postValue(accResponse)
                                Timber.d("ACCOUNT RESPONSE ${accResponse.data}")
                            }else {
                                _isVisible.value = false
                                _isEmpty.value = true
                            }
                            _statusCode.value=1
                        }else if (accResponse.status==0){
                            _statusCode.value=accResponse.status
                            _isError.value=true
                        }


                }catch (e: Exception){
                    _isError.value=true
                    _isVisible.value=false
                    _responseStatus.value= GeneralResponseStatus.ERROR
                    Timber.e("ERROR Exception${e.message}")
                }
            }
        }
    fun getTransactionSummary(){
        _transactionDetails.postValue(arrayListOf())
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val accountSummaryDTO= AccountSummaryDTO()
            accountSummaryDTO.accountId= _accountId.value!!
            accountSummaryDTO.productId= _productId.value.toString()
            val summaryDetails= SaccoApi.retrofitService.getUserAccountsSummary(accountSummaryDTO)
            try {
                val summaryResponse=summaryDetails.await()
                    if (summaryResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCode.value=summaryResponse.status
                        if (summaryResponse.data.transactions.isNotEmpty()){
                            _alltransDetails.value=summaryResponse
                            _transactionDetails.value= summaryResponse.data.transactions
                        }else{
                            _transactionDetails.postValue(arrayListOf())
                            _isVisible.value=false
                            _isEmpty.value=true

                        }

                    }else if (summaryResponse.status==0){
                        _statusCode.value=0
                        _responseStatus.value= GeneralResponseStatus.ERROR

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