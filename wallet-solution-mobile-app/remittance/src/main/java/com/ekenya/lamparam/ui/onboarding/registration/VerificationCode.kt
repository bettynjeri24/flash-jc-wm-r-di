package com.ekenya.lamparam.ui.onboarding.registration

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentVerificationCodeBinding
import com.ekenya.lamparam.utilities.SmsBroadcastReceiver
import com.ekenya.lamparam.viewmodel.VerificationCodeViewModel

import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.main_toolbar.*
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class VerificationCode : Fragment(), TextWatcher {

    private lateinit var viewModel: VerificationCodeViewModel
    private lateinit var binding: FragmentVerificationCodeBinding

    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

    private lateinit var fullName: String
    private lateinit var phoneNumber: String
    private lateinit var dob: String
    private lateinit var address: String
    private lateinit var docType: String
    private lateinit var docNumber: String

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() {viewModelFactory}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startSmsUserConsent()
        registerToSmsBroadcastReceiver()
        tvActionTitle.text = getString(R.string.verification_code)

        val args = requireArguments()
        phoneNumber = args.getString("phone", "")
        fullName = args.getString("name", "")
        dob = args.getString("dob", "")
        address = args.getString("address", "")
        docType = args.getString("docType", "")
        docNumber = args.getString("docNumber", "")
        binding.tvDescription.text = getString(R.string.enter_4_digit_code_instr, phoneNumber)

        ((activity as OnBoardingActivity).onBackClick(btn_back, view))

        viewModel = ViewModelProvider(this).get(VerificationCodeViewModel::class.java)

        viewModel.currentTime.observe(viewLifecycleOwner, Observer {
            if (it >= 0) {
                updateTextView(it)
            }
        })

        binding.btnVerification.setOnClickListener {
            if(valid()){
                findNavController().navigate(R.id.changePinFragment)
            }
        }

        binding.et1.addTextChangedListener(this)
        binding.et2.addTextChangedListener(this)
        binding.et3.addTextChangedListener(this)
        binding.et4.addTextChangedListener(this)
//        binding.et4.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
//            }
//
//        })
    }

    private fun valid(): Boolean {
        val code = binding.et1.text.toString()+binding.et2.text.toString()+binding.et3.text.toString()+binding.et4.text.toString()
        if (code.length<4){
            return false
        }

        return true
    }

    private fun updateTextView(it: Long) {
        binding.tvTimeout.text = getString(R.string.resend_code_timeout, it)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = requireActivity().currentFocus as TextView?

        if (text != null && text.length() > 0) {
            val next =
                text.focusSearch(View.FOCUS_RIGHT) // or FOCUS_FORWARD
            next?.requestFocus()
        }

        if (text != null && text.length() == 0) {
            val next =
                text.focusSearch(View.FOCUS_LEFT) // or FOCUS_FORWARD

            next?.requestFocus()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQ_USER_CONSENT -> {
                if ((resultCode == Activity.RESULT_OK) && (data != null)) {
                    //That gives all message to us. We need to get the code from inside with regex
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    val code = message?.let { fetchVerificationCode(it) }

                    if (code!=null){
                        binding.apply {
                            et1.setText(code[0].toString())
                            et2.setText(code[1].toString())
                            et3.setText(code[2].toString())
                            et4.setText(code[3].toString())
                        }
                    }
                }
            }
        }
    }

    private fun startSmsUserConsent() {
        SmsRetriever.getClient(requireActivity()).also {
            //We can add user phone number or leave it blank
            it.startSmsUserConsent("ECLECTICS")
                .addOnSuccessListener {
                    Log.d(TAG, "LISTENING_SUCCESS")
                }
                .addOnFailureListener {
                    Log.d(TAG, "LISTENING_FAILURE")
                }
        }
    }

    private fun registerToSmsBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver().also {
            it.smsBroadcastReceiverListener = object : SmsBroadcastReceiver.SmsBroadcastReceiverListener {
                override fun onSuccess(intent: Intent?) {
                    intent?.let { context -> startActivityForResult(context, REQ_USER_CONSENT) }
                }

                override fun onFailure() {}
            }
        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    private fun fetchVerificationCode(message: String): String {
        val p = Pattern.compile("(\\d{4})")
        val m = p.matcher(message)
        return if (m.find()) {
            m.group(0) ?: "0000"
        } else "0000"
    }

    companion object {
        const val TAG = "SMS_USER_CONSENT"
        const val REQ_USER_CONSENT = 100
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(smsBroadcastReceiver)
    }

}