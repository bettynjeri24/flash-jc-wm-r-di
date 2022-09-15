package com.ekenya.rnd.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.blurPhoneNumber
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.databinding.FragmentResetPinBinding


class ResetPasswordFragment : BaseDaggerFragment() {

    private  lateinit var binding : FragmentResetPinBinding
    private  lateinit var sms: CheckBox
    private  lateinit var email: CheckBox

    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPinBinding.inflate(inflater,container,false)

        toastMessage("hey misskorir")

        initUi()
        initChangeListeners()

        return binding.root
    }

    private fun initChangeListeners() {


        binding.checkboxNumber.setOnCheckedChangeListener { buttonView, isChecked ->
        toastMessage("isChecked")

        }
        sms.setOnCheckedChangeListener { _, _ ->
            if (!email.isChecked && !sms.isChecked) {
                email.isChecked = true
            }
        }
        email.setOnCheckedChangeListener { _, _ ->
            if (!sms.isChecked && !email.isChecked) {
                sms.isChecked = true
            }
        }
    }

    private fun initUi() {
        binding.checkboxNumber.text = "Via SMS ${blurPhoneNumber(requireContext())}"
        sms = binding.checkboxNumber
        email = binding.checkboxEmail
    }


    override fun onResume() {
        super.onResume()
        showSupportActionBar()
    }

}