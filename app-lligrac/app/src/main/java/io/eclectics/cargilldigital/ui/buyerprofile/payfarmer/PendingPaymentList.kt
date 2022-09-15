package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.adapter.PendingPaymentAdapter
import io.eclectics.cargilldigital.databinding.FragmentPendingPaymentListBinding
import io.eclectics.cargilldigital.data.model.BuyerPendingPayment
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.*
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class PendingPaymentList : Fragment() {
    private var _binding: FragmentPendingPaymentListBinding? = null
    private val binding get() = _binding!!

    val buyerViewModel: BuyerViewModel by viewModels()
    lateinit var lookupJson:JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    @Inject
    lateinit var navOptions: NavOptions
    lateinit var  adapter : PendingPaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_pending_payment_list, container, false)
        _binding = FragmentPendingPaymentListBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.pending_payments),resources.getString(R.string.pending_payments_subttl),binding.mainLayoutToolbar,requireActivity())

       // processChannelRequest(it.value)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      pdialog =  SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        getBuyerList()
    }

    private fun getBuyerList() {
        try {
            var endpoint = ApiEndpointObj.ffPendingPayment
            lookupJson = JSONObject()
            var userdatajson = UtilPreference().getUserData(requireActivity())
            var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)
           // setupListeners()

            lookupJson.put("userId", userData.providedUserId)
            lookupJson.put("buyerPhonenumber", userData.phoneNumber)
            lookupJson.put("cooperativeid", userData.cooperativeId)
            CoroutineScope(Dispatchers.Main).launch {
                pdialog.show()
                buyerViewModel.getFarmerPendingPayment(lookupJson, endpoint, requireActivity())
                    .observe(requireActivity(), Observer {
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
        var pendingList: List<BuyerPendingPayment> = NetworkUtility.jsonResponse(response)
        //var pendingList = BuyerPending.pendingList()

        adapter = PendingPaymentAdapter(requireActivity(),
            PendingPaymentAdapter.BuyerListListener { farmer, action ->
                handleAction(farmer, action)
            }, pendingList
        )
        binding.rvFarmers.adapter = adapter
        /*
        save pending payment list
         */
        CoroutineScope(Dispatchers.Main).launch {
            val buyerRoomModel : BuyerRoomViewModel by viewModels()
            buyerRoomModel.insertPendingPayment(requireActivity(),response)

        }
    }

    private fun handleAction(payment: BuyerPendingPayment, action: String) {
        var bundle = Bundle()
        var pendingPyamentJson = NetworkUtility.getJsonParser().toJson(payment)
        bundle.putString("payment",pendingPyamentJson)
        findNavController().navigate(R.id.nav_pending_paydetails,bundle,navOptions)
    }
}