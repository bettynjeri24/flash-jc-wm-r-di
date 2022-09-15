package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.SendToMobileFragmentBinding
import com.hbb20.CountryCodePicker


class SendToMobileFragment : BaseDaggerFragment() {
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private  lateinit var transactionViewModel: TransactionConfirmationViewModel

    private lateinit var binding: SendToMobileFragmentBinding
    private var amount: Int = 0
    private lateinit var phoneNumber: String
    private  var selectedOption = " "
    private lateinit var countryCode: String
    private lateinit var countryCodeTv: CountryCodePicker



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SendToMobileFragmentBinding.inflate(inflater, container, false)
        binding.tvWalletBalance.text = "Balance " +
                SharedPreferencesManager.getAccountBalance(requireContext())!!
        initUi()
        setSharedElementTransition()

        setupViewModel()
        setClickListeners()
        initObservers()

        return binding.root
    }

    private fun initUi()
    {
        val items = listOf( "Safaricom Mpesa" ,"Ethio Cash", "TeleBirr Cash")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.providerSpinner as? AutoCompleteTextView)?.setAdapter(adapter)

        countryCodeTv = binding.ccp
        countryCode = countryCodeTv.defaultCountryCode
    }

    private fun setClickListeners() {
        countryCodeTv.setOnCountryChangeListener {
            countryCode = countryCodeTv.selectedCountryCode
        }
        binding.icContacts.setOnClickListener {
            toastMessage("Feature Coming soon")
        }
        binding.btnContinue.setOnClickListener {
            if (validDetails()){
                confirmSendingMoneyViewModel.setReceiversPhoneNumber(countryCode + phoneNumber)
                /*confirmSendingMoneyViewModel.setBotswanaPMetreNumber(metreNumber)
                confirmSendingMoneyViewModel.setDstvAccountName(accountName)*/
                findNavController().navigate(R.id.sendMoneyConfirmationFragment)
                //showCardPaymentOptionsDialog(Constants.SEND_TO_MOBILE)
            }


        }
    }
    private fun initObservers() {
        confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner, {
            selectedOption = it
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    private fun validDetails(): Boolean {
        val etAmount = binding.etAmount.text.toString().trim()
        phoneNumber = binding.etPhonenumber.text.toString().trim()

        if (phoneNumber.isNullOrBlank()) {
            binding.tilPhoneNumber.error = "Please enter a valid phone number"
            binding.tilPhoneNumber.error
            return false
        }

        if (phoneNumber.isNotEmpty()) {
            val temp = phoneNumber.toCharArray()[0]
            if (temp == '0') {
                phoneNumber = phoneNumber.substring(1)

            } else if (temp == '2') {
                phoneNumber = phoneNumber.substring(3)
            }
        }


        if (etAmount.isNullOrBlank()) {
            binding.etAmount.setError("Please Enter a Valid Amount")
            binding.etAmount.requestFocus()
            return false
        } else {
            amount = Integer.parseInt(etAmount)

            confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
        }
        return true
    }

    private fun setupViewModel() {
        transactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(TransactionConfirmationViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.SEND_TO_MOBILE)

        confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)


    }



}