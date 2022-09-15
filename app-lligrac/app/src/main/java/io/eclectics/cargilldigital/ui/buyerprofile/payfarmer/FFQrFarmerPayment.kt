package io.eclectics.cargilldigital.ui.buyerprofile.payfarmer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentFfqrFarmerPaymentBinding
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
import io.eclectics.cargill.model.FarmerModelObj
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class FFQrFarmerPayment : Fragment() {
    var _binding: FragmentFfqrFarmerPaymentBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BuyerPaymentOptionSpn

    @Inject
    lateinit var navoption: NavOptions
    lateinit var userData: UserDetailsObj

    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var paymentReason: String
    lateinit var farmerObj: FarmerModelObj
    lateinit var buyerDetails: List<String>
    var isScannedffReceipt by Delegates.notNull<Boolean>()
    lateinit var lookupJson: JSONObject
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

    private fun populateScannedData(scannedString: String?) {
        try {
            //var stringToGenerate = "$transID~$date~$amount~$time~$name"
            //var stringToGenerate = "$userId~$userIndex~$phoneNumber~$coopId~$name"
            setScanDataVisible()
            isScannedffReceipt = true
            buyerDetails = scannedString!!.split("~").toTypedArray().map { it.trim() }
            binding.tvProviderName.text =
                "${resources.getString(R.string.name)}: ${buyerDetails[4]}"
            binding.tvTransId.text = "${buyerDetails[0]}"
            binding.etRefNo.setText(buyerDetails[0])
            binding.tvAmount.text = ": ${NetworkUtility().cashFormatter(buyerDetails[2])}"
            binding.tvTime.text = "${buyerDetails[1]} | ${buyerDetails[3]} "
            binding.etAirtimeAmount.setText(buyerDetails[2])
        } catch (ex: Exception) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ffqr_farmer_payment, container, false)
        _binding = FragmentFfqrFarmerPaymentBinding.inflate(inflater, container, false)
        isScannedffReceipt = false
        return binding.root
        // return inflater.inflate(R.layout.fragment_transfer_tobank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var farmerObjJson = requireArguments().getString("farmerobj")
        farmerObj = NetworkUtility.jsonResponse(farmerObjJson!!)
        ToolBarMgmt.setToolbarTitle(
            resources.getString(R.string.pay_farmer_title),
            "${farmerObj.firstName} ${farmerObj.lastName}",
            binding.mainLayoutToolbar,
            requireActivity()
        )
        var userJson = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        setListeners()
        hideScanData()
        binding.layoutScanQr.setOnClickListener {
            findNavController().navigate(R.id.nav_scan_ffpayment_qrcode)
        }
        /*  binding.rgPaymentMode.setOnCheckedChangeListener{ _groupId,_id, ->
              when(_id){
                  R.id.rdb_full ->{ binding.tvAmountTitle.visibility = View.GONE
                  binding.tlAmount.visibility = View.GONE}
                  R.id.rdb_partial ->{binding.tvAmountTitle.visibility = View.VISIBLE
                      binding.tlAmount.visibility = View.VISIBLE}
              }
          }*/

        binding.btnPayFarmer.setOnClickListener {
            if (idValidFields()) {
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

    private fun hideScanData() {
        ///Payer un producteur
        binding.tvProviderName.visibility =
            View.GONE//.text = "${resources.getString(R.string.name)}: ${buyerDetails[4]}"
        binding.tvTransId.visibility = View.GONE//
        //binding.etRefNo.visibility = View.GONE//
        binding.tvAmount.visibility =
            View.GONE//.text = ": ${NetworkUtility().cashFormatter(buyerDetails[2])}"
        binding.tvTime.visibility = View.GONE//.text = "${buyerDetails[1]} | ${buyerDetails[3]} "
        // binding.etAirtimeAmount.visibility = View.GONE//
        binding.tvScanAmountTitle.visibility = View.GONE
        binding.tvTransIdScanTitle.visibility = View.GONE
    }

    private fun setScanDataVisible() {
        ///Payer un producteur
        binding.tvProviderName.visibility =
            View.VISIBLE//.text = "${resources.getString(R.string.name)}: ${buyerDetails[4]}"
        binding.tvTransId.visibility = View.VISIBLE//
        // binding.etRefNo.visibility = View.VISIBLE//
        binding.tvAmount.visibility =
            View.VISIBLE//.text = ": ${NetworkUtility().cashFormatter(buyerDetails[2])}"
        binding.tvTime.visibility = View.VISIBLE//.text = "${buyerDetails[1]} | ${buyerDetails[3]} "
        //binding.etAirtimeAmount.visibility = View.VISIBLE//
        binding.tvScanAmountTitle.visibility = View.VISIBLE
        binding.tvTransIdScanTitle.visibility = View.VISIBLE
    }

    private fun idValidFields(): Boolean {
        var amount = binding.etAirtimeAmount.text.toString()
        var comments = binding.etComments.text.toString()
        var currentBalance = UtilPreference().getWalletBalance(requireActivity())
        //MAKING SCANNING QR CODE OPTIONAL
        /*if(!isScannedffReceipt){
            NetworkUtility().transactionWarning(resources.getString(R.string.ff_scan_pay),requireActivity())
            return false
        }*/
        if (!InputValidator.isValidAmount(amount)) {
            binding.etAirtimeAmount.requestFocus()
            binding.tlAmount.error = resources.getString(R.string.enter_amount)
            return false
        }
        if (!InputValidator.isValidComments(comments)) {
            binding.etComments.requestFocus()
            binding.tlComments.error = resources.getString(R.string.enter_comments)
            return false
        }
        if (amount.isNotEmpty()) {
            if (amount.toInt() > currentBalance.toInt()) {
                NetworkUtility().transactionWarning(
                    resources.getString(R.string.inssufficient_funds),
                    requireActivity()
                )
                return false
            }
        }
        return true

    }

    private fun sendFundrequest() {
        var endpoint = ApiEndpointObj.buyerPayFarmer
        lookupJson = JSONObject()
        var transAmount = binding.etAirtimeAmount.text.toString()
        lookupJson.put("amount", transAmount.toString())
        lookupJson.put("reasons", binding.etComments.text.toString())
        lookupJson.put("phonenumber", userData.phoneNumber)
        lookupJson.put("coopId", userData.cooperativeId)
        lookupJson.put("coopIndex", userData.userIndex)
        lookupJson.put("farmerId", farmerObj.id.toString())
        lookupJson.put("farmerPhonenumber", farmerObj.phoneNumber)
        lookupJson.put("cooperativeid", userData.cooperativeId)
        lookupJson.put("farmForceRef", "TXN454522545224")
        lookupJson.put("buyerid", userData.userId)
        lookupJson.put("paymentType", paymentReason)
        lookupJson.put("endPoint", endpoint)
        var farmerName = "${farmerObj.firstName} ${farmerObj.lastName}"
        lookupJson.put("farmername", farmerName)
        saveprintData()
        var confirmationObj = ConfirmationObj(
            "${resources.getString(R.string.pay)}-$farmerName", "$transAmount CFA", "0 CFA",
            farmerName, R.id.nav_buyerDashboard, "payfarmer"
        )
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm", json)
        bundle.putString("endPoint", endpoint)
        bundle.putString("requestJson", lookupJson.toString())
        findNavController().navigate(R.id.nav_transactionConfirmation, bundle, navoption)
    }

    private fun setListeners() {
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    //prepare printing
    private fun saveprintData() {
        var printerArrayList = ArrayList<PrinterDataAdapter.PrinterData>()
        var print1 = PrinterDataAdapter.PrinterData(
            "Name \t\t:\t${lookupJson.getString("farmername")}",
            1,
            1,
            true
        )
        var print2 = PrinterDataAdapter.PrinterData(
            "Phone Number\t:\t${lookupJson.getString("farmerPhonenumber")}",
            1,
            1,
            true
        )
        var print3 = PrinterDataAdapter.PrinterData(
            "Amount \t\t:\t\t${lookupJson.getString("amount")}",
            1,
            1,
            true
        )
        var print4 = PrinterDataAdapter.PrinterData(
            "Payment type \t:\t\t${lookupJson.getString("paymentType")}",
            1,
            1,
            true
        )
        var print5 = PrinterDataAdapter.PrinterData(
            "Reason \t:\t${lookupJson.getString("reasons")}",
            1,
            1,
            true
        )

        printerArrayList.add(print1)
        printerArrayList.add(print2)
        printerArrayList.add(print3)
        printerArrayList.add(print4)
        printerArrayList.add(print5)
        var jsonList = NetworkUtility.getJsonParser().toJson(printerArrayList).toString()
        UtilPreference().setPrintData(requireActivity(), jsonList)
        UtilPreference.buyerUsername = "${userData.firstName} ${userData.lastName}"
        UtilPreference.userSection = "${userData.getSection().sectionName}"
        UtilPreference.sectionCode = userData.getSection().sectionName
    }
}