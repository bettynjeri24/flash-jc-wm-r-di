package com.ekenya.rnd.tijara.ui.homepage.loan.getloan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.LoanDataHistory
import com.ekenya.rnd.tijara.network.model.LoanProduct
import com.ekenya.rnd.tijara.requestDTO.LoanDetailsDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LoanProductViewModel @Inject constructor (application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _loanProduct = MutableLiveData<List<LoanProduct>>()
    val loanProduct: LiveData<List<LoanProduct>?>
        get() = _loanProduct
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isEmptyHistory = MutableLiveData<Boolean>()
    val isEmptyHistory: LiveData<Boolean>
        get() = _isEmptyHistory
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private val viewModelJob = Job()
    private var _loanHistory = MutableLiveData<List<LoanDataHistory>>()
    val loanHistory: LiveData<List<LoanDataHistory>?>
        get() = _loanHistory
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    var canApply=MutableLiveData<Boolean>()
    var canPay=MutableLiveData<Boolean>()
    var mustGuaranteed=MutableLiveData<Boolean>()
    var hasActiveLoan=MutableLiveData<Boolean>()
    var amountBorrowed=MutableLiveData<String>()
    var availBal=MutableLiveData<String>()
    var loanLimit=MutableLiveData<String>()
    var thresholdAmnt=MutableLiveData<String>()
    var productId=MutableLiveData<String>()
    var productName=MutableLiveData<String>()

    init {
        _statusCode.value=null
        _isEmpty.value=false
        _isVisible.value=true
        getLoanProducts()
    }
    fun stopObserving() {
        _statusCode.value = null
        _status.value = null

    }
    private fun getLoanProducts(){
        _loanProduct.postValue(arrayListOf())
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val loanProperties= SaccoApi.retrofitService.getLoanProduct()
            try {
                val loanResponse=loanProperties.await()
                    Timber.d("LOANS RESPONSE $loanResponse")
                    if (loanResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCode.value=loanResponse.status
                        if (loanResponse.data.isNotEmpty()){
                            _loanProduct.value= loanResponse.data
                        }else{
                            _loanProduct.postValue(arrayListOf())
                            _isVisible.value=false
                            _isEmpty.value=true
                        }

                    }else{
                        _responseStatus.value = GeneralResponseStatus.DONE
                    }


            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun getLoanHistory(loanDetailsDTO: LoanDetailsDTO){
        _loanHistory.postValue(arrayListOf())
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val loanProperties= SaccoApi.retrofitService.getLoanHistory(loanDetailsDTO)
            try {
                val loanResponse=loanProperties.await()
                    Timber.d("LOANS RESPONSE $loanResponse")
                    if (loanResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        if (loanResponse.data.history.isNotEmpty()){
                            _loanHistory.value= loanResponse.data.history
                        }else{
                            _isVisible.value=false
                            _loanHistory.postValue(arrayListOf())
                            _isEmptyHistory.value=true
                        }
                        _status.value=loanResponse.status

                    }else if (loanResponse.status==0){
                        _responseStatus.value= GeneralResponseStatus.ERROR
                        _status.value=0
                    }


            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                _status.value=e.hashCode()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
