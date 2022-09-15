package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.signup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.BankBranchList
import com.ekenya.rnd.tijara.network.model.BankBranchResponse
import com.ekenya.rnd.tijara.network.model.BankList
import com.ekenya.rnd.tijara.network.model.BankResponse
import com.ekenya.rnd.tijara.requestDTO.BankBranchDTO
import com.ekenya.rnd.tijara.requestDTO.BankDetailsDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class BankDetailsViewModel @Inject constructor(application: Application):AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private var _allBankListProperties = MutableLiveData<BankResponse>()
    val allBankListProperties: LiveData<BankResponse>
        get() = _allBankListProperties

    private var _bankListProperties = MutableLiveData<List<BankList>>()
    val bankListProperties: LiveData<List<BankList>?>
        get() = _bankListProperties
    private var _allBBranchProperties = MutableLiveData<BankBranchResponse>()
    val allBBranchProperties: LiveData<BankBranchResponse>
        get() = _allBBranchProperties

    private var _bBranchListProperties = MutableLiveData<List<BankBranchList>>()
    val bBranchListProperties: LiveData<List<BankBranchList>?>
        get() = _bBranchListProperties
    private val viewModelJob = Job()
    private var _bankId = MutableLiveData<String>()
    val bankId: LiveData<String>
        get() = _bankId
    fun setBankId(value:String){
        _bankId.value=value
    }
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    init {
        _statusCode.value=null
        getBanks()

    }

    fun bankDetails(bankDetailsDTO: BankDetailsDTO){
        uiScope.launch {
            Timber.d("BANK REQUEST$bankDetailsDTO")
            val bankProperties= SaccoApi.retrofitService.createBankAcc(bankDetailsDTO)
            try {
                val bankResponse=bankProperties.await()
                if (bankResponse.toString().isNotEmpty()){
                    Timber.d("BANK RESPONSE $bankResponse")
                    if (bankResponse.status==1){
                        _statusCode.value=bankResponse.status
                    }else if (bankResponse.status==0){
                        _statusMessage.value=bankResponse.message
                        _statusCode.value=0
                    }
                }
            }catch (e:Exception){

                _statusCode.value=e.hashCode()

            }
        }

    }
    private fun getBanks(){
        uiScope.launch {
            val bankProperties= SaccoApi.retrofitService.getBankList()
            try {
                val bankResult=bankProperties.await()
                if (bankResult.toString().isNotEmpty()){
                    Timber.d("BANK RESULTS $bankResult")
                    if(bankResult.status==1){
                        _responseStatus.value= GeneralResponseStatus.DONE
                        _allBankListProperties.value=bankResult
                        /**for the first item in the list*/
                        val first= BankList(0,0,"Select Bank",0,"",)
                        val firstItem=ArrayList<BankList>()
                        firstItem.add(first)
                        firstItem.addAll(bankResult.data)
                        _bankListProperties.value=bankResult.data
                    }else{

                    }
                }
            }catch (e: Exception){
                _responseStatus.value= GeneralResponseStatus.ERROR
                Timber.e("ERROR BANK ${e.message}")
            }
        }

    }
    fun getBankBraches(bankBranchDTO:BankBranchDTO){
        uiScope.launch {
            val bankBranchProperties= SaccoApi.retrofitService.getBankBranchList(bankBranchDTO)
            try {
                val bankBranchResult=bankBranchProperties.await()
                if (bankBranchResult.toString().isNotEmpty()){
                    Timber.d("BANK Branch RESULTS $bankBranchResult")
                    if(bankBranchResult.status==1){
                        _allBBranchProperties.value=bankBranchResult
                        /**for the first item in the list*/
                        val first= BankBranchList(0,0,0,"Select Branch",0)
                        val firstItem=ArrayList<BankBranchList>()
                        firstItem.add(first)
                        firstItem.addAll(bankBranchResult.data)
                        _bBranchListProperties.value=bankBranchResult.data
                    }
                }
            }catch (e: Exception){
                Timber.e("ERROR BANK BRANCHES ${e.message}")
            }
        }

    }
    fun stopObserving(){
        _statusCode.value=null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}