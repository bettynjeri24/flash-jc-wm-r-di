package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.utils.blurPhoneNumber
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.onboarding.databinding.FragmentResetPinBinding
import com.ekenya.rnd.onboarding.ui.ResetPasswordViewModel


class ResetPinFragment : Fragment() {

    private  lateinit var binding : FragmentResetPinBinding
    private  lateinit var sms: CheckBox
    private  lateinit var email: CheckBox


    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPinBinding.inflate(inflater,container,false)


        initUi()
        initClickListeners()

        return binding.root
    }

    private fun initClickListeners() {


        binding.checkboxNumber.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkboxEmail.isChecked=false

                toastMessage("ischeckedsms")
            }
        }
        binding.checkboxEmail.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.checkboxNumber.isChecked=true

                toastMessage("feature coming soon")
            }
        }

        binding.btnGetPassword.setOnClickListener{

        }

    }

    private fun initUi() {
        binding.checkboxNumber.text = "Via SMS ${blurPhoneNumber(requireContext())}"
        sms = binding.checkboxNumber
        email = binding.checkboxEmail
    }

    override fun onStop() {
        super.onStop()
        showSupportActionBar()
    }
    override fun onResume() {
        super.onResume()
        showSupportActionBar()
    }

}