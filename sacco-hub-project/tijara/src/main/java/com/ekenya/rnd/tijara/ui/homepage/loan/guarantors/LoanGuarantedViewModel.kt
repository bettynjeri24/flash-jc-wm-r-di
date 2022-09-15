package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.NewLoanGuaranteData
import com.ekenya.rnd.tijara.requestDTO.*
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class LoanGuarantedViewModel  (application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _responseLoading = MutableLiveData<GeneralResponseStatus>()
    val responseLoading: LiveData<GeneralResponseStatus>
        get() = _responseLoading
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusCan = MutableLiveData<Int?>()
    val statusCan: MutableLiveData<Int?>
        get() = _statusCan
    private var _statusCommit = MutableLiveData<Int?>()
    val statusCommit: MutableLiveData<Int?>
        get() = _statusCommit
    private var _loans = MutableLiveData<List<NewLoanGuaranteData>>()
    val loans: LiveData<List<NewLoanGuaranteData>>
        get() = _loans
    private var _loansRequest = MutableLiveData<List<NewLoanGuaranteData>>()
    val loansRequest: LiveData<List<NewLoanGuaranteData>>
        get() = _loansRequest
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isEmptyRequest = MutableLiveData<Boolean>()
    val isEmptyRequest: LiveData<Boolean>
        get() = _isEmptyRequest
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _canMessage = MutableLiveData<String>()
    val canMessage: LiveData<String>
        get() = _canMessage

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _statusCommit.value=null
        _status.value=null
        _statusCan.value=null
        _isEmpty.value=false
        _isEmptyRequest.value=false
        _isVisible.value=true
        getAllLoanGuaranted()
        getNewLoanLoanGuaranted()
    }
    fun stopObserving() {
        _statusCode.value = null
        _statusCan.value=null
        _statusCommit.value = null
        _status.value = null
    }
    fun getAllLoanGuaranted(){
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val guarontorsProperties= SaccoApi.retrofitService.getAllGuarantedLoan()
            try {
                val guarResponse=guarontorsProperties.await()
                    Timber.d("LOANS RESPONSE $guarResponse")
                    if (guarResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCommit.value=guarResponse.status
                        if (guarResponse.data.isNotEmpty()){
                            _loans.value=guarResponse.data
                        }else{
                            _isEmpty.value=true
                        }

                    }else if (guarResponse.status==0){
                        _statusCommit.value=0
                        _responseStatus.value= GeneralResponseStatus.DONE

                }

            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun getNewLoanLoanGuaranted(){
        _responseLoading.value= GeneralResponseStatus.LOADING
        uiScope.launch {
            val newLoanRequestDTO=NewLoanRequestDTO()
            newLoanRequestDTO.isNew=1
            val loanProperties= SaccoApi.retrofitService.getNewGuarantedLoan(newLoanRequestDTO)
            try {
                val loansResult=loanProperties.await()
                    Log.d("TAG","Loan RESULTS $loansResult")
                    if(loansResult.status==1){
                        _statusCode.value=1
                        _responseLoading.value= GeneralResponseStatus.DONE
                        /**for the first item in the list*/
                        if (loansResult.data.isNotEmpty()){
                            _loansRequest.value=loansResult.data
                        }else{
                            _isEmptyRequest.value=true
                        }

                    }else if (loansResult.status==0){
                        _responseLoading.value= GeneralResponseStatus.DONE
                        _statusCode.value=0

                }
            }catch (e: HttpException){
                _responseLoading.value= GeneralResponseStatus.DONE

                Timber.e("ERROR BANK ${e.message}")
            }
        }

    }
    fun actOnLoan(actLoanDTO: ActLoanDTO) {
        uiScope.launch {
            val cancelRequest = SaccoApi.retrofitService.getActLoan(actLoanDTO)
            try {
                val cancelResponse = cancelRequest.await()

                if (cancelResponse.status == 1) {
                    _statusCan.value = cancelResponse.status
                } else if (cancelResponse.status == 0) {
                    _canMessage.value = cancelResponse.message
                    _statusCan.value = 0

                }
            } catch (e: HttpException) {

                _statusCan.value = e.code()

                Timber.e("ERROR Exception${e.message}")

            }
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}