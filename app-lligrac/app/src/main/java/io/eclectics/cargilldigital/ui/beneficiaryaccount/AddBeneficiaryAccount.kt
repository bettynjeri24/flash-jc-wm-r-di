package io.eclectics.cargilldigital.ui.beneficiaryaccount

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentAddBeneficiaryAccountBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.data.model.SendMoney
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.ui.spinnermgmt.BeneficiarySpinner
import io.eclectics.cargilldigital.utils.FieldValidation
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.InputValidator.formatPhoneNumber
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.FarmerRoomViewModel
import io.eclectics.cargilldigital.viewmodel.FarmerViewModel
import io.eclectics.cargilldigital.viewmodel.ViewModelWrapper
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AddBeneficiaryAccount : Fragment() {
    private var _binding: FragmentAddBeneficiaryAccountBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BeneficiarySpinner
    @Inject
    lateinit var navoption: NavOptions
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var accountNo:String
    lateinit var accountName:String
    lateinit var accountProvider:String
    lateinit var lookupJson:JSONObject
    lateinit var userData:UserDetailsObj
    lateinit var selectedChannel:SendMoney.ChannelListObj
    val farmViewModel: FarmerViewModel by viewModels()
    val farmerRoomViewModel:FarmerRoomViewModel by viewModels()
    lateinit var beneficiaryAccountNumber:String
    private lateinit var pickContactCallback: ActivityResultCallback<Uri?>
    private lateinit var pickContactLauncher: ActivityResultLauncher<Uri>
    lateinit var bankName:String
    // Single Permission Contract
    private val askContactPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                selectContact()
                Timber.e("TAG Contract permission granted")
            } else {
                Timber.e("TAG Contract permission denied")
                Toast.makeText(
                    requireActivity(),
                    "You have disabled a contacts permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBeneficiaryAccountBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        setToolbarTitle(resources.getString(R.string.add_beneficiary),resources.getString(R.string.add_beneficiary_subbtle))
        return binding.root
        // return inflater.inflate(R.layout.fragment_transfer_tobank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        var beneficiaryList = FarmerAccount.providerList()
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        getChannelList()
        bankName = ""
        binding.spinnerProvider.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 selectedChannel = spnAdapter.getItem(position)
                accountProvider = selectedChannel.channelName
                //farmerName = "${selectedFarmer.firstName}${selectedFarmer.lastName}"
                setHint()
            }
        }
        binding.btnBuyAirtime.setOnClickListener{
          //  findNavController().navigate(R.id.nav_verifyOtp)
            AddAccount()
        }



    }

    private fun setHint() {
        LoggerHelper.loggerError("chanel","${selectedChannel.type}")
        when(selectedChannel.type) {
            "Telco" -> {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_phonebook)
                binding.tvMobileNotitle.text = resources.getString(R.string.mobile_number)
                binding.tlPhonenumber.hint = resources.getString(R.string.enter_phoneNo)
                //binding.tlPhonenumber.setEndIconTintMode()
                binding.tlPhonenumber.endIconMode = TextInputLayout.END_ICON_CUSTOM
                binding.tlPhonenumber.endIconDrawable=resources.getDrawable(R.drawable.ic_phonebook)
               /* binding.etPhoneNumber.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )*/
                binding.etPhoneNumber.onRightDrawableClicked {
                    LoggerHelper.loggerError("tets","testclick")
                    selectContact()
                }
            }
            "Bank"->{
                binding.tvMobileNotitle.text = resources.getString(R.string.account_number)
                binding.tlPhonenumber.hint = resources.getString(R.string.enter_account_number)
                bankName = selectedChannel.channelName
                binding.tlPhonenumber.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
               // binding.tlPhonenumber.removeOnEndIconChangedListener(binding.etPhoneNumber.hasOnClickListeners())
            }
        }
    }

    private fun getChannelList() {
        var endpoint  =  ApiEndpointObj.getChannelList
        lookupJson = JSONObject()
        //lookupJson.put("phonenumber",requireArguments().getString("phone"))
        lookupJson.put("userId",userData.providedUserId)
        lookupJson.put("channelType","telco")
        lookupJson.put("endPoint",endpoint)
         lifecycleScope.launch {
            pdialog.show()
            farmViewModel.getFarmerChannellist(lookupJson, endpoint,requireActivity()).observe(requireActivity(), Observer {
                pdialog.dismiss()
                when(it){
                    is ViewModelWrapper.error -> GlobalMethods().transactionWarning(requireActivity(),"${it.error}")//LoggerHelper.loggerError("error","error")
                    is ViewModelWrapper.response -> processChannelRequest(it.value)//LoggerHelper.loggerSuccess("success","success ${it.value}")
                    //processRequest(it.value)//
                }
            })
        }
    }

    private fun processChannelRequest(response: String) {
//ChannelListObj
        LoggerHelper.loggerError("channellistObj","response test loop")

        LoggerHelper.loggerSuccess("response","product response ${response}")
        //save to room database
        var channelList: List<SendMoney.ChannelListObj> = NetworkUtility.jsonResponse(response)
        spnAdapter = BeneficiarySpinner(requireActivity(),channelList)
        binding.spinnerProvider.adapter = spnAdapter
         lifecycleScope.launch {
            //insertChannelList
            farmerRoomViewModel.insertChannelList(requireActivity(),response)
        }
    }

    private fun AddAccount() {
        if(fieldValidated()) {
            beneficiaryAccountNumber = binding.etPhoneNumber.text.toString()
            processRequest()
        }
    }

    private fun processRequest() {
         var lookupJson = JSONObject()
        var modifyAcc = modifyChannelNumber()
       lookupJson.put("beneficiaryName", binding.etAccName.text.toString())
        lookupJson.put("accountholderphonenumber",userData.phoneNumber)
        lookupJson.put("channelId",selectedChannel.channelId)
        lookupJson.put( "channelNumber",modifyAcc)
        lookupJson.put("bankName",bankName)
        lookupJson.put("cardNumber","")
        lookupJson.put("expiryDate","")
        lookupJson.put("cvc","")
        lookupJson.put("endPoint",ApiEndpointObj.addBeneficiaryAcc)

        var confirmationObj = ConfirmationObj(resources.getString(R.string.add_beneficiary),binding.etPhoneNumber.text.toString(),selectedChannel.channelName,binding.etAccName.text.toString(),R.id.nav_farmerDashboard,"addAccount")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var selectedChannelJson = NetworkUtility.getJsonParser().toJson(selectedChannel)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("accountNumber",binding.etPhoneNumber.text.toString())
        bundle.putString("accountName",binding.etAccName.text.toString())
        bundle.putString("channel",selectedChannelJson.toString())
        bundle.putString("confirmTitle",resources.getString(R.string.confirm_add_beneficiary))
        bundle.putString("debitTitle",resources.getString(R.string.debit_title))
        bundle.putString("requestjson",lookupJson.toString())
        bundle.putString("endPoint", ApiEndpointObj.addBeneficiaryAcc)

        findNavController().navigate(R.id.nav_confirmationAddChannel,bundle,navoption)

    }

    private fun modifyChannelNumber(): String {
        when(selectedChannel.type){
            "Telco"->{beneficiaryAccountNumber = "225$beneficiaryAccountNumber"}
        }
        return beneficiaryAccountNumber

    }

    private fun fieldValidated(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        val validAccName = FieldValidation().validName(binding.etAccName.text.toString(), "Account name")
        val validPhoneNumber = FieldValidation().validPhoneNUmber(binding.etPhoneNumber.text.toString(),"Beneficiary account")
        var accountNumber = formatPhoneNumber(binding.etPhoneNumber.text.toString())
        binding.etPhoneNumber.setText(accountNumber)
        when(selectedChannel.type) {
            "Telco" -> {
                if(!InputValidator.isValidPhone(accountNumber)) {
                    binding.etPhoneNumber.requestFocus()
                    binding.tlPhonenumber.error =
                        resources.getString(R.string.validation_phone_number)
                    return false
                }
                if(!validTelco(accountNumber)){return false}
            }
            "Bank" -> {
                return true
            }
            "Wave" -> {
                return true
            }
            "Card" -> {
                return true
            }
        }
        /*if(!InputValidator.isValidPhone(accountNumber)){
            return validTelco(accountNumber)
        }*/

        if (!validAccName.contentEquals(validMsg)) {
            binding.tlAccName.requestFocus()
            binding.tlAccName.error = validAccName
            return false
        }
        return true
    }

    private fun validTelco(accountNumber: String): Boolean {
        when(selectedChannel.channelName){
            "Orange Money"->{
                if(!accountNumber.startsWith("07")){
                    NetworkUtility().transactionWarning(resources.getString(R.string.validation_phone_number),requireActivity())
                    return  false
                }
              // return Pattern.matches("^[07]{10}|[0-9]{9}$", accountNumber)
            }
            "MTN Money"->{
                if(!accountNumber.startsWith("05")){
                    NetworkUtility().transactionWarning(resources.getString(R.string.validation_phone_number),requireActivity())
                    return  false
                }
            }
        }
       // if(accountNumber.startsWith("0706"))



        return true
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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        selectContactContract()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    fun selectContactContract() {
        pickContactCallback = ActivityResultCallback<Uri?> { contactUri: Uri? ->
            // handle the actual result later to query the database for the contact
            contactUri?.let {
                val phoneNo: String?
                // Get the URI and query the content provider for the phone number
                val cursor = activity?.contentResolver?.query(it, null, null, null, null)
                if (cursor!!.moveToFirst()) {
                    val phoneIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    phoneNo = cursor.getString(phoneIndex).filter { char -> char.isDigit() }
                    /*if (this::etPhoneNumber.isInitialized) {
                        etPhoneNumber.setText(formatPhoneNumber(phoneNo))
                    }*/
                    binding.etPhoneNumber.setText(formatPhoneNumber(phoneNo))
                    //UserProfileModel.selectContact = false
                }
                cursor.close()
            }
        }
        pickContactLauncher = registerForActivityResult(PickContactContract(), pickContactCallback)
    }
    /**
     * Select Contact from phone book
     */
    fun selectContact() {
       // UserProfileModel.selectContact = true
        pickContactLauncher.launch(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
    }

    fun requestContactPermission() {
        // Check if the READ_CONTACTS permission has been granted
        if ((ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_CONTACTS
            )) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available, show contact List
            selectContact()
        } else {
            // Permission is missing and must be requested.
            askContactPermission.launch(Manifest.permission.READ_CONTACTS)
        }
    }

}