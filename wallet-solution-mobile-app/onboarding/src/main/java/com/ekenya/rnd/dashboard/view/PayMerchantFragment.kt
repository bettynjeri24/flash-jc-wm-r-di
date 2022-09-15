package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
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
import com.ekenya.rnd.dashboard.datadashboard.model.MerchantPayload
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.utils.AppUtils.afterTextChanged
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.PayMerchantFragmentBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.*


class PayMerchantFragment : BaseDaggerFragment() {

    private lateinit var binding: PayMerchantFragmentBinding
    private var amount: Int = 0
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private var store_label = "Shoprite Garden City"

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract())
    { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            val merchantPayload = MerchantPayload(qr_code = result.contents)
            getMerchantDetails(merchantPayload)
        }
    }


    private fun getMerchantDetails(merchantPayload: MerchantPayload) {

        val token = SharedPreferencesManager.getnewToken(requireContext())!!

        mobileWalletViewModel.scanMerchantQRCode(token, MainDataObject(merchantPayload))
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            // binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {
                                    toastMessage("Session Expired Login to Continue")
                                    findNavController().navigate(R.id.loginFragment2)

                                }
                                "00" -> {

                                    binding.etMerchantid.setText(it.data!!.data.transaction_details.additional_data.bill_number)

                                }
                                "57" -> {

                                    store_label =
                                        it.data!!.data.transaction_details.additional_data.store_label.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.getDefault()
                                            ) else it.toString()
                                        }
                                    confirmSendingMoneyViewModel.setDstvAccountName(store_label)

                                    binding.etMerchantid.setText(it.data!!.data.transaction_details.additional_data.bill_number)

                                }
                                else -> {
                                    toastMessage("Merchant Not Found")
                                }
                            }

                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = PayMerchantFragmentBinding.inflate(inflater, container, false)
        initUI()
        initViewModel()
        setSharedElementTransition()
        initClickListeners()

        return binding.root
    }

    private fun initUI()
    {
        binding.tvWalletBalance.text =" Current Balance ${SharedPreferencesManager.getAccountBalance(requireContext())!!}"

        binding.etMerchantid.afterTextChanged {
            binding.etReferenceNo.text.clear()
        }
        binding.etReferenceNo.afterTextChanged {
            binding.etMerchantid.text.clear()
        }

    }

    private fun initViewModel() {

        mobileWalletViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(MobileWalletViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

        confirmSendingMoneyViewModel.setRequestingFragment(Constants.BOTSWANA_POWER_FRAGMENT)

    }


    private fun initClickListeners() {

        binding.cardviewScan.setOnClickListener {
            barcodeLauncher.launch(ScanOptions())

        }

        binding.btnContinue.setOnClickListener {
            if (validDetails()) {
                if (!isInsufficientFunds(amount, requireContext())) {
                    showErrorSnackBar(getString(R.string.insufficient_balance))
                } else {

                    confirmSendingMoneyViewModel.setBotswanaPMetreNumber(binding.etMerchantid.text.trim().toString())
                    confirmSendingMoneyViewModel.setDstvAccountName(store_label)
                    confirmSendingMoneyViewModel.setRequestingFragment(Constants.PAY_MERCHANT_FRAGMENT)

                    findNavController().navigate(R.id.billPaymentsConfirmationFragment)
                   // showCardPaymentOptionsDialog(Constants.PAY_MERCHANT_FRAGMENT)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
    }


    private fun validDetails(): Boolean {

        val etamount = binding.etAmount.text.toString().trim()
        val merchantID = binding.etMerchantid.text.toString().trim()
        val referenceID = binding.etReferenceNo.text.toString().trim()


        if (etamount.isBlank())
        {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()

            return false
        }
        else if (merchantID.isBlank() && referenceID.isBlank())
        {
            showErrorSnackBar("Please enter a valid Merchant ID or ReferenceID")
            return false
        }
        else
        {
            amount = Integer.parseInt(etamount)
            confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
            SharedPreferencesManager.setMerchantCode(requireContext(), merchantID)
        }

        return true
    }


}