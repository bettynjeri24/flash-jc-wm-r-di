package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentMascomAirtimeBinding


class MascomAirtimeFragment : Fragment() {
    private lateinit var binding: FragmentMascomAirtimeBinding
    private lateinit var viewModel: MobileWalletViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private var amount: Int = 0
    private lateinit var phoneNumber: String
    private var buyforSelf = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMascomAirtimeBinding.inflate(inflater, container, false)
        setSharedElementTransition()
        initUi()
        initViewModel()

        return binding.root
    }

    private fun initUi() {

        binding.btnContinue.setOnClickListener {
            if (validDetails()) {

                    confirmSendingMoneyViewModel.setDstvAccountName(
                        "${
                            SharedPreferencesManager.getFirstName(
                                requireContext()
                            )
                        }  ${SharedPreferencesManager.getMiddleName(requireContext())} ${
                            SharedPreferencesManager.getLastName(
                                requireContext()
                            )
                        }"
                    )
                    confirmSendingMoneyViewModel.setDstvAccountNumber("0$phoneNumber")
                    confirmSendingMoneyViewModel.setRequestingFragment(Constants.MASCOM_AIRTIME_FRAGMENT)
                    findNavController().navigate(R.id.billPaymentsConfirmationFragment)
                    // showCardPaymentOptionsDialog(Constants.MASCOM_AIRTIME_FRAGMENT)
                    //showDialog()

            }
        }
        binding.btnOtherNumber.setOnClickListener {
            buyforSelf = false
            makeActiveAirtimeOption(binding.btnOtherNumber, binding.btnOwnNumber)


            binding.ccp.visibility = View.VISIBLE
            binding.tilPhoneNumber.visibility = View.VISIBLE
        }
        binding.btnOwnNumber.setOnClickListener {
            buyforSelf = true
            makeActiveAirtimeOption(binding.btnOwnNumber, binding.btnOtherNumber)

            binding.ccp.visibility = View.INVISIBLE
            binding.tilPhoneNumber.visibility = View.INVISIBLE
        }

        binding.tvWalletBalance.text =
            "Balance : ${SharedPreferencesManager.getAccountBalance(requireContext())}0"


    }

    private fun initViewModel() {

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.BOTSWANA_POWER_FRAGMENT)


        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)


    }


    private fun validDetails(): Boolean {
        val eAmount = binding.etAmount.text.toString().trim()

        //if (phoneNumber)
        if (buyforSelf) {
            phoneNumber = SharedPreferencesManager.getPhoneNumber(requireContext()).toString()
        } else {
            phoneNumber = binding.etPhonenumber.text.toString().trim()
        }

        if (phoneNumber.isNotEmpty()) {
            val temp = phoneNumber.toCharArray()[0]
            if (temp == '0') {
                phoneNumber = phoneNumber.substring(1)

            } else if (temp == '2') {
                phoneNumber = phoneNumber.substring(3)
            }
        }




        if (phoneNumber.isNullOrBlank()) {
            binding.tilPhoneNumber.error = "Please enter a valid phone number"
            binding.tilPhoneNumber.error
            return false
        }
        if (eAmount.isNullOrBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        } else {
            amount = Integer.parseInt(eAmount)
            confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
        }

        return true
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