package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
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
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentPayUsingPointsBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import java.util.*

class PayUsingPointsFragment : Fragment() {

    private lateinit var binding:FragmentPayUsingPointsBinding

    private var amount: Int = 0
    private lateinit var merchantID: String
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private var store_label = "Shoprite Garden City"

    private val barcodeLauncher: ActivityResultLauncher<ScanOptions> = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            val merchantPayload = MerchantPayload(qr_code = result.contents)
            getMerchantDetails(merchantPayload)
            /*Toast.makeText(requireActivity(), "Scanned: " + result.contents, Toast.LENGTH_LONG)
                .show()*/
        }
    }



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
        binding = FragmentPayUsingPointsBinding.inflate(layoutInflater)

        binding.tvWalletBalance.text =" Current Balance ${SharedPreferencesManager.getAccountBalance(requireContext())!!}"
        initViewModel()
        setSharedElementTransition()
        initClickListeners()
        return binding.root
    }
    private fun initClickListeners() {
        //launch Scanner

        binding.cardviewScan.setOnClickListener {
            barcodeLauncher.launch(ScanOptions())

        }
        binding.etAmount.addTextChangedListener(textWatcher)
        binding.btnContinue.setOnClickListener {
            if (validDetails()) {


                    confirmSendingMoneyViewModel.setBotswanaPMetreNumber(merchantID)
                    confirmSendingMoneyViewModel.setDstvAccountName(store_label)
                    confirmSendingMoneyViewModel.setRequestingFragment(Constants.PAY_MERCHANT_FRAGMENT)

                   // showCardPaymentOptionsDialog(Constants.PAY_MERCHANT_FRAGMENT)
            }
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

    private fun validDetails(): Boolean {
        val etamount = binding.etAmount.text.toString().trim()

        merchantID = binding.etMerchantid.text.toString().trim()

        if (etamount.isBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        } else
            if (merchantID.isBlank()) {
                binding.etMerchantid.error = "Please enter a valid Merchant ID"
                binding.etMerchantid.error
                return false
            } else {
                amount = Integer.parseInt(etamount)
                confirmSendingMoneyViewModel.setAmount(amount)
                SharedPreferencesManager.setAmount(requireContext(), amount.toString())
                SharedPreferencesManager.setMerchantCode(requireContext(), merchantID)
            }

        return true
    }

    private fun getMerchantDetails(merchantPayload: MerchantPayload) {

        //check on paymentsPayload

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
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start > 0) {
                binding.tvPointsConversions.makeVisible()
                binding.tvPointsConversions.text = "GHS $s for 8,000 Tollo points"

            }else{
                binding.tvPointsConversions.makeInvisible()


            }

            /*if (start == 0) {
                Toast.makeText(context, "start Limit Reached", Toast.LENGTH_SHORT)
                    .show()
            }*/
        }
    }

}