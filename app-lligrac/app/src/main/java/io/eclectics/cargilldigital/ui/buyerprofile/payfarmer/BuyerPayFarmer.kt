package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentBuyerPayFarmerBinding
import io.eclectics.cargilldigital.data.model.BuyerAccount
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.ui.spinnermgmt.BuyerPaymentOptionSpn
import io.eclectics.cargilldigital.utils.CustomTextWatcher
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.BuyerViewModel
import io.eclectics.cargill.model.FarmerModelObj
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class BuyerPayFarmer : Fragment() {
    private var _binding: FragmentBuyerPayFarmerBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BuyerPaymentOptionSpn
    @Inject
    lateinit var navOptions: NavOptions
    val buyerViewModel: BuyerViewModel by viewModels()
    lateinit var userData: UserDetailsObj
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var paymentReason:String
    lateinit var farmerObj:FarmerModelObj
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBuyerPayFarmerBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        //setToolbarTitle("Transfer to telco","Transfer to other mobile provider")
         return binding.root
        // return inflater.inflate(R.layout.fragment_transfer_tobank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var farmerObjJson = requireArguments().getString("farmerobj")
         farmerObj = NetworkUtility.jsonResponse(farmerObjJson!!)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.assign_evoucher),"${farmerObj.firstName} ${farmerObj.lastName}",binding.mainLayoutToolbar,requireActivity())
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        setListeners()

        binding.btnPayFarmer.setOnClickListener {
            if(idValidFields()) {
                sendFundrequest()
            }
            //findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
        }

        var beneficiaryList = BuyerAccount.getPaymentOptions()
        spnAdapter = BuyerPaymentOptionSpn(requireActivity(), beneficiaryList)
        binding.spinnerProvider.adapter = spnAdapter
        binding.spinnerProvider.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    var paymentOption = spnAdapter.getItem(position)
                    paymentReason = paymentOption.optionName
                }
            }
    }

    private fun idValidFields(): Boolean {
        var amount = binding.etAirtimeAmount.text.toString()
        var comments = binding.etComments.text.toString()
        var currentBalance = UtilPreference().getWalletBalance(requireActivity())
        if(!InputValidator.isValidAmount(amount)){
            binding.etAirtimeAmount.requestFocus()
            binding.tlAmount.error = resources.getString(R.string.enter_amount)
            return false
        }
        if(!InputValidator.isValidComments(comments)){
            binding.etComments.requestFocus()
            binding.tlComments.error = resources.getString(R.string.enter_comments)
            return false
        }
        if(amount.isNotEmpty()){
            if(amount.toInt() > currentBalance.toInt()){
                NetworkUtility().transactionWarning(resources.getString(R.string.inssufficient_funds),requireActivity())
                return false
            }
        }
        return true

    }

    private fun sendFundrequest() {
        var endpoint = ApiEndpointObj.buyerPayFarmer
        var lookupJson = JSONObject()
        var transAmount = binding.etAirtimeAmount.text.toString()
        lookupJson.put("amount",transAmount.toString())
        lookupJson.put("reasons",binding.etComments.text.toString())
        lookupJson.put("phonenumber",userData.phoneNumber)
        lookupJson.put("coopId",userData.cooperativeId)
        lookupJson.put("coopIndex",userData.userIndex)
        lookupJson.put("farmerId",farmerObj.id.toString())
        lookupJson.put("farmerPhonenumber",farmerObj.phoneNumber)
        lookupJson.put("cooperativeid",userData.cooperativeId)
        lookupJson.put("buyerid",userData.userId)
        lookupJson.put("paymentType",paymentReason)

        lookupJson.put("endPoint",endpoint)
        var farmerName = "${farmerObj.firstName} ${farmerObj.lastName}"
        var confirmationObj = ConfirmationObj("${resources.getString(R.string.pay)}-$farmerName","$transAmount CFA","0 CFA",
            farmerName,R.id.nav_buyerDashboard,"payfarmer")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
    }
    private fun setListeners() {
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }
}