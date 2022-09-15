package com.ekenya.rnd.tijara.ui.homepage.loan.guarantors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.ActivesLoan
import com.ekenya.rnd.tijara.network.model.GuarantorData
import com.ekenya.rnd.tijara.network.model.LoanProduct
import com.ekenya.rnd.tijara.requestDTO.AddGuarantorDTO
import com.ekenya.rnd.tijara.requestDTO.LoanGuarantedDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class MyGuarantorsViewModel (application: Application) : AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusCommit = MutableLiveData<Int?>()
    val statusCommit: MutableLiveData<Int?>
        get() = _statusCommit

    private var _guarantors = MutableLiveData<List<GuarantorData>>()
    val guarantors: LiveData<List<GuarantorData>?>
        get() = _guarantors
    private var _loans = MutableLiveData<List<LoanProduct>>()
    val loans: LiveData<List<LoanProduct>?>
        get() = _loans
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    private var _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean>
        get() = _isVisible
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _commitMessage = MutableLiveData<String>()
    val commitMessage: LiveData<String>
        get() = _commitMessage
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        _statusCommit.value=null
        _status.value=null
        _isEmpty.value=false
        _isVisible.value=true
        getMyGuarantors()
    }
    fun stopObserving() {
        _statusCode.value = null
        _statusCommit.value = null
        _status.value = null
    }
    fun getMyGuarantors(){
        _guarantors.value= arrayListOf()
        uiScope.launch {
            _responseStatus.value= GeneralResponseStatus.LOADING
            val guarontorsProperties= SaccoApi.retrofitService.getguarantors()
            try {
                val guarResponse=guarontorsProperties.await()
                    if (guarResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _statusCode.value=guarResponse.status
                        if (guarResponse.data.isNotEmpty()){
                            _guarantors.value= guarResponse.data
                        }else{
                            _guarantors.value= arrayListOf()
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
    fun addGuarantors(addGuarantorDTO: AddGuarantorDTO){
        uiScope.launch {
            val guarontorsProperties= SaccoApi.retrofitService.addGuarantorsPreveiw(addGuarantorDTO)
            try {
                val guarResponse=guarontorsProperties.await()
                    if (guarResponse.status==1){
                        Timber.d("Guarantors RESPONSE $guarResponse")
                        Constants.FORMID=guarResponse.data.formId
                        Constants.GENERAL_NAME=guarResponse.data.memberName
                        _status.value=guarResponse.status

                    }else if(guarResponse.status==0){
                        _statusMessage.value=guarResponse.message
                        _status.value=0
                    }


            }catch (e: HttpException){
                _status.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun addGuarantorsCommit(statementDTO: StatementDTO){
        uiScope.launch {
            val guarontorsRequest= SaccoApi.retrofitService.addGuarantorsCommit(statementDTO)
            try {
                val guarantorsResponse=guarontorsRequest.await()

                    if (guarantorsResponse.status==1){
                        Timber.d("RESPONSE $guarantorsResponse")
                        _statusCommit.value=guarantorsResponse.status

                    }else if(guarantorsResponse.status==0){
                        _commitMessage.value=guarantorsResponse.message
                        _statusCommit.value=0
                    }


            }catch (e: HttpException){
                _statusCommit.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun getLoanGuaranted(loanGuarantedDTO: LoanGuarantedDTO){

        uiScope.launch {
            val loanProperties= SaccoApi.retrofitService.getMustGuarantedLoan(loanGuarantedDTO)
            try {
                val loansResult=loanProperties.await()
                    Timber.d("Loan RESULTS $loansResult")
                    if(loansResult.status==1){
                        /**for the first item in the list*/
                        _loans.value=loansResult.data
                    }

            }catch (e:Exception){
                Timber.e("ERROR BANK ${e.message}")
            }
        }

    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}