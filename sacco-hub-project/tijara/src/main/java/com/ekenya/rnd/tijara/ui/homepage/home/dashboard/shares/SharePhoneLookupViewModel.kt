package com.ekenya.rnd.tijara.ui.homepage.home.dashboard.shares

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.CommitShareData
import com.ekenya.rnd.tijara.network.model.ShareMemberLookupResponse
import com.ekenya.rnd.tijara.network.model.ShareTranferData
import com.ekenya.rnd.tijara.requestDTO.CommitShareDTO
import com.ekenya.rnd.tijara.requestDTO.ShareDTO
import com.ekenya.rnd.tijara.requestDTO.ShareLookupDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber

class SharePhoneLookupViewModel(application: Application) : AndroidViewModel(application) {
    val app=application
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private var _shareMessage = MutableLiveData<String>()
    val shareMessage: LiveData<String>
        get() = _shareMessage
    private var _commitCode = MutableLiveData<Int?>()
    val commitCode: MutableLiveData<Int?>
        get() = _commitCode
    private var _commitMessage = MutableLiveData<String>()
    val commitMessage: LiveData<String>
        get() = _commitMessage
    private var _allPhoneResponse= MutableLiveData<ShareMemberLookupResponse>()
    val allPhoneResponse: LiveData<ShareMemberLookupResponse>
        get() = _allPhoneResponse

    private var _allTransferShareResponse= MutableLiveData<ShareTranferData>()
    val allTransferShareResponse: LiveData<ShareTranferData>
        get() = _allTransferShareResponse
    private var _allCommitShareResponse= MutableLiveData<CommitShareData>()
    val allCommitShareResponse: LiveData<CommitShareData>
        get() = _allCommitShareResponse

    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus


    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
        _status.value=null
        _statusCode.value=null
        _commitCode.value=null
        _isObserving.value = false
    }
    fun stopObserving() {
        _status.value = null
        _statusCode.value = null
        _isObserving.value = false
        _commitCode.value=null
    }
    fun shareNoCheck(shareLookupDTO: ShareLookupDTO){
        uiScope.launch {
            _isObserving.value = true
            val phoneDetails= SaccoApi.retrofitService.shareNoLookup(shareLookupDTO)
            try {
                val phoneResult=phoneDetails.await()

                if (phoneResult.status==1){
                    Constants.SHAREMEMBERLOOKUP=phoneResult.data.recipientMemberNumber
                    Constants.SHAREPHONELOOKUP=phoneResult.data.recipientPhone
                    Constants.SHARENAMELOOKUP=phoneResult.data.recipientName
                    Constants.SHAREVALUE=phoneResult.data.valuePerShare
                    Constants.SHAREKES=phoneResult.data.currency
                    _status.value=phoneResult.status
                }else if (phoneResult.status==0){
                    _statusMessage.value=phoneResult.message
                    _status.value=0

                }
            }catch (e: HttpException){
                when {
                    e.code()==500 -> {
                        _status.value=e.code()
                    }
                    else -> {
                        Timber.e("SHARE ERROR ${e.message}")
                    }
                }
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }
    fun transactShare(shareDTO: ShareDTO) {
        uiScope.launch {
            val shareRequest = SaccoApi.retrofitService.shareTransfer(shareDTO)
            try {
                val shareResults = shareRequest.await()
                if (shareResults.toString().isNotEmpty()) {
                    if (shareResults.status==1) {
                        _statusCode.value=shareResults.status
                        _allTransferShareResponse.value=shareResults.data
                        Constants.SHAREREFCODE=shareResults.data.transactionCode
                    }else if (shareResults.status==0){
                        _shareMessage.value=shareResults.message
                        _statusCode.value=shareResults.status

                    }
                }
            } catch (e: HttpException) {
                _responseStatus.value = GeneralResponseStatus.ERROR
                if (e.code()==422){
                    _statusCode.value=422

                }else{
                }
                Timber.e("SHARE TRANSAC ERROR ${e.message.toString()}")
            }
        }
    }
    fun commitShare(commitShareDTO: CommitShareDTO) {
        uiScope.launch {
            val commitRequest = SaccoApi.retrofitService.commitShare(commitShareDTO)
            try {
                val commitResults = commitRequest.await()
                if (commitResults.toString().isNotEmpty()) {
                    if (commitResults.status==1) {
                        _commitCode.value=commitResults.status
                        _allCommitShareResponse.value=commitResults.data
                    }else if (commitResults.status==0){
                        _commitMessage.value=commitResults.message
                        _commitCode.value=commitResults.status
                    }
                }
            } catch (e: HttpException) {
                _responseStatus.value = GeneralResponseStatus.ERROR
                if (e.code()==422){
                    _statusCode.value=422

                }else{
                }
                Timber.e("COOMIT TRANSAC ERROR ${e.message.toString()}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}