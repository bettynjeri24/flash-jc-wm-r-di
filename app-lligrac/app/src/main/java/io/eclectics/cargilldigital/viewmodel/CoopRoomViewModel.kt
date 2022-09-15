package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import io.eclectics.cargilldigital.data.repository.CooperativeRoomRepository
import io.eclectics.cargilldigital.network.RestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoopRoomViewModel @Inject constructor(): ViewModel() {
    suspend fun insertBuyersList(activity: FragmentActivity, json: String){
        GlobalScope.launch(Dispatchers.IO) {
            CooperativeRoomRepository(RestClient.apiService).saveBuyersList(activity, json)
        }
    }
}