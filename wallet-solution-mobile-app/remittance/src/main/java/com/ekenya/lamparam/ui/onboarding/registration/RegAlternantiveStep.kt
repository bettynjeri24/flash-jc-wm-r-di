package com.ekenya.lamparam.ui.onboarding.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.ekenya.lamparam.R
import com.ekenya.lamparam.databinding.FragmentRegAlternantiveStepBinding
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.Status
import com.ekenya.lamparam.user.UserDataRepository
import com.ekenya.lamparam.utilities.FieldValidation
import com.ekenya.lamparam.utilities.GlobalMethods
import com.ekenya.lamparam.utilities.UtilityClass
import kotlinx.android.synthetic.main.fragment_reg_alternantive_step.*
import kotlinx.android.synthetic.main.header_layout.*
import javax.inject.Inject

class RegAlternantiveStep : Fragment() {
    private lateinit var navOptions: NavOptions

    private var _binding: FragmentRegAlternantiveStepBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() { viewModelFactory }

    @Inject
    lateinit var globalMethods: GlobalMethods

    @Inject
    lateinit var userDataRepository: UserDataRepository

    private lateinit var user: RegFields

    private lateinit var referralCode: String
    private lateinit var email: String
    private lateinit var occupation: String
    private lateinit var altMobile: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegAlternantiveStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvActionTitle.text = resources.getString(R.string.str_just_one_more_step)
        ((activity as OnBoardingActivity).onBackClick(btn_back, view)) //back button
        onboardingViewModel.userData.observe(viewLifecycleOwner, {
            it?.let { user = it }
        })

        navOptions = UtilityClass().getNavoptions()

        val list = listOf("Teacher", "Farmer", "Accountant", "Doctor", "Engineer", "Other")
        val adapter: ArrayAdapter<String> =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, list) }!!
        binding.etOccupation.setAdapter(adapter)
        binding.etOccupation.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.etOccupation.showDropDown()
            false
        })

        binding.btnCreateAccount.setOnClickListener {
            if (validateFields()) {
                onboardingViewModel.setAltDetails(referralCode, email, altMobile, occupation)
                sendRequest()
//                findNavController().navigate(
//                    R.id.nav_verificationCode,
//                    null,
//                    navOptions)
            }
        }
    }

    /**
     * Sends request to perform account lookup
     */
    private fun sendRequest() {
        onboardingViewModel.register(user).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        globalMethods.loader(requireActivity(), 1)
                        resource.data?.let { data -> parseData(data.body()!!) }
                    }
                    Status.ERROR -> {
                        globalMethods.loader(requireActivity(), 1)
                        globalMethods.transactionError(requireActivity(), it.message!!)
                    }
                    Status.LOADING -> {
                        globalMethods.loader(requireActivity(), 0)
                    }
                }
            }
        })
    }

    private fun parseData(body: DefaultResponse) {
        if (body.responseCode == "00") {
            onboardingViewModel.setOtp(body.pwd)
            userDataRepository.saveUser(body.phoneNumber, "")

                findNavController().navigate(
                    R.id.nav_verificationCode,
                    null,
                    navOptions)
        } else {
            globalMethods.transactionError(requireActivity(), body.responseMessage)
        }
    }


    private fun validateFields(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        referralCode = binding.etReferralCode.text.toString()
        email = binding.etEmail.text.toString()
        altMobile = binding.etAlternativePhone.text.toString()
        occupation = binding.etOccupation.text.toString()
        val validreferralCode =
            FieldValidation().validRefNumber(referralCode, "Referral Number")

        if (!validreferralCode.contentEquals(validMsg)) {
            binding.etReferralCode.requestFocus()
            tlReferalCode.error = validreferralCode
            return false
        } else {
            tlReferalCode.error = null
            binding.etReferralCode.clearFocus()
        }

        return true
    }

}