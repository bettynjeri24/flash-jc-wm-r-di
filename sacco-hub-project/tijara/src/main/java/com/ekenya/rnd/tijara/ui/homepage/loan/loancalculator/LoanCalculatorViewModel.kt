package com.ekenya.rnd.tijara.ui.homepage.loan.loancalculator



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.LoanCalculatorData
import com.ekenya.rnd.tijara.network.model.LoanProduct
import com.ekenya.rnd.tijara.requestDTO.LoanCalDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class LoanCalculatorViewModel(application: Application): AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _loanProduct = MutableLiveData<List<LoanProduct>>()
    val loanProduct: LiveData<List<LoanProduct>>
        get() = _loanProduct
    private var _calData = MutableLiveData<LoanCalculatorData>()
    val calData: LiveData<LoanCalculatorData>
        get() = _calData
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _rate = MutableLiveData<String>()
    val rate: LiveData<String>
        get() = _rate
    fun setRate(value:String){
        _rate.value=value
    }
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null

    }
    fun stopObserving(){
        _statusCode.value=null
        _isObserving.value = false
    }

    fun calculateLoan(loanCalDTO: LoanCalDTO) {
        uiScope.launch {
            val calLoanRequest = SaccoApi.retrofitService.calculateLoan(loanCalDTO)
            try {
                val calLoanResponse = calLoanRequest.await()
                if (calLoanResponse.toString().isNotEmpty()) {
                    if (calLoanResponse.status==1) {
                        _calData.value=calLoanResponse.data
                        _statusMessage.value=calLoanResponse.message
                        _statusCode.value=calLoanResponse.status
                    }else if (calLoanResponse.status==0){
                        _statusMessage.value=calLoanResponse.message
                        _statusCode.value=calLoanResponse.status
                    }
                }
            } catch (e: Exception) {
                _statusMessage.value=e.localizedMessage
                _statusCode.value=e.hashCode()
                Timber.e("ERROR ${e.message.toString()}")
            }
        }
    }

     fun loadLoanProducts(){
        uiScope.launch {
            val loanRequest= SaccoApi.retrofitService.getLoanProduct()
            try {
                val loanResponse=loanRequest.await()
                if (loanResponse.toString().isNotEmpty()){
                    Timber.d("SAVING RESULTS $loanResponse")
                    if(loanResponse.status==1){
                        /**for the first item in the list*/
                        _loanProduct.value=loanResponse.data


                    }
                }
            }catch (e:Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}