package io.eclectics.cargill.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.agritech.cargill.repositories.MainRequestRepository
import io.eclectics.agritech.cargill.responsewrapper.ResultWrapper
import io.eclectics.cargilldigital.viewmodel.RequestResponse
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class FarmViewModel @Inject constructor():ViewModel() {

    //registerFarmer
    var registerFarmLivedata : MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    //buy cocoa
    var buyCocoaLivedata : MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmersListLivedata: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var buyAirtimeLivedata: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    var farmerWalletbalLiveData:MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()
    //buycocoa
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun sendFarmRegRequest(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        registerFarmLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).registerFarmer(json)
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> registerFarmLivedata.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> registerFarmLivedata.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                registerFarmLivedata,
                results.value
            )
        }

        return registerFarmLivedata
    }
    //buycocoa
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun sendBuyCocoaRequest(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        buyCocoaLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).buycocoa(json)
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> buyCocoaLivedata.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> buyCocoaLivedata.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                buyCocoaLivedata,
                results.value
            )
        }

        return buyCocoaLivedata
    }
    //buy airtime
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun buyAirtimeReq(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        buyAirtimeLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).buyAirtimeReq(json)
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> buyAirtimeLivedata.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> buyAirtimeLivedata.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respDataMessage(
                buyAirtimeLivedata,
                results.value
            )
        }

        return buyAirtimeLivedata
    }
    //merchcant payment
    /**
     * submit login data
     * @return MediatorLiveData<GeneralResponse>
     */
    suspend fun merchantPaymentReq(json: JSONObject):MediatorLiveData<ViewModelWrapper<String>>{
        buyAirtimeLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).merchantPayReq(json)
       return RequestResponse.extractUpdateLiveDataMsg(buyAirtimeLivedata, results)
    }
    suspend fun getFarmerList(): MediatorLiveData<ViewModelWrapper<String>> {
        farmersListLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).getFarmerList()
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> farmersListLivedata.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> farmersListLivedata.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respData(farmersListLivedata, results.value)
        }

        return farmersListLivedata
    } //farmerWalletbalLiveData
    suspend fun getFarmerWalletBalance(): MediatorLiveData<ViewModelWrapper<String>> {
        farmerWalletbalLiveData = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).getFarmerWalletBalance()
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> farmerWalletbalLiveData.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> farmerWalletbalLiveData.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respData(
                farmerWalletbalLiveData,
                results.value
            )
        }

        return farmerWalletbalLiveData
    }
    suspend fun getFarmerTransaction(): MediatorLiveData<ViewModelWrapper<String>> {
        farmersListLivedata = MediatorLiveData()
        val results = MainRequestRepository(RestClient.apiService).getFarmerTransaction()
        //LoggerHelper.loggerSuccess("fetchoutlets",results.toString()+"data")
        when(results){
            is ResultWrapper.NetworkError -> farmersListLivedata.value =
                ViewModelWrapper.error("No Internet connection check your network and try again")
            is ResultWrapper.GenericError -> farmersListLivedata.value =
                ViewModelWrapper.error("Error $results")
            is ResultWrapper.Success -> RequestResponse.respData(farmersListLivedata, results.value)
        }

        return farmersListLivedata
    }

    //extract the response and update livedata accordingly

}