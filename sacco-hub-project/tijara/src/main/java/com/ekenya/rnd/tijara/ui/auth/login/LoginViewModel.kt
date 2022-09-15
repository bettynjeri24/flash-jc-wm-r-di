package com.ekenya.rnd.tijara.ui.auth.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.network.api.SaccoApi
import com.ekenya.rnd.tijara.requestDTO.LoginDTO
import com.ekenya.rnd.tijara.utils.PrefUtils
import com.ekenya.rnd.tijara.utils.camelCase
import com.ekenya.rnd.tijara.utils.formatRequestTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import java.util.*
import javax.inject.Inject

enum class GeneralResponseStatus { LOADING, DONE, ERROR }
class LoginViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
    val app = application
    private var _status = MutableLiveData<Int?>()
    val status: MutableLiveData<Int?>
        get() = _status
    private var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String>
        get() = _statusMessage
    private var _isFirstTimeStatus = MutableLiveData<Boolean>()
    val isFirstTimeStatus: LiveData<Boolean>
        get() = _isFirstTimeStatus
    private var _isObserving = MutableLiveData<Boolean>()
    val isObserving: LiveData<Boolean>
        get() = _isObserving

    private var _changePassword = MutableLiveData<Boolean>()
    val changePassword: LiveData<Boolean>
        get() = _changePassword
    private var _responseStatus = MutableLiveData<GeneralResponseStatus>()
    val responseStatus: LiveData<GeneralResponseStatus>
        get() = _responseStatus
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _status.value = null
    }
    fun loginUser(loginDTO: LoginDTO) {
        uiScope.launch {
            val getLoginProperties = SaccoApi.retrofitService.login(loginDTO)
            Timber.d("LOGIN REQUEST IS $loginDTO")
            try {
                val loginResults = getLoginProperties.await()
                if (loginResults.toString().isNotEmpty()) {
                    Timber.d("LOGIN RESPONSE $loginResults")
                    if (loginResults.status == 1) {
                        _responseStatus.value = GeneralResponseStatus.DONE
                        _status.value = loginResults.status
                        PrefUtils.setPreference(app.applicationContext, "token", loginResults.data.token)
                        PrefUtils.setPreference(
                            app.applicationContext,
                            "logintime",
                            formatRequestTime(loginResults.data.lastLogin)
                        )
                        PrefUtils.setPreference(app.applicationContext, PrefUtils.PREF_JTOKEN, loginResults.data.token)
                        PrefUtils.setPreference(app.applicationContext, "changePassword", loginResults.data.changePassword.toString().trim())
                        val loginStatus = Constants.FIRSTLOGIN == loginResults.data.isFirstLogin
                        Timber.d("MODER FIRST TIME $loginStatus")
                        _isFirstTimeStatus.value = loginResults.data.isFirstLogin
                        _changePassword.value = loginResults.data.changePassword
                        Constants.token = PrefUtils.getPreferences(app.applicationContext, PrefUtils.PREF_JTOKEN)!!
                        val time = PrefUtils.getPreferences(app.applicationContext, "logintime")!!
                        Timber.d("FIRST TIME LOGIN STATUS: ${loginResults.data.isFirstLogin} ")
                        Timber.d("TOKEN ${Constants.token}")
                        Timber.d("LOGIN TIME $time")
                        Constants.PHONENUMBER = loginResults.data.user.phone
                        Constants.EMAILADDRESS = loginResults.data.user.email
                        Timber.d("Constants.PHONENUMBER ${Constants.PHONENUMBER}")
                        var serverUserName = loginResults.data.user.name.toLowerCase(Locale.US)
                        Constants.USERFULLNAME = loginResults.data.user.name
                        serverUserName = camelCase(serverUserName)
                        PrefUtils.setPreference(app.applicationContext, "username", serverUserName)
                        val splited: List<String> = serverUserName.split("\\s".toRegex())
                        if (splited.count() == 2) {
                            val lastName = splited[1]
                            PrefUtils.setPreference(app.applicationContext, "lastName", camelCase(lastName))
                        }
                        val firstName = splited[0]
                        PrefUtils.setPreference(app.applicationContext, "firstName", camelCase(firstName))
                    } else if (loginResults.status == 0) {
                        _statusMessage.value = loginResults.message
                        _status.value = 0
                    }
                }
            } catch (e: HttpException) {
                _status.value = e.code()
                Timber.e("ERROR OCCURRED ${e.code()}")
            }
        }
    }
    fun stopObserving() {
        _status.value = null
        _isObserving.value = false
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
