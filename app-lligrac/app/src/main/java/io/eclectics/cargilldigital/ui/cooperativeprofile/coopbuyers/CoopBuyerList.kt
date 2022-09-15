package io.eclectics.cargilldigital.ui.cooperativeprofile.coopbuyers

import android.os.Bundle
import android.text.Html
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
import io.eclectics.cargilldigital.adapter.CoopBuyersListAdapter
import io.eclectics.cargilldigital.databinding.FragmentCoopBuyerListBinding
import io.eclectics.cargilldigital.data.model.CoopBuyer
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CoopRoomViewModel
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class CoopBuyerList : Fragment() {
    private var _binding: FragmentCoopBuyerListBinding? = null
    private val binding get() = _binding!!
    val coopViewModel:CooperativeViewModel by viewModels()
    val coopRoomViewModel:CoopRoomViewModel by viewModels()
    lateinit var lookupJson:JSONObject
    @Inject
    lateinit var pdialog:SweetAlertDialog
    @Inject
    lateinit var navOptions:NavOptions
    lateinit var  adapter : CoopBuyersListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoopBuyerListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(
            resources.getString(R.string.coop_buyers),
            resources.getString(R.string.request_sttle),
            binding.mainLayoutToolbar,
            requireActivity()
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get buyers list
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        getBuyerList()

    }

    private fun getBuyerList() {
        try {
            var endpoint = ApiEndpointObj.coopBuyersList
            lookupJson = JSONObject()
            var userdatajson = UtilPreference().getUserData(requireActivity())
            var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)
            setupListeners()

            lookupJson.put("userId", userData.providedUserId)
            lookupJson.put("phoneNumber", userData.phoneNumber)
            lookupJson.put("cooperativeid", userData.cooperativeId)
             lifecycleScope.launch {
                pdialog.show()
                coopViewModel.getCoopBuyerlist(lookupJson, endpoint, requireActivity())
                    .observe(viewLifecycleOwner, Observer {
                        pdialog.dismiss()
                        when (it) {
                            is ViewModelWrapper.error -> GlobalMethods().transactionWarning(
                                requireActivity(),
                                "${it.error}"
                            )//LoggerHelper.loggerError("error","error")
                            is ViewModelWrapper.response -> processChannelRequest(it.value)
                        }
                    })
            }
        }catch (ex:Exception){}
    }

    private fun processChannelRequest(response: String) {
        var buyerList: List<CoopBuyer.BuyerList> = NetworkUtility.jsonResponse(response)

         adapter = CoopBuyersListAdapter(requireActivity(),CoopBuyersListAdapter.BuyerListListener { farmer, action ->
            handleAction(farmer, action)
        },buyerList)
        binding.rcvBuyers.adapter = adapter
        binding.tvFarmerNo.text = Html.fromHtml(
            getString(
                R.string.total_no,
                GlobalMethods().getColoredSpanned(buyerList.size.toString(), "#000000")
            )
        )

         lifecycleScope.launch {
            //insertChannelList
            coopRoomViewModel.insertBuyersList(requireActivity(),response)
        }
       // binding.rcvBuyers.adapter = adapter
    }

    private fun handleAction(buyer: CoopBuyer.BuyerList, action: String) {
//handle individual top up request
        //TODO HANDLE  BUYER TOP UP REQUEST
        var buyerjson = NetworkUtility.getJsonParser().toJson(buyer)
        var bundle = Bundle()
        bundle.putString("buyer",buyerjson);
        findNavController().navigate(R.id.nav_buyerTopUp,bundle,navOptions)
    }
    private fun setupListeners() = binding.etSearch.addTextChangedListener {
        adapter.filter.filter(it.toString())
    }

}