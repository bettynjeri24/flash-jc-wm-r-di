package com.ekenya.rnd.authcargill.ui.otpverification

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.authcargill.R
import com.ekenya.rnd.authcargill.databinding.FragmentVerificationOtpBinding
import com.ekenya.rnd.authcargill.ui.AuthCargillViewModel

import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.data.network.NetworkExceptions
import com.ekenya.rnd.common.data.repository.ApiExceptions
import com.ekenya.rnd.common.utils.base.BaseCommonAuthCargillDIFragment
import com.ekenya.rnd.common.utils.custom.UtilPreferenceCommon
import com.ekenya.rnd.common.utils.custom.hasGooglePlayServices
import com.ekenya.rnd.common.utils.custom.isValidPIN
import com.ekenya.rnd.common.utils.services.GoogleSMSReceiver
import com.ekenya.rnd.common.utils.services.HuaweiSMSReceiver
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.huawei.hmf.tasks.Task
import com.huawei.hms.support.sms.ReadSmsManager
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


class OtpVerificationFragment : BaseCommonAuthCargillDIFragment<FragmentVerificationOtpBinding>(
    FragmentVerificationOtpBinding::inflate
), GoogleSMSReceiver.GoogleOTPReceiveListener,
    HuaweiSMSReceiver.OTPHuaweiReceiveListener {

    @Inject
    lateinit var authCargillViewModel: AuthCargillViewModel

    @Inject
    lateinit var googleSMSReceiver: GoogleSMSReceiver

    @Inject
    lateinit var huaweiSMSReceiver: HuaweiSMSReceiver

    private var smsOtp: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        startSMSListener()
        binding.btnVerifyOtp.setOnClickListener {
            sendPhoneLookUpRequest()
        }
    }

    private fun sendPhoneLookUpRequest() {
        if (validateRequiredField()) {
            showCustomDialog(getString(com.ekenya.rnd.common.R.string.sending_request_wallet))
            coopAccountIdCheckRequest()
        }
    }

    private fun coopAccountIdCheckRequest() {
        lifecycleScope.launch {
            try {
                val otp = binding.edtOtp.text.toString().trim()
                val jsonObject = JSONObject()
                jsonObject.put(
                    "phonenumber",
                    UtilPreferenceCommon().getLoggedPhoneNumber(requireActivity())
                )
                jsonObject.put("otp", otp.trim())
                val response = authCargillViewModel.verifyOtpForAccountSetUp(jsonObject)
                if (response.statusCode == 0) {
                    dismissCustomDialog()
                    findNavController().navigate(R.id.setAccountPinFragment)
                } else {
                    toast(response.toString())
                    dismissCustomDialog()
                    dismissCustomDialog()
                }

            } catch (e: ApiExceptions) {
                toast(e.toString())
                dismissCustomDialog()
                Log.e("Exception", UtilPreferenceCommon.phonenumber)
                Log.e("Exception", e.toString())
            } catch (e: NetworkExceptions) {
                toast(e.toString())
                dismissCustomDialog()
            }

        }
    }

    private fun validateRequiredField(): Boolean {
        var pin = binding.edtOtp.text.toString()
        if (isValidPIN(pin)) {
            binding.edtOtp.error =
                resources.getString(com.ekenya.rnd.common.R.string.enter_secure_pin)
            return false
        }
        return true
    }

    private fun startSMSListener() {
        if (requireActivity().hasGooglePlayServices()) {// Phone has Google PlayServices start SMS Retriever
            try {
                googleSMSReceiver.googleInitOTPListener(this)
                val intentFilter = IntentFilter()
                intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
                requireActivity().registerReceiver(googleSMSReceiver, intentFilter)
                googleSMSReceiver.startGoogleSMSRetriever(requireActivity())
            } catch (e: Exception) {
                Timber.e("EXCEPTION MESSAGE -%s", e.message)
            }
        } else {//Huawei Implementation
            huaweiSMSReceiver.initHuaweiOTPListener(this)
            val task: Task<Void> =
                ReadSmsManager.startConsent(requireActivity(), "")
            task.addOnCompleteListener { task1 ->
                if (task1.isSuccessful) {
                    Timber.e("STARTHUAWEISMSRETRIEVER: ISSUCCESSFUL")
                    huaweiSMSReceiver.huaweiSmsRegister(requireActivity())
                } else {
                    //task false
                    Timber.e("STARTHUAWEISMSRETRIEVER: FAILED")
                }
            }
            task.addOnFailureListener {
                Timber.e("ADDONFAILURELISTENER: FAILED" + it.message)
            }
        }
    }

    override fun onStop() {
        huaweiSMSReceiver.huaweiSmsUnRegister(requireActivity())
        super.onStop()
    }

    override fun onHuaweiOTPReceived(otp: String?) {
        Timber.e("ONHUAWEIOTPRECEIVED SUCCESS MESSAGE -%s", otp)
        requireActivity().runOnUiThread {
            smsOtp = otp
            binding.edtOtp.setText(smsOtp)
            dismissCustomDialog()
            binding.btnVerifyOtp.visibility = View.VISIBLE
        }
    }

    override fun onHuaweiOTPTimeOut(timeoutMessage: String) {
        Timber.e("ONHUAWEIOTPTIMEOUT TIMEOUTMESSAGE MESSAGE -%s", timeoutMessage)
    }

    override fun onGoogleOTPReceived(message: String) {
        Timber.e("success Message -%s", message)
        requireActivity().runOnUiThread {
            smsOtp = message
            binding.edtOtp.setText(smsOtp)
            dismissCustomDialog()
            binding.btnVerifyOtp.visibility = View.VISIBLE
        }
    }

    override fun onGoogleOTPTimeOut(timeoutMessage: String) {
        Timber.e("timeoutMessage Message -%s", timeoutMessage)
    }

}