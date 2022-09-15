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
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentBotswanaPowerBinding


class BotswanaPowerFragment : Fragment() {
    private lateinit var binding: FragmentBotswanaPowerBinding
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private var amount: Int = 0
    private lateinit var metreNumber: String
    private lateinit var accountName: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBotswanaPowerBinding.inflate(layoutInflater, container, false)

        setOnClickListeners()
        initViewModel()
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        confirmSendingMoneyViewModel.botswanaPMetreNumber.observe(viewLifecycleOwner, {
            binding.etMetreNumber.setText(it)
        })

        confirmSendingMoneyViewModel.customerMetreNumberName.observe(viewLifecycleOwner, {
            binding.etMetreName.setText(it)
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

        confirmSendingMoneyViewModel.setRequestingFragment(Constants.BOTSWANA_POWER_FRAGMENT)

    }

    private fun setOnClickListeners() {

        binding.tvWalletBalance.text =
            "Balance : ${SharedPreferencesManager.getAccountBalance(requireContext())}0"

        binding.btnContinue.setOnClickListener {

            if (validDetails()) {

                    confirmSendingMoneyViewModel.setDstvAccountName(accountName)
                    confirmSendingMoneyViewModel.setBotswanaPMetreNumber(metreNumber)
                    confirmSendingMoneyViewModel.setRequestingFragment(Constants.BOTSWANA_POWER_FRAGMENT)
                    findNavController().navigate(R.id.billPaymentsConfirmationFragment)

                   // showCardPaymentOptionsDialog(Constants.BOTSWANA_POWER_FRAGMENT)

            }

        }
    }


    private fun validDetails(): Boolean {
        val etamount = binding.etAmount.text.toString().trim()

        metreNumber = binding.etMetreNumber.text.toString().trim()
        accountName = binding.etMetreName.text.toString().trim()


        if (metreNumber.isBlank() || metreNumber.length != 11) {
            binding.tilMetreNumber.error = "Please enter a valid Metre number"
            binding.tilMetreNumber.error
            return false
        }
        if (accountName.isBlank() ) {
            binding.tilMetreName.error = "Please enter a valid Account Name"
            binding.tilMetreName.error
            return false
        }


        if (etamount.isBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        } else {
            amount = Integer.parseInt(etamount)
            confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
        }

        return true
    }


}