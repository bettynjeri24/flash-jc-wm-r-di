package com.ekenya.rnd.tijara.ui.auth.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.baseapp.BaseApp
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.network.NetworkHelper
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.network.model.NewSaccoData
import com.ekenya.rnd.tijara.network.model.SaccoDetail
import com.ekenya.rnd.tijara.network.model.local.NewSaccoDataEntity
import com.ekenya.rnd.tijara.network.model.local.SaccoDetailEntity
import com.ekenya.rnd.tijara.requestDTO.CountryDTO
import com.ekenya.rnd.tijara.requestDTO.NewAccDTO
import com.ekenya.rnd.tijara.ui.auth.login.GeneralResponseStatus
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.TijaraRoomDatabase
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.NewSaccoRepository
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.billpayment.roomdb.repositories.SaccoDetailsRepository
import com.ekenya.rnd.tijara.utils.PrefUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class CountryViewmodel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    val app = application
    val mySaccoDetails = arrayListOf<SaccoDetail?>()
    val newSaccoDetails = arrayListOf<NewSaccoData?>()
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _saccodetails = MutableLiveData<List<SaccoDetail>>()
    val saccodetails: LiveData<List<SaccoDetail>>
        get() = _saccodetails
    private var _loginSaccos = MutableLiveData<List<SaccoDetail>>()
    val loginSaccos: LiveData<List<SaccoDetail>>
        get() = _loginSaccos

    private var _newSaccoList = MutableLiveData<List<NewSaccoData>>()
    val newSaccoList: LiveData<List<NewSaccoData>>
        get() = _newSaccoList

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
    private var _isNewError = MutableLiveData<Boolean>()
    val isNewError: LiveData<Boolean>
        get() = _isNewError
    private var _statusCode = MutableLiveData<Int?>()
    val statusCode: MutableLiveData<Int?>
        get() = _statusCode
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private lateinit var saccoRepository: SaccoDetailsRepository
    private lateinit var newSaccoRepository: NewSaccoRepository
    fun getAllSaccos(): LiveData<List<SaccoDetailEntity>> {
        return saccoRepository.allSaccoDetails
    }

    fun getNewSaccos(): LiveData<List<NewSaccoDataEntity>> {
        return newSaccoRepository.allNewSaccoDetails
    }

    fun getSaccosDetails(): List<SaccoDetailEntity> {
        return saccoRepository.getSaccoDetails()
    }

    init {
        val saccoDao = TijaraRoomDatabase.getDatabase(application).getAllSaccoDao()
        val newSaccoDao = TijaraRoomDatabase.getDatabase(application).getNewSaccoDao()
        saccoRepository = SaccoDetailsRepository(saccoDao)
        newSaccoRepository = NewSaccoRepository(newSaccoDao)
        _status.value = null
        _statusCode.value = null
        _isEmpty.value = false
        _isError.value = false
        _isVisible.value = true
        _isObserving.value = false
    }

    fun stopObserving() {
        _status.value = null
        _statusCode.value = null
        _isObserving.value = false
    }

    fun getSaccos(countryDTO: CountryDTO) {
        if (NetworkHelper.isNetworkConnected()) {
            uiScope.launch {
                _responseStatus.value = GeneralResponseStatus.LOADING
                val request = SaccoApi.retrofitService.getSaccos(countryDTO)
                try {
                    val response = request.await()
                    val code = response.code()
                    val pSaccoResult = response.body()
                    if (code == 200 || code == 201) {
                        /**for use in our recyclerview which is listing all the sacco one is registered into"*/
                        if (pSaccoResult!!.status == 1) {
                            val items = pSaccoResult.data.map {
                                SaccoDetailEntity(
                                    it.name,
                                    it.firstName,
                                    it.username,
                                    it.orgId,
                                    it.isSacco
                                )
                            }
                            saccoRepository.insertSacco(items)
                            PrefUtils.setPreference(
                                app.applicationContext,
                                app.applicationContext.getString(R.string.stored_username),
                                pSaccoResult.data[0].username.toString()
                            )
                            Constants.RegUsername = pSaccoResult.data[0].username
                            _responseStatus.value = GeneralResponseStatus.DONE
                            mySaccoDetails.clear()
                            mySaccoDetails.addAll(pSaccoResult.data)
                            _saccodetails.value = pSaccoResult.data
                            _status.value = pSaccoResult.status
                        } else {
                            _statusMessage.value = pSaccoResult.message
                            _status.value = 0
                            _isError.value = true
                            //  _isVisible.value=false
                        }
                    } else {
                        var errorBody = response.errorBody()?.string()
                        if (errorBody != null) {
                            val errorData = JSONObject(errorBody)
                            val message = errorData.getString("message")
                            _statusMessage.value = message
                        } else {
                            _statusMessage.value = BaseApp.applicationContext()
                                .getString(R.string.error_occurred)
                        }
                        _status.value = 0
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    if (e is IOException) {
                        _statusMessage.value = BaseApp.applicationContext()
                            .getString(R.string.no_network_connection)
                    }
                    _isError.value = true
                    _isVisible.value = false
                    _status.value = e.hashCode()
                }
            }
        } else {
            _statusMessage.value =
                BaseApp.applicationContext().getString(R.string.no_network_connection)
            _status.value = 0
        }
    }

    fun getNewSaccos(newAccDTO: NewAccDTO) {
        uiScope.launch {
            val pSaccosDetails = SaccoApi.retrofitService.getNewSaccos(newAccDTO)
            try {
                val pSaccoResult = pSaccosDetails.await()
                if (pSaccoResult.toString().isNotEmpty()) {
                    Timber.d("SACCO DETAILS RESPONSE $pSaccoResult")
                    /**for use in our recyclerview which is listing all the sacco one is registered into"*/
                    if (pSaccoResult.status == 1) {
                        val items = pSaccoResult.data.map {
                            NewSaccoDataEntity(
                                it.id,
                                it.isSacco,
                                it.name,
                                it?.website
                            )
                        }
                        newSaccoRepository.insertNewSacco(items)
                        newSaccoDetails.clear()
                        newSaccoDetails.addAll(pSaccoResult.data)
                        _newSaccoList.value = pSaccoResult.data
                        _statusCode.value = pSaccoResult.status
                    } else if (pSaccoResult.status == 0) {
                        _statusMessage.value = pSaccoResult.message
                        _statusCode.value = 0
                        _isNewError.value = true
                        //  _isVisible.value=false
                    }
                } else {
                    _isNewError.value = true
                    //  _isVisible.value=false
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                _responseStatus.value = GeneralResponseStatus.ERROR
                _isError.value = true
                _isVisible.value = false
                _statusCode.value = e.code()
                Timber.e("ERROR Exception${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
