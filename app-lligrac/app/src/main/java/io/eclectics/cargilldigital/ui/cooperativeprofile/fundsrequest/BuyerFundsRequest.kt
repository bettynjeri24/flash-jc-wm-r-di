package io.eclectics.cargilldigital.ui.cooperativeprofile.fundsrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.CoopFundRequestListAdapter
import io.eclectics.cargilldigital.databinding.FragmentBuyerFundsRequestBinding
import io.eclectics.cargilldigital.data.model.CoopFundsRequestList
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class BuyerFundsRequest : Fragment() {
    private var _binding: FragmentBuyerFundsRequestBinding? = null
    private val binding get() = _binding!!
    val coopViewModel: CooperativeViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var navOptions:NavOptions
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var  adapter : CoopFundRequestListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBuyerFundsRequestBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(
            resources.getString(R.string.fund_request),
            resources.getString(R.string.request_sttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        getBuyerFundsRequest()
       // setupListeners()

    }

    private fun getBuyerFundsRequest() {
        var endpoint  =  ApiEndpointObj.getFundsRequestList
        lookupJson = JSONObject()
        var userdatajson = UtilPreference().getUserData(requireActivity())
        var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)

        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        /* lookupJson.put("channelAcc",binding.etAccName.text.toString())
         lookupJson.put("accName",binding.etAccName.text.toString())
         lookupJson.put("channeName",accountProvider)*/
        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("phoneNumber",userData.phoneNumber)
        lookupJson.put("cooperativeid",userData.cooperativeId)
         lifecycleScope.launch {
            pdialog.show()
            coopViewModel.getCoopBuyerlist(lookupJson,endpoint,requireActivity()).observe(viewLifecycleOwner, Observer {
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
        var buyerList: List<CoopFundsRequestList> = NetworkUtility.jsonResponse(response)
        var productFilterSort = buyerList.toMutableList()
            .sortedByDescending { vanOrderList -> vanOrderList.dateOfRequest }
        //buyerList.filter { x->x.servicedStatus.contentEquals("1") }
         adapter = CoopFundRequestListAdapter(requireActivity(),CoopFundRequestListAdapter.FundsListListener { farmer, action ->
            handleAction(farmer, action)
        },productFilterSort)
        binding.rcvBuyers.adapter = adapter
    }

    private fun handleAction(buyerList: CoopFundsRequestList, action: String) {
        //pass data to approve
        var buyerListJson = NetworkUtility.getJsonParser().toJson(buyerList)
        var bundle = Bundle()
        bundle.putString("buyerobj",buyerListJson)
        findNavController().navigate(R.id.nav_coopApproveFundsRequest,bundle,navOptions)
    }
    /*private fun setupListeners() {
        binding.etSearch.addTextChangedListener {
            adapter.filter.filter(it.toString())
        }*/
   // }
}