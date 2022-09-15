package io.eclectics.cargilldigital.ui.cooperativeprofile.evalue

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import io.eclectics.cargilldigital.databinding.FragmentEbookingBinding
import io.eclectics.cargilldigital.data.model.ChargesModel
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.CoopBank
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject
import kotlin.Exception
import kotlin.math.roundToInt
import kotlin.properties.Delegates

@AndroidEntryPoint
class EbookingFragment : Fragment() {
    private var _binding: FragmentEbookingBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var navOptions:NavOptions
    val coopViewModel: CooperativeViewModel by viewModels()
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var userData: UserDetailsObj
    lateinit var bankData:CoopBank
    lateinit var  textWatcher:TextWatcher
    var transactionCharges by Delegates.notNull<Double>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEbookingBinding.inflate(inflater, container, false)
        //return inflater.inflate(R.layout.fragment_cooperative_dashboard, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.evalue_booking),resources.getString(R.string.fund_booking),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        var userdatajson = UtilPreference().getUserData(requireActivity())
         userData = NetworkUtility.jsonResponse(userdatajson)
        populateBank()


        addTextWatcherhere()
        binding.btnBookEvalue.setOnClickListener {
            if(fieldValidated()){
                bookCoopEvalue()
            }
            //findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
        }
    }

    private fun addTextWatcherhere() {
         textWatcher = object : TextWatcher {
            override
            fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {

            }

            override
            fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override
            fun afterTextChanged(editable: Editable?) {
                if (editable.toString().isNotBlank()) {//editable != null && !editable.toString().equals("")
                    // Checking editable.hashCode() to understand which edittext is using right now
                    if (binding.etAmount!!.text.hashCode() === editable.hashCode()) {
                        // This is just an example, your magic will be here!
                        val value = editable.toString()
                        binding.etAmount.removeTextChangedListener(this)
                        //binding.etAmount.setText(value)
                        setCharge(value)
                        binding.etAmount.addTextChangedListener(this)
                    }
                    if (binding.etReceiveAmount!!.text.hashCode() === editable.hashCode()) {
                        // This is just an example, your magic will be here!
                        val value = editable.toString()
                        binding.etReceiveAmount.removeTextChangedListener(this)
                        //binding.etReceiveAmount.setText(value)
                        setTotalAmount(value)
                        binding.etReceiveAmount.addTextChangedListener(this)
                    }
                } /*else if (binding.etReceiveAmount!!.text.hashCode() === editable.hashCode()) {
                    // This is just an example, your magic will be here!
                    val value = editable.toString()
                    binding.etReceiveAmount.removeTextChangedListener(this)
                    //binding.etReceiveAmount.setText(value)
                    setTotalAmount(value)
                    binding.etReceiveAmount.addTextChangedListener(this)
                }*/
            }
        }
        binding.etReceiveAmount.addTextChangedListener(textWatcher)
        binding.etAmount.addTextChangedListener(textWatcher)
    }

    private fun setTotalAmount(text: CharSequence?) {
        LoggerHelper.loggerError("setCurrentAmt","set current amount $text")
        if (text.isNullOrBlank()){
            transactionCharges = 0.0
        }else{
            binding.etAmount.removeTextChangedListener(textWatcher)
            var amount = getIntAmount(text.toString())
            var totalAMount = ChargesModel.getTotalBookingAmount(amount)
            transactionCharges =totalAMount - amount
            binding.etAmount.setText(totalAMount.toString())
            binding.transChargeAmount.text =NetworkUtility().doubleCashFormatter(transactionCharges.toString())
        }
    }

    private fun setCharge(text: CharSequence?) {
        LoggerHelper.loggerError("amout","amount $text")
       // binding.etReceiveAmount.removeTextChangedListener(this)
        if (text.isNullOrBlank()){
            transactionCharges = 0.0
        }else{
           // try {
            binding.etReceiveAmount.removeTextChangedListener(textWatcher)
            var amount = getIntAmount(text.toString())
                transactionCharges = ChargesModel.getChargesAmount(amount)
                var receiveAmount = amount - transactionCharges
                binding.etReceiveAmount.setText(receiveAmount.toString())
                binding.transChargeAmount.text =
                    NetworkUtility().doubleCashFormatter(transactionCharges.toString())
           // }catch (ex:Exception){LoggerHelper.loggerError("amoutErr","err ${ex.message}")}
        }
    }
    fun getIntAmount(amount:String): Int{
        return amount.toDouble().roundToInt()


    }

    private fun populateBank() {
        try{
             bankData=NetworkUtility.jsonResponse(userData.bankAccount.toString())
            LoggerHelper.loggerError("details","acc: ${bankData.accountName}")
           // binding.etAccountName.setText(bankData.accountName)
            var maskedAcc = GlobalMethods().simpleMasking(bankData.accountNumber,4,4)//bankData.accountNumber
            //binding.etAccNumber.setText(maskedAcc)
        }catch (ex:Exception){
            LoggerHelper.loggerError("eroro",ex.message+"null")
        }
    }

    private fun bookCoopEvalue() {
        var endpoint  =  ApiEndpointObj.coopBookEvalue

        lookupJson = JSONObject()


        lookupJson.put("accountName",bankData.accountName)
        lookupJson.put("accountNumber",bankData.accountNumber)
        lookupJson.put("amountBooked",binding.etAmount.text.toString())
        //lookupJson.put("bookingFee","")
        lookupJson.put("coopId",userData.cooperativeId)
        lookupJson.put("bankIndex",bankData.id)
        lookupJson.put("bookedBy","${userData.userId}")
        lookupJson.put("reasonForBooking",binding.etComments.text.toString())
        lookupJson.put("endPoint",endpoint)


        var confirmationObj = ConfirmationObj(resources.getString(R.string.evalue_booking),binding.etAmount.text.toString(),transactionCharges.toString(),bankData.accountName,R.id.nav_farmerDashboard,"ebooking")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
       /*  lifecycleScope.launch {
            pdialog.show()
            coopViewModel.getCoopBuyerlist(lookupJson,endpoint,requireActivity()).observe(requireActivity(), Observer {
                pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processBookingRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }*/
    }

    private fun processBookingRequest(response: String) {

    }


    private fun fieldValidated(): Boolean {

        return true
    }
}
