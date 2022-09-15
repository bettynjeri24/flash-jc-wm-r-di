package io.eclectics.cargill.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.cargill.repository.AgentRequestRepository
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.viewmodel.RequestResponse
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AgentViewModel @Inject constructor():ViewModel() {
    //registerFarmer
    var registerFarmLivedata : MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    //buy cocoa
    var getAgentFloatLiveData : MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var requestFloatLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    //request float
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun requestFunds(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        requestFloatLiveData = MediatorLiveData()
        val results = AgentRequestRepository(RestClient.apiService).requestfund(json)
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> requestFloatLiveData.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> requestFloatLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                requestFloatLiveData,
                results.value
            )
        }

        return requestFloatLiveData
    }
    //request float
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun getAgentFloat():MediatorLiveData<ViewModelWrapper<String>>{
        getAgentFloatLiveData = MediatorLiveData()
        val results = AgentRequestRepository(RestClient.apiService).getAgentWalletBalance()
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> getAgentFloatLiveData.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> getAgentFloatLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respData(
                getAgentFloatLiveData,
                results.value
            )
        }

        return getAgentFloatLiveData
    }
}