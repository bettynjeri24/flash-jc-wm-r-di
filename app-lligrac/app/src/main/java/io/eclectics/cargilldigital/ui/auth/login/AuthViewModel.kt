package io.eclectics.cargilldigital.ui.auth.login

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.agritech.cargill.repositories.MainRequestRepository
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.data.repository.authrepos.AuthRepository
import io.eclectics.cargilldigital.data.responses.authresponses.AccountIdLookupMobileResponse
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponse
import io.eclectics.cargilldigital.data.responses.authresponses.CargillUserLoginResponseData
import io.eclectics.cargilldigital.viewmodel.RequestResponse
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.cargilldigital.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import javax.inject.Inject

//@HiltViewModel
class AuthViewModel @Inject constructor(
    application: Application,
    private val repository: AuthRepository
) : BaseViewModel(application) {
    var mainLoginLivedata: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()

    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun sendloginRequest(json: JSONObject): MediatorLiveData<ViewModelWrapper<String>> {
        mainLoginLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).loginRequest(json)
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when (results) {
            is ResultWrapper.NetworkError -> mainLoginLivedata.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> mainLoginLivedata.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respData(mainLoginLivedata, results.value)
        }

        return mainLoginLivedata
    }

    /**
     * Navigate to Check navigated Fragment
     */
    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    init {
        //initialise the values at first so that no navigation can occur
        _navigate.value = false
    }

    /**
     * Show that the navigation has occurred
     */
    fun navigate() {
        _navigate.value = true
    }

    /**
     * Show that the navigation has occurred
     */
    fun hasNavigated() {
        _navigate.value = false
    }

    /** Methods for completed events **/
    override fun onCleared() {
        super.onCleared()
        _navigate.value = false
    }

    /** ################################ NEW FUNCTION UPDATES ON 07/09/2022 ####################################### **/


    private val _responseCooparativeIdLookUp: MutableLiveData<ResourceNetwork<AccountIdLookupMobileResponse>> =
        MutableLiveData()
    val responseCooparativeIdLookUp: LiveData<ResourceNetwork<AccountIdLookupMobileResponse>>
        get() = _responseCooparativeIdLookUp

    fun sendCooparativeIdLookUp(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        _responseCooparativeIdLookUp.value = ResourceNetwork.Loading
        _responseCooparativeIdLookUp.value = repository.cooparativeIdLookUp(requestBody)
    }

    // *********************************************************************************

    /**
     * LOGIN TO SYSTEM
     */
    private val _getloggedInUser: MutableLiveData<ResourceNetwork<CargillUserLoginResponse>> =
        MutableLiveData()
    val getloggedInUser: LiveData<ResourceNetwork<CargillUserLoginResponse>>
        get() = _getloggedInUser

    fun loginUser(
        requestBody: RequestBody
    ) = viewModelScope.launch {
        _getloggedInUser.value = ResourceNetwork.Loading
        _getloggedInUser.value = repository.loginUser(requestBody)
    }

    suspend fun saveUserVmRoom(user: CargillUserLoginResponseData) = repository.saveUserInRoom(user)

    //
    fun getUserVmRoom() = repository.getUserInRoom().asLiveData()

}