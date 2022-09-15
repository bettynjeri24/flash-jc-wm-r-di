package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

object UserLatestTransaction {
    /**
     * get last transaction sent to recipient phone no
     */
    lateinit var mediatordata: MediatorLiveData<ViewModelWrapper<String>>
    fun getTransaction(context: FragmentActivity, fundsReqJson: JSONObject): MediatorLiveData<ViewModelWrapper<String>> {
        var mediatordata = MediatorLiveData<ViewModelWrapper<String>>()
        var generalViewModel = ViewModelProvider(context).get(GeneralViewModel::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            generalViewModel.getRecentTransactions(fundsReqJson,ApiEndpointObj.recentTransactions,context).observe(context, Observer {
                mediatordata.value = it
            })


        }
        return mediatordata

    }
}