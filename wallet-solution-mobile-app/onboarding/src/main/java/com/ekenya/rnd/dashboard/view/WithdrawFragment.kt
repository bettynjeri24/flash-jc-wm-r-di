package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.WithdrawFragmentBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class WithdrawFragment : BaseDaggerFragment() {
    private lateinit var binding: WithdrawFragmentBinding
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var transactionViewModel: TransactionConfirmationViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel

    private lateinit var viewModel: MobileWalletViewModel
    private var amount: Int = 0
    private lateinit var agentId: String
    private lateinit var agentName: String
    private var selectedOption = " "
    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireActivity(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WithdrawFragmentBinding.inflate(inflater, container, false)
        binding.tvWalletBalance.text = "Balance " +
                SharedPreferencesManager.getAccountBalance(requireContext())!!

        initializeViewModel()
        setclickListeners()
        initObservers()
        initSpinner()

        return binding.root
    }

    private fun initializeViewModel() {
        transactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(TransactionConfirmationViewModel::class.java)

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
        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)
        confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)

    }

    private fun setclickListeners() {

        binding.cardviewScan.setOnClickListener{
            barcodeLauncher.launch(ScanOptions())

        }

        binding.btnContinue.setOnClickListener {


                if (validDetails()) {

                        confirmSendingMoneyViewModel.setAgentNumber(agentId)
                    confirmSendingMoneyViewModel.setRequestingFragment(Constants.WITHDRAW_FRAGMENT)
                    authorizeTransactionViewModel.setRequestingFragment(Constants.WITHDRAW_FRAGMENT)
                        findNavController().navigate(R.id.withdrawConfirmationFragment)
                   // showCardPaymentOptionsDialog(Constants.WITHDRAW_FRAGMENT)



            }
        }
    }

    private fun initObservers() {
        confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner) {
            selectedOption = it
        }
    }

    private fun initSpinner() {

        val agentAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.fromAgent, R.layout.spinner_item
        )

        agentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)



        binding.tvFrom.adapter = agentAdapter
    }

    private fun validDetails(): Boolean {
        val etamount = binding.etAmount.text.toString().trim()


        agentId = binding.etAgentId.text.toString().trim()

        if (etamount.isNullOrBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        }
        if (agentId.isNullOrBlank()) {
            binding.etAgentId.error = "Please enter a valid Agent ID"
            binding.etAgentId.requestFocus()
            return false
        } else {
            amount = Integer.parseInt(etamount)

        }

        return true
    }



}