package com.ekenya.rnd.tijara.ui.homepage.statement

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.FullStatItems
import com.ekenya.rnd.tijara.network.model.SavingAccountData
import com.ekenya.rnd.tijara.requestDTO.FullStatementDTO
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class FullStatementViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusCommit = MutableLiveData<Int?>()
    val statusCommit: MutableLiveData<Int?>
        get() = _statusCommit
    private var _statusCMessage = MutableLiveData<String>()
    val statusCMessage: LiveData<String>
        get() = _statusCMessage
    private var _savingAccountProperties = MutableLiveData<List<SavingAccountData>>()
    val savingAccountProperties: LiveData<List<SavingAccountData>?>
        get() = _savingAccountProperties
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _fullStatProperties = MutableLiveData<FullStatItems>()
    val fullStatProperties: LiveData<FullStatItems>
        get() = _fullStatProperties
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty
    val app=application
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _statusCode.value=null
        loadSavingAccountName()
    }
    fun stopObserving(){
        _statusCode.value=null
        _statusCommit.value=null
        _status.value=null
        _isObserving.value = false
    }

    fun getFullStatement(fullStatementDTO: FullStatementDTO) {
        uiScope.launch {
            _isObserving.value = true
            _responseStatus.value= GeneralResponseStatus.LOADING
            val fullDetails= SaccoApi.retrofitService.getFullStatement(fullStatementDTO)
            try {
                val fStatResponse=fullDetails.await()
                if (fStatResponse.toString().isNotEmpty()){
                    Timber.d(" RESPONSE $fStatResponse")
                    if (fStatResponse.status==1){
                        _statusCode.value=fStatResponse.status
                        _fullStatProperties.value=fStatResponse.data
                        Constants.FULLSFORMID=fStatResponse.data.formId
                    }else if (fStatResponse.status==0){
                        _statusMessage.value=fStatResponse.message
                        _statusCode.value=0
                    }
                }

            }catch (e:Exception){
                _statusCode.value=e.hashCode()
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }

    fun generateFullStatement(statementDTO: StatementDTO) {
        uiScope.launch {
            _isObserving.value = true
            val statementRequest= SaccoApi.retrofitService.genarateFullStatement(statementDTO)
            try {
                val statResponse=statementRequest.await()
                    Timber.d("COMMIT STAT RESPONSE $statResponse")
                    if (statResponse.status==1){
                        _statusCommit.value=statResponse.status
                        Constants.FULLSREFCODE=statResponse.data.transactionCode
                    }else if (statResponse.status==0){
                        _statusCMessage.value=statResponse.message
                        _statusCommit.value=statResponse.status
                    }


            }catch (e: HttpException){
                _statusCommit.value=e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    /**get saving account dropdown items*/
    fun loadSavingAccountName(){
        _savingAccountProperties.value= arrayListOf()
        uiScope.launch {
            val savingProperties= SaccoApi.retrofitService.getSavingAccount()
            try {
                val savingAccResult=savingProperties.await()
                    Timber.d("SAVING RESULTS $savingAccResult")
                    if(savingAccResult.status==1){
                        Log.d("TAG","CHECK !")

                        if (savingAccResult.data.isNullOrEmpty()){
                            _savingAccountProperties.value= arrayListOf()
                            Log.d("TAG","CHECK !")
                            _isEmpty.value=true

                        }else{
                            _savingAccountProperties.value=savingAccResult.data
                            _isEmpty.value=false
                        }
                        _status.value=savingAccResult.status
                    }else if (savingAccResult.status==0){
                        _status.value=savingAccResult.status
                    }

            }catch (e: Exception){
                Timber.e("ERROR SAVING ACC ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}