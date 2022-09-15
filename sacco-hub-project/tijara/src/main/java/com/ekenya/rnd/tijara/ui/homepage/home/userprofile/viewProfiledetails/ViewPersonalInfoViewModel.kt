package com.ekenya.rnd.tijara.ui.homepage.home.userprofile.viewProfiledetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.*
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class ViewPersonalInfoViewModel @Inject constructor (application: Application): AndroidViewModel(application) {
    val app=application
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode

    private var _personalInfoList = MutableLiveData<PersonalInfo>()
    val personalInfoList: LiveData<PersonalInfo>
        get() = _personalInfoList
    private var _residenseInfoList = MutableLiveData<ResidenceInfo>()
    val residenseInfoList: LiveData<ResidenceInfo>
        get() = _residenseInfoList
    private var _idInfoList = MutableLiveData<Identification>()
    val idInfoList: LiveData<Identification>
        get() = _idInfoList

    private var _allpersonalInfoList= MutableLiveData<ProfileInfoResponse>()
    val llpersonalInfoList: LiveData<ProfileInfoResponse>
        get() = _allpersonalInfoList
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)



    init {
        _statusCode.value=null
        getPersonalInfo()
    }
    private fun getPersonalInfo(){
        uiScope.launch {
            _isObserving.value = true
            _responseStatus.value= GeneralResponseStatus.LOADING
            val personalDetails= SaccoApi.retrofitService.getPersonalInfo()
            try {
                val personalResponse=personalDetails.await()
                if (personalResponse.toString().isNotEmpty()){
                    Timber.d("PERSONAL LISTS RESPONSE $personalResponse")
                    if (personalResponse.status==1){
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _allpersonalInfoList.value=personalResponse
                        _personalInfoList.value=personalResponse.data.personalInfo
                        _residenseInfoList.value=personalResponse.data.residenceInfo
                        _idInfoList.value=personalResponse.data.identification
                        _statusCode.value=1
                    }else if (personalResponse.status==0){
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
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
