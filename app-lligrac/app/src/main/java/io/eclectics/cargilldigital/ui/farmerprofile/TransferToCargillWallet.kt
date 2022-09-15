package io.eclectics.cargilldigital.ui.farmerprofile

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import io.eclectics.cargilldigital.MainActivity
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.databinding.FragmentTransferToCargillWalletBinding
import io.eclectics.cargilldigital.data.model.ConfirmationObj
import io.eclectics.cargilldigital.data.model.FarmerAccount
import io.eclectics.cargilldigital.data.model.SendMoney
import io.eclectics.cargilldigital.data.model.UserDetailsObj
import io.eclectics.cargilldigital.network.ApiEndpointObj
import io.eclectics.cargilldigital.ui.beneficiaryaccount.PickContactContract
import io.eclectics.cargilldigital.ui.spinnermgmt.BeneficiarySpinner
import io.eclectics.cargilldigital.utils.CustomTextWatcher
import io.eclectics.cargilldigital.utils.InputValidator
import io.eclectics.cargilldigital.utils.InputValidator.formatPhoneNumber
import io.eclectics.cargilldigital.utils.InputValidator.isValidAmount
import io.eclectics.cargilldigital.utils.InputValidator.isValidComments
import io.eclectics.cargilldigital.utils.ToolBarMgmt
import io.eclectics.cargilldigital.utils.UtilPreference
import io.eclectics.cargilldigital.viewmodel.FarmerRoomViewModel
import io.eclectics.cargilldigital.viewmodel.FarmerViewModel
import io.eclectics.cargilldigital.utils.GlobalMethods
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TransferToCargillWallet : Fragment() {
    private var _binding: FragmentTransferToCargillWalletBinding? = null
    private val binding get() = _binding!!
    lateinit var spnAdapter: BeneficiarySpinner
    @Inject
    lateinit var navoption: NavOptions
    @Inject
    lateinit var pdialog: SweetAlertDialog
    lateinit var accountNo:String
    lateinit var accountName:String
    lateinit var accountProvider:String
    lateinit var lookupJson: JSONObject
    lateinit var userData: UserDetailsObj
    lateinit var selectedChannel: SendMoney.ChannelListObj
    val farmViewModel: FarmerViewModel by viewModels()
    val farmerRoomViewModel: FarmerRoomViewModel by viewModels()
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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_transfer_to_cargill_wallet, container, false)
        _binding = FragmentTransferToCargillWalletBinding.inflate(inflater, container, false)
        (activity as MainActivity?)!!.hideToolbar()
        // setToolbarTitle("Transfer to telco","Transfer to other mobile provider")
        ToolBarMgmt.setToolbarTitle(resources.getString(R.string.transfer_towallet),resources.getString(R.string.transfer_to_cargill_wallet_subttle),binding.mainLayoutToolbar,requireActivity())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userJson  = UtilPreference().getUserData(activity)
        userData = NetworkUtility.jsonResponse(userJson)
        var beneficiaryList = FarmerAccount.providerList()
        pdialog = SweetAlertDialog(requireActivity(), SweetAlertDialog.PROGRESS_TYPE)
        getWalletDetails()
        setListeners()
        bankName = ""

        binding.btnBuyAirtime.setOnClickListener{
            //  findNavController().navigate(R.id.nav_verifyOtp)
            if(ifValidField()) {
                sendTransferRequest()
            }
        }



    }
    private fun setListeners() {
        binding.tlPhonenumber.endIconMode = TextInputLayout.END_ICON_CUSTOM
        binding.tlPhonenumber.endIconDrawable=resources.getDrawable(R.drawable.ic_phonebook)
        binding.etPhoneNumber.onRightDrawableClicked {
            LoggerHelper.loggerError("tets","testclick")
            selectContact()
        }
        binding.tlComments.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlComments))
        binding.tlAmount.editText!!.addTextChangedListener(CustomTextWatcher(binding.tlAmount))
    }

    private fun sendTransferRequest() {
        var transAmount = binding.etAmount.text.toString()
        var phoneNumber = binding.etPhoneNumber.text.toString()
        var recipientPhoneNumber = "${GlobalMethods.countryCode}${phoneNumber}"
        var endpoint  =  ApiEndpointObj.transferToCargillWalet
        var lookupJson = JSONObject()
        lookupJson.put("sendorPhoneNumber",userData.phoneNumber)
        lookupJson.put("recipientPhoneNumber",recipientPhoneNumber)
        lookupJson.put("amount",transAmount)
        lookupJson.put("userIndex",userData.userIndex)
        lookupJson.put("farmerId",userData.userIndex)

        lookupJson.put("reason",binding.etComments.text.toString())
        lookupJson.put("cooperativeid",userData.cooperativeId)
        lookupJson.put("userid",userData.userId)
        lookupJson.put("endPoint",endpoint)
        var confirmationObj = ConfirmationObj(resources.getString(R.string.transfer_to_cargill_wallet_subttle),"${transAmount} CFA","0 CFA","${resources.getString(R.string.cargill_wallet)}-${recipientPhoneNumber}",R.id.nav_farmerDashboard,"ftwallet")
        var json = NetworkUtility.getJsonParser().toJson(confirmationObj)
        var bundle = Bundle()
        bundle.putString("confirm",json)
        bundle.putString("endPoint",endpoint)
        bundle.putString("requestJson",lookupJson.toString())
        bundle.putString("debitTitle",resources.getString(R.string.beneficiary_account))
        findNavController().navigate(R.id.nav_transactionConfirmation,bundle,navoption)
    }

    private fun ifValidField(): Boolean {
        var currentBalance = UtilPreference().getWalletBalance(requireActivity())
        var accountNumber = binding.etPhoneNumber.text.toString()
        var amount  = binding.etAmount.text.toString()
        var comments = binding.etComments.toString()
        if(!InputValidator.isValidPhone(accountNumber)) {
            binding.etPhoneNumber.requestFocus()
            binding.tlPhonenumber.error =
                resources.getString(R.string.validation_phone_number)
            return false
        }
        if(!isValidAmount(amount)){
            binding.etAmount.requestFocus()
            binding.tlAmount.error = resources.getString(R.string.validation_amount)
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

    private fun getWalletDetails() {
        var maskedPhone = GlobalMethods().simpleMasking(userData.phoneNumber!!,4,3)
        var username = "${userData.firstName} ${userData.lastName}"
       // var channelObj = SendMoney.ChannelListObj(1,"1","$username-$maskedPhone",resources.getString(R.string.cargill_wallet),resources.getString(R.string.cargill_wallet))
       // var channelList: List<SendMoney.ChannelListObj> = NetworkUtility.jsonResponse(response)
      /* var mlist = listOf(channelObj)
        spnAdapter = BeneficiarySpinner(requireActivity(),mlist)
        binding.spinnerProvider.adapter = spnAdapter*/
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

}