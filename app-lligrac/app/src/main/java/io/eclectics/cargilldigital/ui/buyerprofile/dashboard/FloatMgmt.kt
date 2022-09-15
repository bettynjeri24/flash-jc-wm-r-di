package io.eclectics.cargilldigital.ui.buyerprofile.dashboard

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.viewmodel.AgentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

object FloatMgmt {
    lateinit var mediatordata: MediatorLiveData<ViewModelWrapper<String>>
    lateinit var agentViewModel: AgentViewModel
    fun requestFunds(context: FragmentActivity,fundsReqJson:JSONObject): MediatorLiveData<ViewModelWrapper<String>> {
        mediatordata = MediatorLiveData<ViewModelWrapper<String>>()
        agentViewModel = ViewModelProvider(context).get(AgentViewModel::class.java)


         GlobalScope.launch {
            agentViewModel.requestFunds(fundsReqJson).observe(context, Observer {
                mediatordata.value = it
            })


        }
        return mediatordata
    }
}