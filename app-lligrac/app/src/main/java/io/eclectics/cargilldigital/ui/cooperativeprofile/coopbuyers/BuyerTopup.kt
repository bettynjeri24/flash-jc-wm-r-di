package io.eclectics.cargilldigital.ui.cooperativeprofile.coopbuyers

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
import io.eclectics.cargilldigital.databinding.FragmentBuyerTopupBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.CoopBuyer
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.InputValidator.isValidAmount
import io.eclectics.cargilldigital.utils.InputValidator.isValidComments
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class BuyerTopup : Fragment() {
    private var _binding: FragmentBuyerTopupBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions:NavOptions
    val coopViewModel: CooperativeViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var buyer:CoopBuyer.BuyerList
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBuyerTopupBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.top_up_title),resources.getString(R.string.allocate_money_sbtt),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*var confirmationObj = ConfirmationObj(resources.getString(R.string.top_up_buyer),"1 0000 CFA","0 CFA","Evans Gangla",R.id.nav_farmerDashboard,"fundrequest")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)*/
        var buyerJson = requireArguments().getString("buyer")
        buyer = NetworkUtility.jsonResponse(buyerJson!!)
        binding.etName.setText("${buyer.firstName} ${buyer.lastName}")
        binding.btnApproveEvalue.setOnClickListener {
            if(amountValidated()){
                approveFundRequest()
            }
            //findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
        }
    }

    private fun approveFundRequest() {
        var endpoint  =  ApiEndpointObj.individualBuyerTopup
        lookupJson = JSONObject()
        var userdatajson = UtilPreference().getUserData(requireActivity())
        var userData: UserDetailsObj = NetworkUtility.jsonResponse(userdatajson)
        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("phoneNumber",userData.phoneNumber)
        lookupJson.put("coopId",userData.cooperativeId)
        lookupJson.put("coopIndex",userData.userIndex)
        lookupJson.put("buyerIndex",buyer.id.toString())
        lookupJson.put("buyerId",buyer.cocoaBuyerId)
        lookupJson.put("amount",binding.etTopUpAmount.text.toString())
        lookupJson.put("phonenumber",buyer.phoneNumber)
        lookupJson.put("endPoint",endpoint)

        var confirmationObj = ConfirmationObj(resources.getString(R.string.funds_topup),binding.etTopUpAmount.text.toString(),"0","${buyer.firstName} ${buyer.lastName}",R.id.nav_farmerDashboard,"fundstopup")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)

    }

    private fun amountValidated(): Boolean {
    var amount = binding.etTopUpAmount.text.toString()
        var comments = binding.etComments.text.toString()
        var currentBalance = UtilPreference().getWalletBalance(requireActivity())

        if(!isValidAmount(amount)){
            binding.etTopUpAmount.requestFocus()
            binding.tlAmount.error = resources.getString(R.string.enter_amount)
            return false
        }
        if(amount.isNotEmpty()){
            if(amount.toInt() > currentBalance.toInt()){
                NetworkUtility().transactionWarning(resources.getString(R.string.inssufficient_funds),requireActivity())
                return false
            }
        }
        if(!isValidComments(comments)){
            binding.etComments.requestFocus()
            binding.tlComments.error = resources.getString(R.string.enter_comments)
            return false
        }
        return true
    }
}