package io.eclectics.cargilldigital.ui.buyerprofile.fundrequest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.BuyerDashboardFundsAdapter
import io.eclectics.cargilldigital.databinding.FragmentFundsRequestHistoryBinding
import io.eclectics.cargilldigital.data.model.CoopFundsRequestList
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.BuyerRoomViewModel
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class FundsRequestHistory : Fragment() {
   private var _binding:FragmentFundsRequestHistoryBinding? = null
    private val binding  get() = _binding!!
    val coopViewModel: CooperativeViewModel by viewModels()
    val buyerViewmodel:BuyerRoomViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var navOptions: NavOptions
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var  adapter : BuyerDashboardFundsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_funds_request_history, container, false)
        _binding = FragmentFundsRequestHistoryBinding.inflate(inflater,container,false)
        ToolBarMgmt.setToolbarTitle(
            resources.getString(R.string.request),
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
        setupListeners()

    }

    private fun getBuyerFundsRequest() {
        var endpoint  =  ApiEndpointObj.buyerTopupRequest
        lookupJson = JSONObject()
        var userdatajson = UtilPreference().getUserData(requireActivity())
        var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)

        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        /* lookupJson.put("channelAcc",binding.etAccName.text.toString())
         lookupJson.put("accName",binding.etAccName.text.toString())
         lookupJson.put("channeName",accountProvider)*/

        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("buyeruniqueid",userData.userId)
        lookupJson.put("cooperativeid",userData.cooperativeId)
         lifecycleScope.launch {
            pdialog.show()
            coopViewModel.getBuyerFundsRequest(lookupJson,endpoint,requireActivity()).observe(requireActivity(), Observer {
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

        //buyerList.filter { x->x.servicedStatus.contentEquals("1") }
        adapter = BuyerDashboardFundsAdapter(requireActivity(),BuyerDashboardFundsAdapter.FundsListListener { farmer, action ->
            handleAction(farmer, action)
        },buyerList)
        binding.rcvBuyers.adapter = adapter
        /**
         * save buyer fudns request list
         */
         lifecycleScope.launch {
            buyerViewmodel.insertFundsRequestList(requireActivity(),response)
        }
    }

    private fun handleAction(buyerList: CoopFundsRequestList, action: String) {
        //pass data to approve
        var buyerListJson = NetworkUtility.getJsonParser().toJson(buyerList)
        var bundle = Bundle()
        bundle.putString("buyerobj",buyerListJson)
        findNavController().navigate(R.id.nav_coopApproveFundsRequest,bundle,navOptions)
    }
    private fun setupListeners() {
        binding.etSearch.addTextChangedListener {
            adapter.filter.filter(it.toString())
        }
    }


}