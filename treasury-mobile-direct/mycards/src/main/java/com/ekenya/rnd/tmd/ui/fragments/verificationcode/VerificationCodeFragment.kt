package com.ekenya.rnd.tmd.ui.fragments.verificationcode

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.sms.SmsRequest
import com.ekenya.rnd.common.sms.SmsService
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentVerificationCodeBinding
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardViewModel
import com.ekenya.rnd.tmd.utils.SMSBroadcastReceiver
import com.ekenya.rnd.tmd.utils.toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class VerificationCodeFragment(
    private val viewmodel: OnboardViewModel,
    val smsService: SmsService,
    val next: () -> Unit
) : Fragment() {

    private lateinit var binding: FragmentVerificationCodeBinding
    private lateinit var smsBroadcastReceiver: SMSBroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentVerificationCodeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    override fun onStart() {
        super.onStart()
        startSmsUserConsent()
        registerToSmsBroadcastReceiver()
    }

    override fun onResume() {
        super.onResume()
//        lifecycleScope.launch {
//            try {
//                val response = smsService.sendSms(
//                    SmsRequest(
//                        clientid = "5166",
//                        from = "Eclectics",
//                        message = "Your Otp test is ${(100000..999999).random()} and your one time password is ${(1000..9999).random()}",
//                        password = "b7c8f6faf1fcfc08ec7481eee2ebe297e7d29a758fdce4613eb5042e6d5c803b38adbf69860687b22b475dee9c9e4a2c32863398ba0923613aab2887e26a0528",
//                        to = "+254710102720",
//                        transactionID = "23232323",
//                        username = "pension"
//                    )
//                )
//
//                Snackbar.make(requireView(), response.resultDesc.toString(), Snackbar.LENGTH_SHORT).show()
//            } catch (e: Exception) {
//                e.localizedMessage?.let { toast(it) }
//            }
//        }
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SMSBroadcastReceiver()
        smsBroadcastReceiver.setOTPListener(object :
                SMSBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    intent?.let { context ->
                        startActivityForResult(
                            context,
                            REQ_USER_CONSENT
                        )
                    }
                }

                override fun onFailure() {
                    toast("Something went wrong. Please try again.")
                }
            })

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_USER_CONSENT -> {
                if ((resultCode == Activity.RESULT_OK) && (data != null)) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    val code = message?.let { fetchVerificationCode(it) }?.toInt()
                    next()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    toast("You require this permission to read your One-Time PIN and access accoount successfully")
                }
            }
        }
    }

    private fun fetchVerificationCode(message: String): String {
        return Regex("(\\d{4})").find(message)?.value ?: ""
    }

    private fun setUpUi() {
        binding.apply {
            imageView8.setOnClickListener {
                next()
            }
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(requireContext()).also {
            it.startSmsUserConsent("ECLECTICS")
                .addOnSuccessListener { }
                .addOnFailureListener {
                    toast("Something went wrong. Please try again later.")
                }
        }
    }

    companion object {
        private const val REQ_USER_CONSENT = 100
        private const val TAG = "VerificationCodeFragmen"
    }
}
