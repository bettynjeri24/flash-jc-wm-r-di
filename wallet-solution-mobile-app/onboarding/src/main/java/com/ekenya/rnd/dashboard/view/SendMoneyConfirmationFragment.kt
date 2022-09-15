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
import com.ekenya.rnd.dashboard.utils.lightStatusBar
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentSendMoneyConfirmationBinding

class SendMoneyConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentSendMoneyConfirmationBinding
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var mobileWalletViewModel: MobileWalletViewModel

    private lateinit var selectedOption: String
    private lateinit var requestingFragment: String
    private var amount: Int = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendMoneyConfirmationBinding.inflate(inflater, container, false)

        initUi()
        initViewModel()
        initObservers()
        initClickListeners()
        return binding.root
    }

    private fun initClickListeners() {

        binding.btnSendNow.setOnClickListener {

            authorizeTransaction()

        }
    }

    private fun authorizeTransaction() {

        when (requestingFragment) {
            Constants.SEND_TO_WALLET -> {
                authorizeTransactionViewModel.setRequestingFragment(Constants.SEND_TO_WALLET)

            }
            Constants.SEND_TO_MOBILE -> {
                authorizeTransactionViewModel.setRequestingFragment(Constants.SEND_TO_MOBILE)

            }
        }
        findNavController().navigate(R.id.authorizeTransactionFragment)
    }


    private fun initObservers() {

        confirmSendingMoneyViewModel.amount.observe(viewLifecycleOwner, {
            amount = it
        })
        confirmSendingMoneyViewModel.requestingFragment.observe(viewLifecycleOwner, {
            requestingFragment = it
        })
        confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner, {
            binding.tvSendingFromValue.text = it
            selectedOption = it

        })

        confirmSendingMoneyViewModel.receiversName.observe(viewLifecycleOwner, {
            binding.tvSendingToValue.text = it
        })
        confirmSendingMoneyViewModel.receiversPhoneNumber.observe(viewLifecycleOwner, {
            binding.tvPhoneNumberValue.text = it
        })


    }

    private fun initViewModel() {

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)

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
        binding.totalAmount.text =
            " GHS ${SharedPreferencesManager.getAmouttoTopUP(requireContext())}.00"


    }


}