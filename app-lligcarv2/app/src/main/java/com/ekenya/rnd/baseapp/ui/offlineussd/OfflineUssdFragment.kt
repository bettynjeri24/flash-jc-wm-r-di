package com.ekenya.rnd.baseapp.ui.offlineussd

import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.READ_CONTACTS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.ekenya.rnd.baseapp.databinding.FragmentOfflineUssdBinding
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.dialogs.dialog_confirm.ConfirmDialogCallBacks
import com.ekenya.rnd.common.utils.base.BaseCommonCargillFragment
import com.ekenya.rnd.common.utils.custom.snackBarCustom
import com.ekenya.rnd.common.utils.custom.stringToAsciiNumber
import com.ekenya.rnd.common.utils.custom.timber
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.properties.Delegates

class OfflineUssdFragment : BaseCommonCargillFragment<FragmentOfflineUssdBinding>(
    FragmentOfflineUssdBinding::inflate
) {
    private var hashMap: HashMap<String, String> = HashMap()

    var callCode: String? = null
    var shortCode: String? = null
    var transIdPassedCode by Delegates.notNull<Int>()
    var telephonyManager: TelephonyManager? = null
    var ussdResponseCallback: TelephonyManager.UssdResponseCallback? = null
    lateinit var SIM_MAP: MutableMap<String, Int>
    var simCardNames: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SIM_MAP = mutableMapOf()
        simCardNames = ArrayList()

        telephonyManager =
            requireActivity().getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // oscarImplementation()
        checkPermissions()

        binding.btnSend.setOnClickListener {
            if (binding.edtUserInputPhoneNumber.text.toString().isNotEmpty()
            ) {
                val dataToSend =
                    "${
                    binding.edtUserInputPhoneNumber.text.toString().toUpperCase()
                    }${
                    binding.edtUserInputPin.text
                    }"
                Timber.e("DATA TO SEND \n\n\n $dataToSend")
                oscarImplementation(
                    userInput = stringToAsciiNumber(
                        dataToSend
                    ).replace(",", "")
                )
            }
        }
    }

    // Implementation
    private fun oscarImplementation(userInput: String = "345671234889654689") {
        shortCode = "605*214"
        // shortCode = "747"
        callCode = "$shortCode*$userInput" // "605*214*1234"
        transIdPassedCode = userInput!!.take(2).toInt()
        showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))

        dialUssdToGetPhoneNumber("*$callCode#", 1)
    }

    private fun dialUssdToGetPhoneNumber(ussdCode: String, sim: Int) {
        Timber.e("ussdTag ========== tag $ussdCode")
        if (ussdCode.equals("", ignoreCase = true)) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager =
                    requireActivity().getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
                val telephonyManager = manager.createForSubscriptionId(2)
                val managerMain = if (sim == 0) manager else telephonyManager
                managerMain.sendUssdRequest(
                    ussdCode,
                    object : TelephonyManager.UssdResponseCallback() {
                        override fun onReceiveUssdResponse(
                            telephonyManager: TelephonyManager,
                            request: String,
                            response: CharSequence
                        ) {
                            super.onReceiveUssdResponse(telephonyManager, request, response)
                            Timber.e("REQUEST ===$request\n, RESPONSE====$response\n")
                            dismissCustomDialog()
                            Timber.e(
                                "onReceiveUssdResponse:  \nUSSD RESPONSE\n = " + response.toString()
                                    .trim { it <= ' ' }
                            )

                            binding.textViewMethod1.text =
                                "TelephonyManager.UssdResponseCallback \n\n SHORT CODE IS $ussdCode  \n\n" +
                                " $response"
                        }

                        override fun onReceiveUssdResponseFailed(
                            telephonyManager: TelephonyManager,
                            request: String,
                            failureCode: Int
                        ) {
                            super.onReceiveUssdResponseFailed(
                                telephonyManager,
                                request,
                                failureCode
                            )
                            dismissCustomDialog()
                            // navigateToDestinationFragment("Request failed kindly try again later",false)
                            binding.textViewMethod1.text =
                                "TelephonyManager.onReceiveUssdResponseFailed Method  \n FAILURECODE CODE IS $failureCode == \n  REQUEST IS $request"
                            Timber.e("onReceiveUssdResponseFailed: $failureCode$request")
                        }
                    },
                    Handler(Looper.getMainLooper())
                )
            } else {
                dismissCustomDialog()
            }
        } catch (e: Exception) {
            dismissCustomDialog()
            snackBarCustom(e.message)
        }
    }

    private fun checkPermissions() {
        if (requireActivity().let {
            ContextCompat.checkSelfPermission(
                    it,
                    CALL_PHONE
                )
        } != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("Request Permissions")
            requestMultiplePermissions.launch(
                arrayOf(CALL_PHONE, READ_CONTACTS)
            )
        } else {
            Timber.e("Permission Already Granted")
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Timber.e("${it.key} = ${it.value}")
            }
            if (permissions[CALL_PHONE] == true && permissions[READ_CONTACTS] == true) {
                simsDetails()
            } else {
                lifecycleScope.launch {
                    showConfirmationDialog(
                        "Permission not granted",
                        "Please to grant this permission to access best offline capabilities",
                        hashMap,
                        dialogCallback
                    )
                }
            }
            Timber.e("Permission not granted")
        }
    private val dialogCallback = object : ConfirmDialogCallBacks {
        override fun confirm() {
            checkPermissions()
        }

        override fun cancel() {
        }
    }

    private fun simsDetails() {
        val subscriptionInfos = SubscriptionManager.from(
            requireActivity()
        ).activeSubscriptionInfoList
        // this requires READ_PHONE_STATE permission

        // loop through all info objects and store info we store 2 details here carrier name and subscription Id fro each active SIM card
        // so we need map and an array to set SIM card names to Spinner
        for (subscriptionInfo in subscriptionInfos) {
            SIM_MAP.put(subscriptionInfo.carrierName.toString(), subscriptionInfo.subscriptionId)

            simCardNames!!.add(subscriptionInfo.carrierName.toString())
            toast("simCardNames === \n$simCardNames")
            timber("simCardNames === \n$simCardNames")
        }
    }

    companion object {
        fun newInstance() = OfflineUssdFragment()
    }
}
