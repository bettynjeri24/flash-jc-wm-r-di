package com.ekenya.lamparam.ui.onboarding.registration

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController

import com.ekenya.lamparam.R
import com.ekenya.lamparam.activities.onboarding.OnBoardingActivity
import com.ekenya.lamparam.activities.main.LampMainViewModel
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import com.ekenya.lamparam.databinding.FragmentCreateAccountBinding
import com.ekenya.lamparam.network.DefaultResponse
import com.ekenya.lamparam.network.Status
import com.ekenya.lamparam.user.UserDataRepository
import com.ekenya.lamparam.utilities.*
import javax.inject.Inject

class CreateAccount : Fragment() {

    private lateinit var navOptions: NavOptions
    private lateinit var lampMainViewModel: LampMainViewModel

    private var phoneNumber = ""
    private lateinit var account: String
    private var isRegistered = false

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    //initialized for all fragments
    private val onboardingViewModel: OnBoardingViewModel by activityViewModels() {viewModelFactory}

    @Inject
    lateinit var globalMethods: GlobalMethods

    @Inject
    lateinit var userDataRepository: UserDataRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as OnBoardingActivity).onboardingComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navOptions = UtilityClass().getNavoptions()
        lampMainViewModel = ViewModelProvider(this).get(LampMainViewModel::class.java)
        onboardingViewModel.setUser() //initialize user data for registration

        binding.apply {
            layoutHeader.tvActionTitle.text = resources.getText(R.string.str_phone_number)
            cpp.setCountryForPhoneCode(+254)
            cpp.showFlag(true)
            cpp.showNameCode(false)

            btnActivate.setOnClickListener {
                if (validateFields()) {
                    sendRequest()
                }
            }

        }
        //back button
        ((activity as OnBoardingActivity).onBackClick(binding.layoutHeader.btnBack, view))
    }

    /**
     * Sends request to perform account lookup
     */
    private fun sendRequest() {
        onboardingViewModel.accountLookUp(phoneNumber).observe(viewLifecycleOwner, {
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

    private fun parseData(result: DefaultResponse) {
        when(result.responseCode){
            "00"->{
                val accountDetails = result.accountLookup
                userDataRepository.saveUser(
                    result.phoneNumber,
                    accountDetails.accountName) //save details in shared preferences
                view?.findNavController()?.navigate(R.id.nav_pin, null, navOptions) //go to login
            }
            "06"->{
                onboardingViewModel.setUserPhone(phoneNumber)
                view?.findNavController()?.navigate(R.id.nav_regPersonalInfo, null, navOptions) //go to reg
            }else->{
            globalMethods.transactionError(requireActivity(), result.responseMessage)
            }
        }
    }

    private fun validateFields(): Boolean {
        val validMsg = FieldValidation.VALIDINPUT
        phoneNumber = binding.cpp.selectedCountryCode + binding.etPhone.text.toString()
        account = binding.etAcct.text.toString()

        val validPhone = FieldValidation().validPhoneNUmber(phoneNumber, "Phone Number")

        if (!validPhone.contentEquals(validMsg)) {
            binding.etPhone.requestFocus()
            binding.tlRegPhone.error = validPhone
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}