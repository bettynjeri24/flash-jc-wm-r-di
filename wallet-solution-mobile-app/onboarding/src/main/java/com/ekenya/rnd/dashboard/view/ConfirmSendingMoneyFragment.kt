package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showErrorSnackBar
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.ConfirmSendingMoneyFragmentBinding

class ConfirmSendingMoneyFragment : Fragment() {
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var binding: ConfirmSendingMoneyFragmentBinding

    private lateinit var selectedOption: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ConfirmSendingMoneyFragmentBinding.inflate(inflater, container, false)

        initUi()
        initViewModel()
        initObservers()
        initClickListeners()
        return binding.root
    }

    private fun initClickListeners() {

        binding.btnSendNow.setOnClickListener {
            if (selectedOption.isNotEmpty() && selectedOption == "Tollo Wallet") {
                try {
                     authorizeTransaction()
                } catch (e: Exception) {
                    showErrorSnackBar(" Some Error Occured")
                }

            } else if (selectedOption.isNotEmpty() && selectedOption == "Debit Or Credit Card") {

                try {
                    //doCardPayments()
                } catch (e: Exception) {
                    showErrorSnackBar(" Some Error Occured")
                }
            }
        }
    }

    private fun authorizeTransaction() {
        findNavController().navigate(R.id.confirmSendingMoneyFragment_to_authorizeTransaction)
    }


    private fun initObservers() {
        confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner, {
            binding.senderName.text = it
            selectedOption = it

        })
        confirmSendingMoneyViewModel.receiversPhoneNumber.observe(viewLifecycleOwner,{
            binding.phoneNumber.text = it
        })


    }

    private fun initViewModel() {

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

        mobileWalletViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)
    }

    private fun initUi() {
        binding.sendingAmountValue.text =
            " GHS ${SharedPreferencesManager.getAmouttoTopUP(requireContext())}.00"


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

}