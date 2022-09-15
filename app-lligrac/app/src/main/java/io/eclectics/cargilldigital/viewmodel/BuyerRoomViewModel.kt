package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.cargilldigital.data.repository.BuyerRoomRepository
import io.eclectics.cargilldigital.network.RestClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyerRoomViewModel @Inject constructor(): ViewModel(){

    suspend fun insertPendingPayment(activity: FragmentActivity,json:String){
        CoroutineScope(Dispatchers.Main).launch {
            BuyerRoomRepository(RestClient.apiService).savePendingBuyerRequestList(activity, json)
        }
    }

    suspend fun insertFarmerList(activity: FragmentActivity,json: String){
        GlobalScope.launch(Dispatchers.IO) {
            BuyerRoomRepository(RestClient.apiService).saveFarmerList(activity, json)
        }
    }

    suspend fun insertFundsRequestList(activity: FragmentActivity,json: String){
        GlobalScope.launch(Dispatchers.IO) {
            BuyerRoomRepository(RestClient.apiService).saveFundsRequestList(activity, json)
        }
    }
}