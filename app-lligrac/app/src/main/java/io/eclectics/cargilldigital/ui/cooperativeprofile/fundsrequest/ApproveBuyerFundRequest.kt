package io.eclectics.cargilldigital.ui.cooperativeprofile.fundsrequest

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.DialogDeclineFtopupBinding
import io.eclectics.cargilldigital.databinding.FragmentApproveBuyerFundrequestBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.CoopFundsRequestList
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class ApproveBuyerFundRequest : Fragment() {
    private var _binding: FragmentApproveBuyerFundrequestBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions:NavOptions
    lateinit var fundsObj: CoopFundsRequestList
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentApproveBuyerFundrequestBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.approve_funds_request_title),resources.getString(R.string.allocate_money),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get buyer fundsRequest obj
        var fundsJson = requireArguments().getString("buyerobj")
         fundsObj = NetworkUtility.jsonResponse(fundsJson!!)
        populateUI()
        /*var confirmationObj = ConfirmationObj("Approve Funds Request","10000 CFA","0 CFA","Evans Gangla",R.id.nav_farmerDashboard,"approvefunds")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)*/
        binding.btnDecline.setOnClickListener {
            if(!InputValidator.isValidComments(binding.etComments.text.toString())){
                binding.tlComments.error = resources
                    .getString(R.string.enter_comments)
            }else {
                var buyerName = "${fundsObj.firstName} ${fundsObj.lastName}"
                showDialog(buyerName)
            }
        }
        binding.btnApproveFunds.setOnClickListener {
            if(validInput()) {
                var userdatajson = UtilPreference().getUserData(requireActivity())
                var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)
                var endpoint = ApiEndpointObj.approveFundsRequest
                var lookupJson = JSONObject()
                var transAmount = binding.etApproveAmount.text.toString()
                lookupJson.put("amount",transAmount.toString())
                lookupJson.put("reasons",binding.etComments.text.toString())

                lookupJson.put("coopId",userData.cooperativeId)
                lookupJson.put("buyerId",fundsObj.buyerid)
                lookupJson.put("phonenumber",fundsObj.phonenumber)
                lookupJson.put("username",userData.userId)
                lookupJson.put("endPoint",endpoint)

                var confirmationObj = ConfirmationObj("${resources.getString(R.string.approve)}-${fundsObj.phonenumber}","$transAmount CFA","0 CFA",
                   "${ fundsObj.firstName} ${ fundsObj.lastName}",R.id.nav_cooperative_dashboard,"approveft")
                var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
                var bundle = Bundle()
                bundle.putString("confirm",json)
                bundle.putString("endPoint",endpoint)
                bundle.putString("requestJson",lookupJson.toString())

                findNavController().navigate(R.id.nav_transactionConfirmation, bundle, navOptions)
            }
        }
    }

    private fun validInput(): Boolean {
        var validAmount = binding.etApproveAmount.text.toString()
        if(!InputValidator.isValidAmount(validAmount)){
            binding.tlApprovedAmount.error = resources
                .getString(R.string.validation_amount)
            return false
        }

        if(!InputValidator.isValidComments(binding.etComments.text.toString())){
            binding.tlComments.error = resources
                .getString(R.string.enter_comments)
            return false
        }

        return true
    }

    private fun populateUI() {
        binding.etName.setText("${ fundsObj.firstName} ${ fundsObj.lastName}")
        binding.etRequestedAmt.setText(fundsObj.amountRequested.toString())
        binding.etApproveAmount.setText(fundsObj.amountRequested.toString())
    }

    fun showDialog(buyerName: String) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE) // before

        val layoutInflater = LayoutInflater.from(context)
        var binding = DialogDeclineFtopupBinding.inflate(layoutInflater)
       // dialog?.setContentView(R.layout.dialog_decline_ftopup)

        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog?.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
       // lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.setContentView(binding.root)
        var rejectmsg = resources.getString(R.string.decline_topup_title)
        var bymsg = resources.getString(R.string.confirm_decline)
        var message = "$rejectmsg ${fundsObj.amountRequested} $bymsg $buyerName"
       binding.tvMessage.text = message
       // dialog.findViewById<TextView>(R.id.tvMessage). = message
        //binding.btnOk.text = "Proceed"
         var btnok: MaterialButton = dialog?.findViewById(R.id.btn_ok)!!
        btnok.setOnClickListener {
            dialog.dismiss()
            refectFundsRequest()
           // findNavController().navigate(R.id.nav_coolerOnboarding,null,navOptions)
        }
       // binding.btnCancel.setBackgroundColor(resources.getColor(R.color.reddish))
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
            //TODO ONCE ALL OUTLETS ARE ONBOARDED , USE THIS POPSTACK SINCE THE FRAGMENT WILL BE IN STACK
            //findNavController().popBackStack(R.id.nav_salesMenu,false)
            //findNavController().navigate(R.id.nav_salesMenu,null,navOptions)
        }

        dialog.show()

    }

    //var showGeneralDialog = DeclineDialog.showDialog(requireActivity(),"","",refectFundsRequest())
   // showGeneralDialog.
    private fun refectFundsRequest() {
        var userdatajson = UtilPreference().getUserData(requireActivity())
        var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)
        var endpoint = ApiEndpointObj.declineFundsRequest
        var lookupJson = JSONObject()
        var transAmount = binding.etApproveAmount.text.toString()
        lookupJson.put("amount",transAmount.toString())
        lookupJson.put("bookingID",fundsObj.bookingId)
        lookupJson.put("reasons",binding.etComments.text.toString())
        lookupJson.put("endPoint",endpoint)
        var bundle = Bundle()
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_pinFragment,bundle,navOptions)
    }
}