package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.content.Intent
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
import io.eclectics.cargilldigital.databinding.FragmentPendingPaymentDetailsBinding
import io.eclectics.cargilldigital.data.model.BuyerPendingPayment
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.printer.PrinterActivity
import io.eclectics.cargilldigital.printer.PrinterDataAdapter
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.CooperativeViewModel
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class PendingPaymentDetails : Fragment() {
    private var _binding: FragmentPendingPaymentDetailsBinding? = null
    private val binding get() = _binding!!
    lateinit var pendingPayment:BuyerPendingPayment
    lateinit var lookupJson: JSONObject
    @Inject
    lateinit var pdialog: SweetAlertDialog
    @Inject
    lateinit var navOptions: NavOptions
    val coopViewModel: CooperativeViewModel by viewModels()
    lateinit var userData: UserDetailsObj
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_pending_payment_details, container, false)
        _binding = FragmentPendingPaymentDetailsBinding.inflate(inflater, container, false)
        // farmViewModel = ViewModelProvider(requireActivity()).get(FarmViewModel::class.java)
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.pay_farmer),resources.getString(R.string.pay_or_scan),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var paymentJson = requireArguments().getString("payment")
        pendingPayment = NetworkUtility.jsonResponse(paymentJson!!)
        populatePaymentInfo()
        binding.btnPayFarmer.setOnClickListener{payFarmerReq()}
    }

    private fun payFarmerReq() {

        if(validFields()){
        //var amount = binding.etPayAmount//etComments
            lookupJson = JSONObject()
            var userdatajson = UtilPreference().getUserData(requireActivity())
             userData = NetworkUtility.jsonResponse(userdatajson)
            var endpoint = ApiEndpointObj.payPendingffPayment
            // setupListeners()
            var transAmount = binding.etPayAmount.text.toString()
            lookupJson.put("userId", userData.providedUserId)
            lookupJson.put("buyerPhonenumber", userData.phoneNumber)
            lookupJson.put("cooperativeid", userData.cooperativeId)
            lookupJson.put("endPoint",endpoint)
            lookupJson.put("reasons",binding.etComments.text.toString())
            lookupJson.put("amount", binding.etPayAmount.text.toString())
            lookupJson.put("buyerId",userData.userId)
            lookupJson.put("farmerPhonenumber",pendingPayment.farmerphonenumber)
            lookupJson.put("paymentType", pendingPayment.paymentDescription)
            lookupJson.put("farmForceRef",pendingPayment.payementrefcode)

            var farmerName = "${pendingPayment.firstName} ${pendingPayment.lastName}"
            lookupJson.put("farmername",  "${pendingPayment.firstName} ${pendingPayment.lastName}")
        LoggerHelper.loggerError("farmdata","data $farmerName - and ")
            var confirmationObj = ConfirmationObj("${resources.getString(R.string.pay)}-$farmerName","$transAmount CFA","0 CFA",
                farmerName,R.id.nav_buyerDashboard,"payfarmer")
            saveprintData(lookupJson)
            var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
            var bundle = Bundle()
            bundle.putString("confirm",json)
            bundle.putString("endPoint",endpoint)
            bundle.putString("requestJson",lookupJson.toString())
            //TODO CONFIRM THIS
            findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navOptions)
            //printtest()
        }
    }
// BYPASS PIN TO TEST PRINTING
    fun printtest(){
    var intent = Intent(requireActivity(), PrinterActivity::class.java)
    intent.putExtra("printerdata","rttrer")
    intent.putExtra("isSettings",false)
    requireActivity().startActivity(intent)
    }
    private fun saveprintData(requestjson: JSONObject) {
        var printerArrayList = ArrayList<PrinterDataAdapter.PrinterData>()
        var print1 = PrinterDataAdapter.PrinterData("Name \t\t:\t${requestjson.getString("farmername")}",1,1,true)
        var print2 = PrinterDataAdapter.PrinterData("Phone Number\t:\t${lookupJson.getString("farmerPhonenumber")}",1,1,true)
        var print3 = PrinterDataAdapter.PrinterData("Amount \t\t:\t\t${lookupJson.getString("amount")}",1,1,true)
        var print4 = PrinterDataAdapter.PrinterData("Payment type \t:\t\t${lookupJson.getString("paymentType")}",1,1,true)
        var print5 = PrinterDataAdapter.PrinterData("Reason \t:\t${requestjson.getString("reasons")}",1,1,true)

        printerArrayList.add(print1)
        printerArrayList.add(print2)
        printerArrayList.add(print3)
        printerArrayList.add(print4)
        printerArrayList.add(print5)
         var jsonList = NetworkUtility.getJsonParser().toJson(printerArrayList)
        UtilPreference().setPrintData(requireActivity(),jsonList)
        UtilPreference.buyerUsername = "${userData.firstName} ${userData.lastName}"
        UtilPreference.userSection = "${userData.getSection().sectionName}"
        UtilPreference.sectionCode = userData.getSection().sectionName
    }


    private fun validFields(): Boolean {

        return true

    }

    private fun populatePaymentInfo() {
    binding.tvCustName.text = "${pendingPayment.firstName} ${pendingPayment.lastName}"
        binding.tvOutType.text = "Type: ${pendingPayment.paymentDescription}"
        binding.tvTotalAmount.text = "${resources.getString(R.string.total_amount)}: ${NetworkUtility().cashFormatter(pendingPayment.fullAmount)}"
        binding.tvPaidAmount.text = "${resources.getString(R.string.paid_amount)}: ${NetworkUtility().cashFormatter(pendingPayment.amountPaid)}"
        var balance = pendingPayment.fullAmount.toInt() - pendingPayment.amountPaid.toInt()
        binding.tvBalance.text = "${resources.getString(R.string.balance_amount)}: ${NetworkUtility().cashFormatter(balance.toString())}"
        binding.tvReferenceNumber.text = "Ref No: ${pendingPayment.payementrefcode}"
        binding.tvDate.text = "Date: ${pendingPayment.date}"

       /* if()*/
        //date
    }
}