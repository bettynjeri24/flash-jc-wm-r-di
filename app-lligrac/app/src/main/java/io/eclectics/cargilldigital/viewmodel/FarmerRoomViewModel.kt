package io.eclectics.cargilldigital.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.data.repository.FarmerRoomRepository
import io.eclectics.cargilldigital.network.RestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FarmerRoomViewModel @Inject constructor(): ViewModel() {
    //FarmerRoomRepository
    var farmerChannelListLiveData: MediatorLiveData<ViewModelWrapper<String>> = MediatorLiveData()

    suspend fun insertChannelList(activity: FragmentActivity, json: String){
        GlobalScope.launch(Dispatchers.IO) {
            FarmerRoomRepository(RestClient.apiService).saveChannelList(activity, json)
        }
    }
    suspend fun insertBeneficiaryAccList(activity: FragmentActivity, json: String){
        GlobalScope.launch(Dispatchers.IO) {
            FarmerRoomRepository(RestClient.apiService).saveBeneficiaryAccList(activity, json)
        }
    }

    private fun modifyNullAccElement(account: FarmerAccount.BeneficiaryAccObj) {
        var modifiedBenArray = ArrayList<FarmerAccount.BeneficiaryAccObj>()
        modifiedBenArray = ArrayList()
        when(account.channelId){
            1-> {
                account.cvc = "Null"
                account.expiryDate = "Null"
            }
        }
    }
}