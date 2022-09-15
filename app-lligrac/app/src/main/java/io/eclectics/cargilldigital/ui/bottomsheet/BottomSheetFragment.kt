package io.eclectics.cargilldigital.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.R

import io.eclectics.cargilldigital.databinding.FragmentBottomSheetBinding
import io.eclectics.cargilldigital.ui.buyerprofile.dashboard.FloatMgmt
import io.eclectics.cargilldigital.ui.buyerprofile.dashboard.UserLatestTransaction
import io.eclectics.cargill.model.FarmersModel
import io.eclectics.cargill.network.networkCallback.AgentCallback
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargill.viewmodel.FarmViewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 * Use the [BottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BottomSheetFragment(
    private val mCallback: OnActionTaken,
    farmer: FarmersModel?,
    action: String
) :
    BottomSheetDialogFragment(), OnActionTaken {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val theFarmer: FarmersModel? = farmer
    private val theAction: String = action
    lateinit var farmViewModel: FarmViewModel
    lateinit var pDialog: SweetAlertDialog
    private val globalMethods = GlobalMethods()
    lateinit var genderStr:String
    lateinit var regionStr:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        farmViewModel = ViewModelProvider(requireActivity()).get(FarmViewModel::class.java)
        pDialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        return binding.root
    }

    override fun onActionChosen(value: String?) {
        mCallback.onActionChosen(value)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //wallet source
        val srcWallet = requireContext().resources!!.getStringArray(R.array.moneySource)
        val srcAdapter =
            context?.let {
                ArrayAdapter<String>(
                    it,
                    R.layout.spinner_layout,
                    R.id.tvProductName,
                    srcWallet
                )
            }
        binding.spnWalletSource.setAdapter(srcAdapter)

        when (theAction) {
            "farm_details" -> handleDetails()
            "add_farmer" -> ""//addfarmer()
            "pay" -> handlePay()
            "request_float" -> handleFloat()
            "payment_option" -> handlePaymentOption()
            "showFloat" -> showWalletFloat()
        }

    }

    private fun handlePaymentOption() {
        binding.clDetails.visibility = View.GONE
        binding.clPay.visibility = View.GONE
        binding.clAddFarmer.visibility = View.GONE
        binding.clFloat.visibility = View.GONE
        binding.clPaymentOptions.visibility = View.VISIBLE
        binding.clFloatBalance.visibility = View.GONE

       /* val adapter = PaymentAdapter(PaymentListener { payment ->
            mCallback.onActionChosen(payment.paymentName)
        })*/

        //binding.rvPaymentOptions.adapter = adapter
    }

    fun showWalletFloat(){
       // cl_floatBalance
        binding.clDetails.visibility = View.GONE
        binding.clPay.visibility = View.GONE
        binding.clAddFarmer.visibility = View.GONE
        binding.clPaymentOptions.visibility = View.GONE
        binding.clFloat.visibility = View.GONE
        binding.clFloatBalance.visibility = View.VISIBLE
        var totalWallet= NetworkUtility().cashFormatter(UtilPreference().getWalletBalance(requireContext()))
        var totalCash= NetworkUtility().cashFormatter(UtilPreference().getcashfloat(requireContext()))
        var totalFloat= NetworkUtility().cashFormatter(UtilPreference().getFloatBalance(requireContext()))
        binding.tvTotalWallet.text = "Total wallet amount:$totalWallet"//tv_floatAmount
        //tv_totalWallet Total wallet amount:45452
        //Total Cash amount:45452  tvTotalcash
        //Total Float Balance  tv_floatAmount
        binding.tvTotalcash.text = "Total Cash amount:$totalCash"
        binding.tvFloatAmount.text = "Total Float amount:$totalFloat"

    }
    private fun handleFloat() {
        binding.clDetails.visibility = View.GONE
        binding.clPay.visibility = View.GONE
        binding.clAddFarmer.visibility = View.GONE
        binding.clPaymentOptions.visibility = View.GONE
        binding.clFloat.visibility = View.VISIBLE
        binding.clFloatBalance.visibility = View.GONE

        binding.btnRequest.setOnClickListener {
           // globalMethods.loader(requireActivity())
            //formulate float request
            if(binding.etFloatAmount.text!!.isNotEmpty()) {
                var loggedUserJson = UtilPreference().getLoggedAgent(context)
                var agentUserObj: AgentCallback.LoginCallback = NetworkUtility.jsonResponse(loggedUserJson)
                var transactionJson = JSONObject()
               transactionJson.put("id", 0)
                transactionJson.put( "agentid", agentUserObj.agentNo)
                transactionJson.put( "date", NetworkUtility().dateToUTC())
                transactionJson.put("amount", binding.etFloatAmount.text.toString())
                transactionJson.put("reason", binding.etFloatComments.text.toString())
                transactionJson.put("requestid", "")
                transactionJson.put("recieved", "")
                transactionJson.put("cooperative", agentUserObj.cooperativeId)
                NetworkUtility().sendRequest(pDialog)
                FloatMgmt.requestFunds(requireActivity(),transactionJson).observe(this, Observer {
                    pDialog.dismiss()
                    when (it) {
                        is ViewModelWrapper.error -> {
                          GlobalMethods().transactionWarning(requireActivity(), it.error)
                        }
                        is ViewModelWrapper.response -> {
                            //processResponse(it.value)
                            mCallback.onActionChosen("request_float")
                        }
                    }
                })
            }else{
                GlobalMethods().transactionWarning(requireActivity(),"Enter float amount")
            }
            /* lifecycleScope.launch {
                delay(3000)
                mCallback.onActionChosen("request_float")
            }*/
        }

        binding.btnCancelRequest.setOnClickListener {
            mCallback.onActionChosen("cancel")
        }
    }

    /*private fun processResponse(value: String) {
        LoggerHelper.loggerError("responseMsg","message $value")


    }*/

   /* private fun addfarmer() {
        binding.clDetails.visibility = View.GONE
        binding.clPay.visibility = View.GONE
        binding.clFloat.visibility = View.GONE
        binding.clPaymentOptions.visibility = View.GONE
        binding.clAddFarmer.visibility = View.VISIBLE
        binding.clFloatBalance.visibility = View.GONE
        //farm data

        binding.apply {
            cpp.setCountryForPhoneCode(+225)
            cpp.showFlag(true)
            cpp.showNameCode(false)
        }


        val salutationsList = requireContext().resources!!.getStringArray(R.array.salutations)
        val salutationsAdapter =
            context?.let { ArrayAdapter<String>(it, R.layout.text_item, R.id.tv_item, salutationsList) }
        binding.etSalutation.setAdapter(salutationsAdapter)
        binding.etSalutation.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.etSalutation.showDropDown()
            false
        })

        val genderList = requireContext().resources!!.getStringArray(R.array.gender)
        val genderLAdapter =
            context?.let { ArrayAdapter<String>(it, R.layout.text_item, R.id.tv_item, genderList) }
        binding.etGender.setAdapter(genderLAdapter)
        binding.etGender.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.etGender.showDropDown()
            false
        })

        binding.etGender.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                genderStr = s.toString()
            }
        })
        //try farmtype here  farmregion
        val regionList = requireContext().resources!!.getStringArray(R.array.farmregion)
        val regionAdapter =
            context?.let { ArrayAdapter<String>(it, R.layout.spinner_layout, R.id.tvProductName, regionList) }
        binding.spnRegion.setAdapter(regionAdapter)
        binding.spnRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            regionStr = parent!!.selectedItem.toString()
            }
        }
       *//* binding.spnRegion.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.spnRegion.showDropDown()
            false
        })*//*
        val farmType = requireContext().resources!!.getStringArray(R.array.farmtype)
        val farmTypeAdapter =
            context?.let { ArrayAdapter<String>(it, R.layout.spinner_layout, R.id.tvProductName, farmType) }
        binding.spnFarmtype.setAdapter(farmTypeAdapter)

        binding.btnAddFarmer.setOnClickListener {
            //assume all data is valid
            processRegisterDetail()
           *//* globalMethods.loader(requireActivity())
             lifecycleScope.launch {
                delay(3000)
                mCallback.onActionChosen("add_farmer")
            }*//*
        }

        binding.btnCancel.setOnClickListener {
            mCallback.onActionChosen("")
        }

        binding.btnCancelAdd.setOnClickListener {
            mCallback.onActionChosen("")
        }
    }
*/
    private fun processRegisterDetail() {
        try{
            var loggedUserJson = UtilPreference().getLoggedAgent(requireContext())
            var agentUserObj: AgentCallback.LoginCallback = NetworkUtility.jsonResponse(loggedUserJson)
            var transactionJson = JSONObject()
         lifecycleScope.launch {
            transactionJson.put("farmerType", "large scale")
            transactionJson.put("farmerFirstName", binding.etName.text.toString())
            transactionJson.put("farmerLastName", binding.etOtherNames.text.toString())
            transactionJson.put("farmerMiddleName", "mik")
            transactionJson.put("idNumber", binding.etFarmerID.text.toString())
            transactionJson.put("dob", "2021-07-22T15:54:19.612Z")
            transactionJson.put("gender", genderStr)
            transactionJson.put("language", "English")
            transactionJson.put(
                "phoneNumber",binding.etPhone.text.toString()
            )//"${ binding.cpp.selectedCountryCode}"+
            transactionJson.put("bankName", "kcb")
            transactionJson.put("bankAccNumber", "125236252")
            transactionJson.put("occupation", "Farmer")
            transactionJson.put("email", binding.etFarmer.text.toString())
            transactionJson.put("station", binding.etLocation.text.toString())
            transactionJson.put("regionCode", regionStr)
            transactionJson.put("country", "cost")
            transactionJson.put("latitude", "23.233")
            transactionJson.put("longitude", "23.23")
            transactionJson.put("regBy", agentUserObj.name)
            transactionJson.put("reRegBy", agentUserObj.name)
            transactionJson.put("registrationSmsSent", true)
            transactionJson.put("cooperativeId", agentUserObj.cooperativeId)
            transactionJson.put("agentId", agentUserObj.agentNo)
            transactionJson.put("farmPhoto", "")
            transactionJson.put("farmsize", binding.etFsize.text.toString())
            NetworkUtility().sendRequest(pDialog)
            sendFarmregRequest(transactionJson)
        }
        }catch (ex:Exception){}
    }

    private suspend fun sendFarmregRequest(dataJson: JSONObject) {
        farmViewModel.sendFarmRegRequest(dataJson).observe(requireActivity(), Observer {
            pDialog.dismiss()
            when(it){
                is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                is ViewModelWrapper.response -> processRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
            }
        })
    }

    private fun processRequest(responsse: String) {
        LoggerHelper.loggerError("farresp","message $responsse")
        mCallback.onActionChosen("add_farmer")
    }

    private fun handlePay() {
        binding.clDetails.visibility = View.GONE
        binding.clAddFarmer.visibility = View.GONE
        binding.clFloat.visibility = View.GONE
        binding.clPaymentOptions.visibility = View.GONE
        binding.clPay.visibility = View.VISIBLE
        binding.clFloatBalance.visibility = View.GONE

        binding.btnPay.setOnClickListener {
            globalMethods.loader(requireActivity())
             lifecycleScope.launch {
                delay(3000)
            }
            mCallback.onActionChosen("Pay")
        }

        binding.btnCancel.setOnClickListener { mCallback.onActionChosen("Cancel") }

        binding.tvBuyFrom.text =
            getString(R.string.purchase_from, "${theFarmer!!.firstName} ${theFarmer.lastName}")

//        binding.tvWeightBought.text = "Product: ${theFarmer.quantity} Cocoa"

        binding.tvCost.text = "CFA 1400"
    }

    private fun handleDetails() {
        binding.clPay.visibility = View.GONE
        binding.clAddFarmer.visibility = View.GONE
        binding.clDetails.visibility = View.VISIBLE
        binding.clPaymentOptions.visibility = View.GONE
        binding.clFloatBalance.visibility = View.GONE
//        val user = UserRepository.theUser.value!![0]

        if (theFarmer != null) {
            binding.tvBuyer.text =
                getString(R.string.buyer, "${theFarmer.firstName} ${theFarmer.lastName}")
            binding.tvLocationValue.text = theFarmer.location
            binding.tvFarmSizeValue.text = "${theFarmer.farmSize} "
            binding.tvLastCollectionValue.text = theFarmer.lastCollectionDate
            binding.tvLastCollectionWeightValue.text = theFarmer.lastWeightCollected
            binding.tvDateJoinedValue.text = theFarmer.dateJoined
            binding.tvEmailAddressdata.text = theFarmer.emailAddress

            binding.rating.rating = theFarmer.produceQuality.toFloat()

            when (theFarmer.produceQuality) {
                1 -> binding.tvQualityValue.text = getString(R.string.very_poor)
                2 -> binding.tvQualityValue.text = getString(R.string.poor)
                3 -> binding.tvQualityValue.text = getString(R.string.good)
                4 -> binding.tvQualityValue.text = getString(R.string.very_good)
                5 -> binding.tvQualityValue.text = getString(R.string.excellent)
            }
            binding.btnCancel.setOnClickListener {
                dismiss()
            }
            binding.ivClose.setOnClickListener { dismiss() }
            binding.btnFdCollect.setOnClickListener { dismiss() }
            //get farmer last payment transaction
            getLatestTransaction()
        }

    }

    private fun getLatestTransaction() {
        var buyerObj =  UtilPreference().getUserObj(requireActivity())
        var lookupJson = JSONObject()
        lookupJson.put("farmerPhoneNumber",theFarmer!!.phoneNumber)
        lookupJson.put("userId", buyerObj.userId)
        lookupJson.put("phoneNumber", buyerObj.phoneNumber)
        UserLatestTransaction.getTransaction(requireActivity(),lookupJson).observe(viewLifecycleOwner,
            Observer {
                when(it){
                    is ViewModelWrapper.response ->{
                        //LoggerHelper.loggerError("respobnse","resp ${it.value}")
                      //  var listdata:List<RecentTransaction> = NetworkUtility.jsonResponse(it.value)
                       // LoggerHelper.loggerError("respobnse","recenttrans ${listdata[0].recipientName}")
                    }
                    else -> {}
                }
            })
    }


}

interface OnActionTaken {

    fun onActionChosen(value: String?)



}
