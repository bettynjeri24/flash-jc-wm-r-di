package com.ekenya.rnd.cargillfarmer.ui.farmerprofile.manageaccounts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.cargillfarmer.R
import com.ekenya.rnd.cargillfarmer.databinding.FragmentAddBeneficiaryAccountBinding
import com.ekenya.rnd.cargillfarmer.ui.adapters.spinners.CustomArrayAdapter
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import com.ekenya.rnd.common.CURRENT_USER_PHONENUMBER
import com.ekenya.rnd.common.SELECT_SERVICE_PROVIDER
import com.ekenya.rnd.common.auth.AuthResult
import com.ekenya.rnd.common.data.db.entity.CargillUserChannelData
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.utils.base.BaseCommonFarmerCargillDIFragment
import com.ekenya.rnd.common.utils.custom.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

var hashMapAddAccount: HashMap<String, String> = HashMap()

class FarmerAddBeneficiaryFragment :
    BaseCommonFarmerCargillDIFragment<FragmentAddBeneficiaryAccountBinding>(
        FragmentAddBeneficiaryAccountBinding::inflate
    ) {
    private lateinit var userChannelDataList: List<CargillUserChannelData>
    private lateinit var userChannelData: CargillUserChannelData
    private lateinit var bankName: String
    private lateinit var pickContactCallback: ActivityResultCallback<Uri?>
    private lateinit var pickContactLauncher: ActivityResultLauncher<Uri>

    @Inject
    lateinit var viewModel: FarmerViewModel

    // init select contact adapter
    private lateinit var customArrayAdapter: CustomArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.farmerAddBeneficiaryFragment, true)
                findNavController().navigate(R.id.farmerHomeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserVmRoom().observe(viewLifecycleOwner) {
            if (it.channels!!.isNotEmpty()) {
                userChannelDataList = it.channels!!
                customArrayAdapter = CustomArrayAdapter(requireContext(), it.channels!!)
                binding.spinnerProvider.adapter = customArrayAdapter
                binding.spinnerProvider.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            val prodObj: CargillUserChannelData =
                                customArrayAdapter.getItem(position) as CargillUserChannelData
                            userChannelData =
                                customArrayAdapter.getItem(position) as CargillUserChannelData
                            SELECT_SERVICE_PROVIDER = prodObj.channelName.toString()
                            // hashMap["accountholderphonenumber"] = prodObj.channelName.toString()
                            setHint()
                            Timber.e("You Select Position: ${prodObj.channelName} ")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            Timber.e("Nothing Selected")
                        }
                    }
            } else {
                Timber.e("No Chanesll Detail")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("requestKey") { _, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val result: AuthResult = bundle.get("authResult") as AuthResult

            when (result) {
                AuthResult.AUTH_SUCCESS -> {
                    // findNavController().navigate(R.id.OTPFragment)
                    sendRequestToAddAccount()
                }
                AuthResult.AUTH_ERROR -> {
                    lifecycleScope.launch {
                        dismissCustomDialog()
                    }
                }
            }
        }
        setUpUi()
    }

    private fun setUpUi() {
        setToolbarTitle(
            resources.getString(com.ekenya.rnd.common.R.string.add_beneficiary),
            resources.getString(com.ekenya.rnd.common.R.string.add_beneficiary_subbtle),
            mainLayoutToolbar = binding.mainLayoutToolbar,
            activity = requireActivity()
        )

        binding.btnBuyAirtime.setOnClickListener {
            if (fieldValidated()) {
                transferToTelcoRequest()
            }
        }
    }

    private fun transferToTelcoRequest() {
        hashMapAddAccount[resources.getString(com.ekenya.rnd.common.R.string.phone_number)] =
            binding.etPhoneNumber.text.toString()
        hashMapAddAccount[resources.getString(com.ekenya.rnd.common.R.string.channel_name)] =
            userChannelData.channelName.toString()
        hashMapAddAccount[resources.getString(com.ekenya.rnd.common.R.string.account_name)] =
            binding.etAccName.text.toString()

        lifecycleScope.launch {
            showConfirmationDialog(
                "${getString(com.ekenya.rnd.common.R.string.confirm)} ${getString(com.ekenya.rnd.common.R.string.add_beneficiary)} ",
                "${getString(com.ekenya.rnd.common.R.string.add_beneficiary)}, " +
                    "${binding.etPhoneNumber.text} :",
                hashMapAddAccount,
                dialogCallBackAddBeneficiary
            )
        }
    }

    private val dialogCallBackAddBeneficiary = object : ConfirmDialogCallBacks {
        override fun confirm() {
            lifecycleScope.launch {
                findNavController().navigate(R.id.commonAuthFragment)
            }
        }

        override fun cancel() {
            Timber.e("cancel")
        }
    }

    private fun sendRequestToAddAccount() {
        lifecycleScope.launch {
            try {
                val lookupJson = JSONObject()
                lookupJson.put("beneficiaryName", binding.etAccName.text.toString())
                lookupJson.put("accountholderphonenumber", CURRENT_USER_PHONENUMBER)
                lookupJson.put("channelId", userChannelData.id)
                lookupJson.put("channelNumber", binding.etPhoneNumber.text.toString())
                showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))

                val response = viewModel.requestAddBeneficiaryAccountData(
                    lookupJson
                )
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    Timber.e("RESPONSE: ${response.data}")
                    hashMapAddAccount["reasons"] =
                        response.statusDescription.toString()

                    val createSuccessBundle = createSuccessBundle(
                        title = getString(com.ekenya.rnd.common.R.string.transfer_to_telco_wallet),
                        subTitle = getString(com.ekenya.rnd.common.R.string.msg_request_was_successful),
                        cardTitle = " ${response.statusDescription}",
                        cardContent = " ${response.statusDescription}",
                        hashMap = hashMapAddAccount
                    )
                    findNavController().popBackStack(R.id.farmerAddBeneficiaryFragment, true)
                    // findNavController().navigate(R.id.OTPFragment, createSuccessBundle)
                } else {
                    dismissCustomDialog()
                    requireActivity().showCargillInternalCustomDialog(
                        title = response.statusDescription.toString(),
                        description = response.statusDescription.toString(),
                        btnConfirmText = getString(com.ekenya.rnd.common.R.string.retry_request),
                        positiveButtonFunction = { Timber.e("") },
                        negativeButtonFunction = { Timber.e("") }
                    )
                    Timber.e("******************registered==1** $response")
                }
            } catch (e: ApiExceptions) {
                Timber.e(e.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                Timber.e(e.toString())
                dismissCustomDialog()
            }
        }
    }

    private fun fieldValidated(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        val validAccName =
            FieldValidation().validName(binding.etAccName.text.toString(), "Account name")
        var accountNumber = formatPhoneNumber(binding.etPhoneNumber.text.toString())
        binding.etPhoneNumber.setText(accountNumber)
        when (userChannelData.type) {
            "Telco" -> {
                if (!accountNumber.isPhoneNumber()) {
                    binding.tlPhonenumber.error =
                        resources.getString(com.ekenya.rnd.common.R.string.validation_phone_number)
                    Timber.e("Hapa problem")
                    return false
                }
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
        if (!validAccName.contentEquals(validMsg)) {
            binding.tlAccName.error = validAccName
            return false
        }
        return true
    }

    private fun validTelco(accountNumber: String): Boolean {
        if (userChannelData.channelName.toString().startsWith("orange", true)) {
            if (!accountNumber.startsWith("07")) {
                Timber.e("here 07")
                Timber.e(
                    resources.getString(com.ekenya.rnd.common.R.string.validation_phone_number)
                )
                return false
            }
        } else if (userChannelData.channelName.toString().startsWith("mtn", true)) {
            if (!accountNumber.startsWith("05")) {
                Timber.e("here 05")
                Timber.e(
                    resources.getString(com.ekenya.rnd.common.R.string.validation_phone_number)
                )
                return false
            }
        }
        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        selectContactContract()
    }

    private fun setHint() {
        Timber.e("chanel $userChannelData")
        when (userChannelData.type) {
            "Telco" -> {
                binding.tvMobileNotitle.text =
                    resources.getString(com.ekenya.rnd.common.R.string.mobile_number)
                binding.tlPhonenumber.hint =
                    resources.getString(com.ekenya.rnd.common.R.string.enter_phoneNo)
                binding.tlPhonenumber.endIconMode = TextInputLayout.END_ICON_CUSTOM
                binding.tlPhonenumber.endIconDrawable =
                    ContextCompat.getDrawable(
                        requireContext(),
                        com.ekenya.rnd.common.R.drawable.ic_phonebook
                    )
                binding.etPhoneNumber.onRightDrawableClicked {
                    Timber.e("tets", "testclick")
                    selectContact()
                }
            }
            "Bank" -> {
                binding.tvMobileNotitle.text =
                    resources.getString(com.ekenya.rnd.common.R.string.account_number)
                binding.tlPhonenumber.hint =
                    resources.getString(com.ekenya.rnd.common.R.string.enter_account_number)
                bankName = userChannelData.channelName.toString()
                binding.tlPhonenumber.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
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

    private fun selectContactContract() {
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
                    // UserProfileModel.selectContact = false
                }
                cursor.close()
            }
        }
        pickContactLauncher = registerForActivityResult(
            activityResultContractPickContactContract,
            pickContactCallback
        )
    }

    /**
     * Select Contact from phone book
     */
    private fun selectContact() {
        // UserProfileModel.selectContact = true
        pickContactLauncher.launch(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
    }

    private fun requestContactPermission() {
        // Check if the READ_CONTACTS permission has been granted
        if ((
            ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_CONTACTS
                )
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already available, show contact List
        } else {
            // Permission is missing and must be requested.
            askContactPermission.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private val askContactPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                selectContact()
                Timber.e("TAG Contract permission granted")
            } else {
                Timber.e("TAG Contract permission denied")
//                requireActivity().showCargillInternalCustomDialog(
//                    title = "Enable read contact",
//                    description = "Enable read contact",
//                    positiveButtonFunction = { requestContactPermission() }
//                )
                Timber.e("You have disabled a contacts permission")
            }
        }
}
