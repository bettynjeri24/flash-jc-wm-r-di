package io.eclectics.cargilldigital.ui.confirmation

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
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentConfirmAddchanelAccountBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.SendMoney
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.FarmerViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmAddChanelAccount : Fragment() {
    private var _binding: FragmentConfirmAddchanelAccountBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var pdialog: SweetAlertDialog
    val farmViewModel: FarmerViewModel by viewModels()
    lateinit var jsonChannel:String
    lateinit var requestJson:String
    @Inject
    lateinit var navOptions:NavOptions
    lateinit var channelObj:SendMoney.ChannelListObj
    lateinit var userData: UserDetailsObj
    lateinit var endpoint:String
    lateinit var  confObj: ConfirmationObj
    lateinit var confirmationTitle:String
    lateinit var debitTitle:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_confirm_addchanel_account, container, false)
        _binding = FragmentConfirmAddchanelAccountBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var confirmJson = requireArguments().getString("confirm")
         endpoint  =  requireArguments().getString("endPoint")!!
        confirmationTitle = requireArguments().getString("confirmTitle")!!
         confObj = NetworkUtility.jsonResponse(confirmJson!!)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
         jsonChannel = requireArguments().getString("channel")!!
         requestJson = requireArguments().getString("requestjson")!!
        debitTitle = requireArguments().getString("debitTitle")!!
        processConfirmation(confObj)

        binding.confirmTransactionTitle.text = confirmationTitle
        binding.btnSubmit.setOnClickListener {
            // findNavController().navigate(R.id.nav_pinFragment)
            when(confObj.transType){
                "removeAcc" ->sendremoverequest()
                "addAccount" ->sendaddBeneficiaryAccount()
            }

        }
    }

    private fun sendremoverequest() {
        var transReqjson = JSONObject(requestJson)
        transReqjson.put("endPoint",endpoint)
        var bundle = Bundle()
        bundle.putString("requestJson",requestJson)
        bundle.putInt("destination",R.id.nav_farmerDashboard)
        findNavController().navigate(R.id.nav_pinFragment,bundle,navOptions)
    }

    private fun sendaddBeneficiaryAccount() {
        if (channelObj.type.contentEquals("Telco")||channelObj.type.contentEquals("Aggregator")) {
            sendAddChannelrequest()
        } else {
            var transReqjson = JSONObject(requestJson)
            transReqjson.put("endPoint",endpoint)
            var bundle = Bundle()
            bundle.putString("requestJson",requestJson)
            bundle.putInt("destination",R.id.nav_farmerDashboard)
            findNavController().navigate(R.id.nav_pinFragment,bundle,navOptions)
        }
    }

    private fun processConfirmation(confObj: ConfirmationObj) {
        when(confObj.transType){
            "removeAcc" ->removeBeneficiaryAccount()
            "addAccount" ->addBeneficiaryAccount()
        }
    }

    private fun addBeneficiaryAccount() {
        channelObj = NetworkUtility.jsonResponse(jsonChannel!!)
        binding.tvTitle.text = confObj.title
        binding.amountTextView.text  =confObj.amount
        binding.transactionCostTextView.text = confObj.charges
        binding.transactionCostDesTextView.text = resources.getString(R.string.operator)
        binding.exciseDutyAmountTextView.text = confObj.recipient
        binding.exciseDutyAmountDesTextView.text = resources.getString(R.string.name)
        binding.etDebitAccount.text = confObj.recipient
        binding.showBalanceTextView.text = debitTitle
        //debitTitle
    }

    private fun removeBeneficiaryAccount() {
        binding.tvTitle.text = confObj.title
        binding.amountTextView.text  =confObj.amount
        binding.transactionCostTextView.text = confObj.charges
        binding.exciseDutyAmountTextView.text = confObj.amount
        binding.transactionCostDesTextView.text = resources.getString(R.string.channel_name)
        binding.exciseDutyAmountDesTextView.text = resources.getString(R.string.channel_number)
        binding.etDebitAccount.text = confObj.recipient
        binding.btnSubmit.text = resources.getString(R.string.remove_account)
    }

    private fun sendAddChannelrequest() {
       //ApiEndpointObj.addBeneficiaryAcc
        var userJson  = UtilPreference().getUserData(activity)
         userData= NetworkUtility.jsonResponse(userJson)


        var lookupJson = JSONObject()
      /*  var accountNumber = requireArguments().getString("accountNumber")
        var accountName = requireArguments().getString("accountName")

        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("channelNumber",accountNumber)
        lookupJson.put("channelName",channelObj.channelName)
        lookupJson.put("accName",accountName)
        lookupJson.put("channelType",channelObj.type)
        lookupJson.put("accountholderphonenumber",userData.phoneNumber)*/
        var transReqjson = JSONObject(requestJson)
        transReqjson.put("pin","")
        //lookupJson.put("pin","")
// "pin":"12345"
         lifecycleScope.launch {
            pdialog.show()
            farmViewModel.addBeneficiaryAccReq(transReqjson,endpoint!!,requireActivity()).observe(requireActivity(), Observer {
                pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }

    }

    private fun processRequest(respose: String) {
        //depending on channel type redirect to otp or success
        LoggerHelper.loggerError("response","response $respose")
        var addResponse: SendMoney.AddbeneficiaryResponse = NetworkUtility.jsonResponse(respose)
        var bundle = Bundle()

        var transRequestJson = JSONObject()
        transRequestJson.put("newNumber",addResponse.newNumber)
        transRequestJson.put("accountholderphonenumber",userData.phoneNumber)
        transRequestJson.put("endPoint",ApiEndpointObj.verifyAddTelcoAccount)
        bundle.putString("recipientNumber",addResponse.newNumber)
        bundle.putString("requestJson",transRequestJson.toString())
        bundle.putInt("destination",R.id.nav_farmerDashboard)
        bundle.putString("response",respose)
        findNavController().navigate(R.id.nav_verifyOtp,bundle,navOptions)

    }
}
    //nav_pinFragment
    //return inflater.inflate(R.layout.fragment_confirmation, container, false)



