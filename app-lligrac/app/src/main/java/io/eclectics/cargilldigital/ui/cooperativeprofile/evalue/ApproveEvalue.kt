package io.eclectics.cargilldigital.ui.cooperativeprofile.evalue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentApproveEvalueBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.CoopEvalue
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class ApproveEvalue : Fragment() {
    private var _binding: FragmentApproveEvalueBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions: NavOptions
    val coopViewModel: CooperativeViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var evalueObj: CoopEvalue.EvalueList


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentApproveEvalueBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.evalue_booking),resources.getString(R.string.aprove_evalue_sbbt),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var requestjson = requireArguments().getString("evalue")
        evalueObj = NetworkUtility.jsonResponse(requestjson!!)
        binding.tvRequestedBy.text = "${resources.getString(R.string.booked_by)}: ${evalueObj.bookedBy}"
        binding.tvAccountNo.text = "${resources.getString(R.string.account_number)}: ${evalueObj.accountNumber}"
        binding.tvChannelName.text =evalueObj.accountName
        binding.etEvalueAmount.setText(NetworkUtility().cashFormatter(evalueObj.amountBooked.toString()))
        binding.etComments.setText(evalueObj.reasonForBooking)
        binding.btnBookEvalue.setOnClickListener {
            if(fieldValidated()){
                approveEvalue()
            }
        }
    }

    private fun approveEvalue() {
        var endpoint  =  ApiEndpointObj.coopApproveEvalue
        lookupJson = JSONObject()
        var userdatajson = UtilPreference().getUserData(requireActivity())
        var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)

        lookupJson.put("id",evalueObj.id.toString())
        lookupJson.put("index",userData.userIndex)
        lookupJson.put("coopId",userData.cooperativeId)
        lookupJson.put("transactionid",evalueObj.bookingId)
        lookupJson.put("otp",binding.etOtp.text.toString())
        lookupJson.put("endPoint",endpoint)
        var confirmationObj = ConfirmationObj(resources.getString(R.string.aprove_evalue_confirmation),evalueObj.amountBooked.toString(),"0",evalueObj.amountBooked.toString(),R.id.nav_farmerDashboard,"eapproveevalue")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
    }

    private fun fieldValidated(): Boolean {

        var otp = binding.etOtp.text.toString()
        if(!InputValidator.isValidOTP(otp)){
            binding.tlOtp.error = resources.getString(R.string.please_enter_digit_otp)
            return false
        }
        return true
    }


}