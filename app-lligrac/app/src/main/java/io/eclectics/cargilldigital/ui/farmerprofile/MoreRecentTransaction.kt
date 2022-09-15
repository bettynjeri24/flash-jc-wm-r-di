package io.eclectics.cargilldigital.ui.farmerprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.MoreRecentTransactionAdapter
import io.eclectics.cargilldigital.databinding.FragmentMorerecentTransactionBinding
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargilldigital.viewmodel.GeneralViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargill.model.FarmerTransaction
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class MoreRecentTransaction : Fragment() {
    private var _binding: FragmentMorerecentTransactionBinding? = null
    private val binding  get() = _binding!!
    val coopViewModel: CooperativeViewModel by viewModels()
    val genViewModel: GeneralViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var navOptions: NavOptions
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var  adapter : MoreRecentTransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMorerecentTransactionBinding.inflate(inflater,container,false)
        ToolBarMgmt.setToolbarTitle(
            resources.getString(R.string.recent_transactions),
            resources.getString(R.string.recent_transactions),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //ste passed rcv recent data
        var initRecentTrans = requireArguments().getString("recenttrans")
        if(initRecentTrans!!.isNotBlank()){
            showInitialList(initRecentTrans)
        }else {
            getRecentTranaction()
        }
    }
    private fun showInitialList(floatResp: String) {

        LoggerHelper.loggerError("respobnse","resp $floatResp")
        var listdata:List<FarmerTransaction> = NetworkUtility.jsonResponse(floatResp)
        if(listdata.isNotEmpty()) {
           val adapter = MoreRecentTransactionAdapter(MoreRecentTransactionAdapter.TransactionListener { action ->
                handleAction( action)
            },listdata)
          /*  val adapter = MoreRecentTransactionAdapter(TransactionListener { collection ->

            }, listdata)*/
            binding.rcvRecentTransaction.adapter = adapter
        }



    }


    private fun getRecentTranaction() {
        var endpoint  =  ApiEndpointObj.recentTransactions
        lookupJson = JSONObject()
        var userdatajson = UtilPreference().getUserData(requireActivity())
        var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)

        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("phoneNumber",userData.phoneNumber)

         lifecycleScope.launch {
            pdialog.show()
            genViewModel.getRecentTransactions(lookupJson,endpoint,requireActivity()).observe(requireActivity(), Observer {
                pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processFundsRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }
    }

    private fun processFundsRequest(response: String) {
        var buyerList: List<FarmerTransaction> = NetworkUtility.jsonResponse(response)

        //buyerList.filter { x->x.servicedStatus.contentEquals("1") }
        adapter = MoreRecentTransactionAdapter(MoreRecentTransactionAdapter.TransactionListener {  action ->
            handleAction( action)
        },buyerList)
        adapter.notifyDataSetChanged()
        binding.rcvRecentTransaction.adapter = adapter

    }

    private fun handleAction(action: FarmerTransaction) {

    }

}