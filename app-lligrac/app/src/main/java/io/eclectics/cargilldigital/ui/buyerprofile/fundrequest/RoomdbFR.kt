package io.eclectics.cargilldigital.ui.buyerprofile.fundrequest

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.repository.BuyerRoomRepository
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.network.RestClient
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility

object RoomdbFR {
    suspend fun getBuyerFundsRequest(buyerFundsrequest: MediatorLiveData<ViewModelWrapper<String>>,
                                  activity: FragmentActivity
    ){//:MediatorLiveData<ViewModelWrapper<String>>
        //withContext(Dispatchers.IO) {
        var listToBEObserved = BuyerRoomRepository(RestClient.apiService).getFundsRequestList(activity)
        if (!listToBEObserved.hasActiveObservers()) {
            listToBEObserved.observe(activity, Observer {
                if (it.isNotEmpty()) {
                    // if (listToBEObserved.value!! == it)
                    LoggerHelper.loggerError("offlineCoolersts", "offline buyerfuns request ")
                    var generalResponce = NetworkUtility.getJsonParser().toJson(it)
                    buyerFundsrequest.value = ViewModelWrapper.response(generalResponce)
                    buyerFundsrequest.removeObservers(activity)
                    listToBEObserved.removeObservers(activity)


                } else {
                    LoggerHelper.loggerError("offlineFarmers", "offline farmers empty ")
                    buyerFundsrequest.value =
                        ViewModelWrapper.error(activity.resources.getString(R.string.no_internet))
                }
            })
            // }
        }
        //return stockRefLivedata
    }

}