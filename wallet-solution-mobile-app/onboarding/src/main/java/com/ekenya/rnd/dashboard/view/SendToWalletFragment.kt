package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.model.AccountLookUp
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.setSharedElementTransition
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentSendToWalletBinding
import com.hbb20.CountryCodePicker

class SendToWalletFragment : BaseDaggerFragment() {
    private lateinit var binding: FragmentSendToWalletBinding
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private var amount: Int = 0
    private lateinit var phoneNumber: String
    private lateinit var accountNumber: String
    private lateinit var viewModel: MobileWalletViewModel
    private lateinit var countryCode: String
    private lateinit var countryCodeTv: CountryCodePicker


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSendToWalletBinding.inflate(inflater, container, false)
        setSharedElementTransition()

        initUI()
        initSpinner()
        setListeners()
        setupViewModel()


        return binding.root
    }

    private fun setListeners() {
        countryCodeTv.setOnCountryChangeListener {
            countryCode = countryCodeTv.selectedCountryCode
        }

        binding.btnSend.setOnClickListener {
            if (validDetails()) {

                // showCardPaymentOptionsDialog(Constants.SEND_TO_WALLET)

                doAccountLookUP()
                // setupObservers()


            }

        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.SEND_TO_WALLET)

    }

    private fun initSpinner() {
  /*      val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_entries, R.layout.spinner_item
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.currencySpinner.adapter = adapter*/
    }

    private fun doAccountLookUP() {
        val data = MainDataObject(
            AccountLookUp(
                "AccountLookup",
                countryCode + phoneNumber,
                countryCode + phoneNumber,
                "Home",
                "android",
                "5.1"
            )
        )
        val token = context?.let { SharedPreferencesManager.getnewToken(it) }

        Log.d("TAG", "setupObservers: token I am using is $token")
        viewModel.doAccountLookUP(token!!, data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {

                    Status.SUCCESS -> {
                        val responseCode = it.data!!.data.response.responseCode


                        when (responseCode) {
                            "00" -> {
                                //getAccountNumber
                                accountNumber = it.data!!.data.transaction_details.account_number
                                val customer_names =
                                    it.data!!.data.transaction_details.customer_names
                                confirmSendingMoneyViewModel.setReceivingAccountNumber(accountNumber)
                                confirmSendingMoneyViewModel.setReceiversPhoneNumber(countryCode + phoneNumber)
                                confirmSendingMoneyViewModel.setReceiversName(customer_names)
                                findNavController().navigate(R.id.sendMoneyConfirmationFragment)
                               // showCardPaymentOptionsDialog(Constants.SEND_TO_WALLET)

                                //process send Money

                            }
                            "401"->{
                                toastMessage("Session Expired Login to Continue")
                                findNavController().navigate(R.id.loginFragment2)

                            }

                            else -> {
                                //toastMessage()
                                binding.progressBar.visibility = View.INVISIBLE
                            }


                        }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {

                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
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

    private fun initUI() {


        countryCodeTv = binding.ccp
        countryCode = countryCodeTv.defaultCountryCode


        binding.tvWalletBalance.text = "Balance " +
                SharedPreferencesManager.getAccountBalance(requireContext())!!
    }


}