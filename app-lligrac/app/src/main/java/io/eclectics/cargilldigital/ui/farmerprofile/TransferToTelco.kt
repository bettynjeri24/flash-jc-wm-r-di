package io.eclectics.cargilldigital.ui.farmerprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentTransferTotelcoBinding
import io.eclectics.cargilldigital.data.model.*
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.ui.spinnermgmt.BeneficiaryChannelSpinner
import io.eclectics.cargilldigital.utils.CustomTextWatcher
import io.eclectics.cargilldigital.utils.InputValidator.isValidAmount
import io.eclectics.cargilldigital.utils.InputValidator.isValidComments
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class TransferToTelco : Fragment() {
    private var _binding: FragmentTransferTotelcoBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter:BeneficiaryChannelSpinner
    @Inject
    lateinit var navOption:NavOptions
    lateinit var userData: UserDetailsObj
    var transactionCharges by Delegates.notNull<Int>()
    //var balance
    lateinit var  acc:FarmerAccount.BeneficiaryAccObj
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTransferTotelcoBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
       // setToolbarTitle("Transfer to telco","Transfer to other mobile provider")
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.transfer_totelco),resources.getString(R.string.transfer_totelco_subttle),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)

        //ic_arrow_up
        var bundleAccount = requireArguments().getString("account")
         acc= NetworkUtility.jsonResponse(bundleAccount!!)
        var beneficiaryList = FarmerAccount.providerList()
        binding.tvAccountNo.setText("Account: ${acc.channelNumber}")
        binding.tvProviderName.text = acc.beneficiaryName
        binding.tvChannelType.text = acc.channelType
        binding.tvChannelName.text = acc.channelName
       var img = SendMoney.getImageResource(acc.channelName)
        binding.imgChannel.setImageResource(img)
     // var mylist:List< FarmerAccount.BeneficiaryAccObj>= acc.toList()
        var mylist=  arrayListOf(acc).toList()
        setListeners()


        binding.etAirtimeAmount.doOnTextChanged { text, start, before, count ->
            setCharge(text)
        }



        binding.btnBuyAirtime.setOnClickListener{
            if(isvalidField()) {
                transferToTelcoRequest()
            }
        }


    }

    private fun isvalidField(): Boolean {
        var amount = binding.etAirtimeAmount.text.toString()
        var comments = binding.etComments.text.toString()
        var currentBalance = UtilPreference().getWalletBalance(requireActivity())
        if(!isValidAmount(amount)){
            binding.etAirtimeAmount.requestFocus()
            binding.tlAmount.error = resources.getString(R.string.enter_amount)
            return false
        }
        if(!isValidComments(comments)){
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

    private fun setListeners() {
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    private fun transferToTelcoRequest() {
        var transAmount = binding.etAirtimeAmount.text.toString()
        var endpoint  =  ApiEndpointObj.transferToTelco
        var lookupJson = JSONObject()
        lookupJson.put("farmerPhone",userData.phoneNumber)
        lookupJson.put("amount",transAmount)
        lookupJson.put("cashoutChannel",acc.channeAbbreviation)
        lookupJson.put("cashoutNumber",acc.channelNumber)
        lookupJson.put("channelId",acc.channelId)
        lookupJson.put("beneficiaryId",acc.id)
        lookupJson.put("userIndex",userData.userIndex)

        lookupJson.put("reasons",binding.etComments.text.toString())
        //lookupJson.put("phonenumber",userData.phoneNumber)
        val i = 0
        val d1 = i.toDouble()
        val x = "${userData.walletBalance}"
        lookupJson.put("balanceOnRequest",x)
        lookupJson.put("cooperativeid",userData.cooperativeId)
        lookupJson.put("buyerid",userData.userId)
        lookupJson.put("endPoint",endpoint)

        LoggerHelper.loggerError("double","$d1 and $x")

        var confirmationObj = ConfirmationObj(resources.getString(R.string.transfer_to_telco_wallet),"${transAmount} CFA","$transactionCharges CFA","${acc.beneficiaryName}-${acc.channelName}",R.id.nav_farmerDashboard,"fttelco")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOption)
    }

    private fun setCharge(text: CharSequence?) {
        if (text.isNullOrBlank()){
        binding.tvCharges.text = "${resources.getString(R.string.available_balance)}: 0"
            binding.tvTotalAmount.visibility = View.INVISIBLE
        }else{
            transactionCharges = ChargesModel.getCharges(text.toString().toInt())
            binding.tvCharges.text = "Charges: $transactionCharges"
          //  binding.tvTotalAmount.text = "${resources.getString(R.string.float_balnce)}: "+(transactionCharges+text.toString().toInt()).toString()
           // binding.tvTotalAmount.visibility = View.VISIBLE
        }
    }

    fun setToolbarTitle(title:String,description:String){
        val toolBar =  binding.mainLayoutToolbar
        binding.mainLayoutToolbar.toolbar.visibility = View.VISIBLE
        toolBar.toolbarTitle.text = title
        toolBar.toolbarDescription.text = description
        toolBar.toolbarCancel.setOnClickListener {
            (activity as MainActivity?)!!.navigationMgmt()
        }
        //layoutToolbar.visibility = View.VISIBLE

    }
}