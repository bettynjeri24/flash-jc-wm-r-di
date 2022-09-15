package io.eclectics.cargilldigital.ui.cooperativeprofile.evalue

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
import io.eclectics.cargilldigital.adapter.BookingEvalueAdapter
import io.eclectics.cargilldigital.databinding.FragmentEvalueBookingBinding
import io.eclectics.cargilldigital.data.model.CoopEvalue
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
class EvalueBooking : Fragment() {
    private var _binding: FragmentEvalueBookingBinding? = null
    private val binding get() = _binding!!
    val coopViewModel: CooperativeViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    @Inject
    lateinit var navOptions:NavOptions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //rcvEbooking
        _binding = FragmentEvalueBookingBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.evalue_booking),resources.getString(R.string.evalue_list),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabBooking.setOnClickListener{
            findNavController().navigate(R.id.nav_bookEvalueFragment)
        }

        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        getBookedElist()
    }

    private fun getBookedElist() {
        try {
            var endpoint = ApiEndpointObj.coopBookedEvalueList
            lookupJson = JSONObject()
            var userdatajson = UtilPreference().getUserData(requireActivity())
            var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)

            lookupJson.put("id", userData.userId)
            lookupJson.put("index", userData.userIndex)
            lookupJson.put("coopId", userData.cooperativeId)
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
                            is ViewModelWrapper.response -> processEbookList(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                            //processRequest(it.value)//
                        }
                    })
            }
        }catch (ex:Exception){}
    }

    private fun processEbookList(response: String) {
        var buyerList: List<CoopEvalue.EvalueList> = NetworkUtility.jsonResponse(response)
        var productFilterSort = buyerList.toMutableList()
            .sortedByDescending { bookinglist -> bookinglist.dateOfBooking }
        val adapter = BookingEvalueAdapter(requireActivity(),BookingEvalueAdapter.BookedEvalueListListener { productFilterSort, action ->
            handleAction(productFilterSort, action)
        },productFilterSort)

        binding.rcvEbooking.adapter =  adapter
    }

    private fun handleAction(evalue: CoopEvalue.EvalueList, action: String) {
        var ebookObjJson = NetworkUtility.getJsonParser().toJson(evalue)
        var bundle = Bundle()
        bundle.putString("evalue",ebookObjJson)
        findNavController().navigate(R.id.nav_approveEvalue,bundle,navOptions)
    }
}