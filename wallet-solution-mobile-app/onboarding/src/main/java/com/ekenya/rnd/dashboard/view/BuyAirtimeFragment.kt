package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.data.model.BuyAirtimeReq
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
import com.ekenya.rnd.onboarding.databinding.FragmentOrangeAirtimeBinding


class BuyAirtimeFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentOrangeAirtimeBinding
    private lateinit var viewModel: MobileWalletViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private var amount: Int = 0
    private lateinit var phoneNumber: String
    private var buyforSelf = true


    override fun onCreateView(
        inflater: LayoutInflater,vg: ViewGroup?,
        bundle: Bundle?
    ): View?
    {

        binding = FragmentOrangeAirtimeBinding.inflate(inflater, vg, false)
        initViewModel()
        initUi()

        return binding.root
    }

    private fun initUi()
    {
        setSharedElementTransition()
        binding.btnContinue.setOnClickListener {
            if (validDetails()) {
                if (!isInsufficientFunds(amount, requireContext())) {
                    showErrorSnackBar(getString(R.string.insufficient_balance))
                }
                else
                {
                    confirmSendingMoneyViewModel.setBuyAirtimeReq(
                        BuyAirtimeReq(
                            getString(R.string.airtime_top_req),
                            10,
                            getString(R.string.buy_saf_service),
                            1,
                            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
                            "AirtimeTopup",
                            "AirtimeTopup",
                            false,
                            "",
                             SharedPreferencesManager.getAccountNumber(requireContext())!!,
                            AppUtils.geolocation,
                            AppUtils.userAgent,
                            AppUtils.userAgentVersion
                        )
                    )

                    findNavController().navigate(R.id.billPaymentsConfirmationFragment)

                }
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
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.BUY_AIRTIME_FRAGMENT)

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