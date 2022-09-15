package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentScannedFarmerDataBinding
import io.eclectics.cargilldigital.data.model.BuyerAccount
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.printer.PrinterDataAdapter
import io.eclectics.cargilldigital.ui.spinnermgmt.BuyerPaymentOptionSpn
import io.eclectics.cargilldigital.utils.CustomTextWatcher
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.BuyerViewModel
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class ScannedFarmerData : Fragment() {

    private var _binding: FragmentScannedFarmerDataBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BuyerPaymentOptionSpn
    @Inject
    lateinit var navOptions: NavOptions
    val buyerViewModel: BuyerViewModel by viewModels()
    lateinit var userData: UserDetailsObj
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var paymentReason:String
    lateinit var buyerDetails: List<String>
    var isScannedffReceipt by Delegates.notNull<Boolean>()
    lateinit  var lookupJson : JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result = bundle.getString("bundleKey")
            binding.layoutQrData.visibility = View.VISIBLE
            populateScannedData(result)
            // Do something with the result
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScannedFarmerDataBinding.inflate(inflater, container, false)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.pay_farmer),resources.getString(R.string.pay_or_scan),binding.mainLayoutToolbar,requireActivity())
        (activity as MainActivity?)!!.hideToolbar()
        isScannedffReceipt = false
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        var beneficiaryList = BuyerAccount.getPaymentOptions()
        //buyerDetails = List<String>
        binding.layoutScanQr.setOnClickListener {
            findNavController().navigate(R.id.nav_scan_ffpayment_qrcode)
        }
        setListeners()
        binding.btnPayFarmer.setOnClickListener {
            if(isValidFields()) {
                sendFundrequest()
            }
            //findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
        }

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

        try {
            var scannedString = requireArguments().getString("scanned")

             buyerDetails = scannedString!!.split("~").toTypedArray().map { it.trim() }
            binding.tvProviderName.text = "${buyerDetails[4]}"
            binding.tvAccountNo.text = resources.getString(R.string.mobile_number)+": ${buyerDetails[2]}"
            binding.tvChannelName.text = "Index: ${buyerDetails[1]}"
            binding.tvChannelType.text = ""
        }catch (ex:Exception){

        }
    }

    private fun isValidFields(): Boolean {
        var amount = binding.etAirtimeAmount.text.toString()
        var comments = binding.etComments.text.toString()
        var currentBalance = UtilPreference().getWalletBalance(requireActivity())
        if(!isScannedffReceipt){
            NetworkUtility().transactionWarning(resources.getString(R.string.ff_scan_pay),requireActivity())
            return false
        }
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

    private fun setListeners() {
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    private fun populateScannedData(scannedString: String?) {
        try {
            //var stringToGenerate = "$transID~$date~$amount~$time~$name"
            //var stringToGenerate = "$userId~$userIndex~$phoneNumber~$coopId~$name"
            isScannedffReceipt = true
            buyerDetails = scannedString!!.split("~").toTypedArray().map { it.trim() }
           // binding.tvFarmerName.text = "${resources.getString(R.string.name)}: ${buyerDetails[4]}"
            binding.tvTransId.text = buyerDetails[0]
            //binding.tvAmount.text = resources.getString(R.string.due_amount)+": ${buyerDetails[2]}"
           // binding.tvTime.text = "${buyerDetails[1]} | ${buyerDetails[3]} "
            binding.etAirtimeAmount.setText(buyerDetails[2])
        }catch (ex:Exception){

        }
    }

    private fun sendFundrequest() {
        var endpoint = ApiEndpointObj.buyerPayFarmer
         lookupJson = JSONObject()
        var transAmount = binding.etAirtimeAmount.text.toString()
        lookupJson.put("amount",transAmount.toString())
        lookupJson.put("reasons",binding.etComments.text.toString())
        lookupJson.put("phonenumber",userData.phoneNumber)
        lookupJson.put("coopId",userData.cooperativeId)
        lookupJson.put("farmForceRef","TXN454522545224")
        lookupJson.put("farmerId", buyerDetails[0])
        lookupJson.put("farmerPhonenumber",buyerDetails[2])
        lookupJson.put("cooperativeid",userData.cooperativeId)
        lookupJson.put("buyerid",userData.userId)
        lookupJson.put("paymentType",paymentReason)

        lookupJson.put("endPoint",endpoint)
        var farmerName = buyerDetails[4]
        lookupJson.put("farmername",farmerName)
        saveprintData()
        var confirmationObj = ConfirmationObj("${resources.getString(R.string.pay)}-$farmerName","$transAmount CFA","0 CFA",
            farmerName,R.id.nav_buyerDashboard,"payfarmer")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
    }
    //prepare printing
    private fun saveprintData() {
        var printerArrayList = ArrayList<PrinterDataAdapter.PrinterData>()
        var print1 = PrinterDataAdapter.PrinterData("Name \t\t:\t${lookupJson.getString("farmername")}",1,1,true)
        var print2 = PrinterDataAdapter.PrinterData("Phone Number\t:\t${lookupJson.getString("farmerPhonenumber")}",1,1,true)
        var print3 = PrinterDataAdapter.PrinterData("Amount \t\t:\t\t${lookupJson.getString("amount")}",1,1,true)
        var print4 = PrinterDataAdapter.PrinterData("Payment type \t:\t\t${lookupJson.getString("paymentType")}",1,1,true)
        var print5 = PrinterDataAdapter.PrinterData("Reason \t:\t${lookupJson.getString("reasons")}",1,1,true)

        printerArrayList.add(print1)
        printerArrayList.add(print2)
        printerArrayList.add(print3)
        printerArrayList.add(print4)
        printerArrayList.add(print5)
        var jsonList = NetworkUtility.getJsonParser().toJson(printerArrayList).toString()
        UtilPreference().setPrintData(requireActivity(),jsonList)
        UtilPreference.buyerUsername = "${userData.firstName} ${userData.lastName}"
        UtilPreference.userSection = "${userData.getSection().sectionName}"
        UtilPreference.sectionCode = ""
    }
}