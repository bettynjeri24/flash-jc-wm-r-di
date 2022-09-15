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
import com.ekenya.rnd.onboarding.databinding.FragmentDstvPaymentsBinding
import kotlinx.coroutines.*


class DstvPayments : Fragment() {

    private lateinit var binding: FragmentDstvPaymentsBinding
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel

    private var amount: Int = 0
    private lateinit var accountName: String
    private lateinit var accountNumber: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedElementTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDstvPaymentsBinding.inflate(inflater, container, false)

        initViewModel()
        setOnclickListeners()

        return binding.root
    }

    private fun initViewModel() {
        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.DSTV_PAYMENTS_FRAGMENT)

    }

    private fun setOnclickListeners() {

        binding.tvWalletBalance.text =
            "Balance : ${SharedPreferencesManager.getAccountBalance(requireContext())}0"


        binding.btnContinue.setOnClickListener {
            if (binding.btnContinue.text.equals(getString(R.string.account_lookup))) {
                binding.progressBar.visibility = View.VISIBLE
                GlobalScope.launch {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        completeLookup()
                    }
                }
            } else {

                if (validDetails()) {

                        confirmSendingMoneyViewModel.setDstvAccountNumber(accountNumber)
                        confirmSendingMoneyViewModel.setDstvAccountName(accountName)
                        confirmSendingMoneyViewModel.setRequestingFragment(Constants.DSTV_PAYMENTS_FRAGMENT)
                        findNavController().navigate(R.id.billPaymentsConfirmationFragment)
                        // showCardPaymentOptionsDialog(Constants.DSTV_PAYMENTS_FRAGMENT)

                }

            }
        }


    }


    private fun completeLookup()
    {

        binding.progressBar.visibility = View.GONE

        binding.tilAccountName.visibility = View.VISIBLE
        binding.amountlayout.visibility = View.VISIBLE

        binding.tvWalletBalance.text =
            "Balance : ${SharedPreferencesManager.getAccountBalance(requireContext())}0"
        binding.etAccountname.setText("Bilal munaj")
        binding.etAmount.hint = "1290"
        binding.btnContinue.text = getString(R.string.txt_continue)
    }

    private fun validDetails(): Boolean
    {
        val etAmount = binding.etAmount.text.toString().trim()

        accountNumber = binding.etAccountNumber.text.toString().trim()
        accountName = binding.etAccountname.text.toString().trim()

        if (etAmount.isBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        }

        if (accountName.isBlank() ) {
            binding.tilAccountName.error = getString(R.string.enter_valid_name_txt_err)
            binding.tilAccountName.error
            return false
        }
        if (accountNumber.isBlank()) {
            binding.tilAccountNumber.error = getString(R.string.enter_valid_account_name_txt_err)
            binding.tilAccountNumber.error
            return false
        } else {
            amount = Integer.parseInt(etAmount)
            confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
        }

        return true
    }


}