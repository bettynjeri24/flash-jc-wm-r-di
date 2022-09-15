package io.eclectics.cargilldigital.ui.buyerprofile.fundrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentFundRequestBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.*
import io.eclectics.cargilldigital.viewmodel.BuyerViewModel
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject


@AndroidEntryPoint
class FundRequest : Fragment() {
    private var _binding: FragmentFundRequestBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions:NavOptions
    val buyerViewModel:BuyerViewModel by viewModels()
    lateinit var userData: UserDetailsObj
    @Inject
    lateinit var pdialog: SweetAlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFundRequestBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()

        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.buyer_fund_requests),resources.getString(R.string.fund_request_ttle),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)

        setListeners()
        binding.btnRequestFunds.setOnClickListener {
        if(isValidData()) {
            sendFundrequest()
        }
        }
    }

    private fun setListeners() {
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    private fun isValidData(): Boolean {
    var amount = binding.etRequestAmount.text.toString()
        var comments = binding.etComments.text.toString()
        if(!InputValidator.isValidAmount(amount)){
            binding.etRequestAmount.requestFocus()
            binding.tlAmount.error = resources.getString(R.string.enter_amount)
            return false
        }
        if(!InputValidator.isValidComments(comments)){
            binding.etComments.requestFocus()
            binding.tlComments.error = resources.getString(R.string.enter_comments)
            return false
        }
        return true
    }

    private fun sendFundrequest() {
        var endpoint  =  ApiEndpointObj.agentRequestFund
        var lookupJson = JSONObject()

        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        var transAmount = binding.etRequestAmount.text.toString()
        lookupJson.put("amountRequested",transAmount.toString())
        lookupJson.put("reasons",binding.etComments.text.toString())
        lookupJson.put("phonenumber",userData.phoneNumber)
        val i = 0
        val d1 = i.toDouble()
        val x = "${userData.walletBalance}"
        lookupJson.put("balanceOnRequest",x)
        lookupJson.put("cooperativeid",userData.cooperativeId)
        lookupJson.put("buyerIndex",userData.userIndex)
        lookupJson.put("coopIndex",userData.cooperativeIndex)
        lookupJson.put("buyerid",userData.userId)
        lookupJson.put("endPoint",endpoint)

        LoggerHelper.loggerError("double","$d1 and $x")

        var confirmationObj = ConfirmationObj(resources.getString(R.string.buyer_fund_requests),"$transAmount CFA","0 CFA",resources.getString(R.string.wallet_account),R.id.nav_buyerDashboard,"ft")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())

        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)

    }

    private fun processRequest(response: String) {
        LoggerHelper.loggerError("response","response $response")


    }

}