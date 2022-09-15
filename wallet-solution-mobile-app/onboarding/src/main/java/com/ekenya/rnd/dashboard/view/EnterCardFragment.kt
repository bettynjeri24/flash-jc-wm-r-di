package com.ekenya.rnd.dashboard.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.onboarding.BuildConfig
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentEnterCardBinding
import org.json.JSONObject

class EnterCardFragment : BaseDaggerFragment() {

    private lateinit var binding: FragmentEnterCardBinding
    private lateinit var transactionViewModel: TransactionConfirmationViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel

    private lateinit var cardNumber: String
    private lateinit var expiryDay: String
    private lateinit var cvv: String
    private lateinit var requestingFragment: String
    private var cardPayload: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEnterCardBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)


        initViewModel()
        initClickListeners()
        setObservers()
        hardCodeCardDetails()

        return binding.root
    }

    private fun hardCodeCardDetails() {
        if (BuildConfig.DEBUG) {
            binding.etCardNumber.setText("4242424242424242")
            binding.etExpiry.setText("122025")
            binding.etCvv.setText("123")

        }
    }

    private fun setObservers() {
        confirmSendingMoneyViewModel.requestingFragment.observe(viewLifecycleOwner, {
            requestingFragment = it
        })
    }

    private fun initViewModel() {

        transactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(TransactionConfirmationViewModel::class.java)
        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)

    }

    private fun initClickListeners() {

        binding.btnContinue.setOnClickListener {
            if (validDetails()) {
                SharedPreferencesManager.setCardNumber(requireContext(),cardNumber)


                when (requestingFragment) {
                    "topupwallet" -> {
                        authorizeTransactionViewModel.setRequestingFragment(Constants.DO_CARD_PAYMENTS)

                        findNavController().navigate(R.id.enterCardFragment_to_transactionconfirmation)

                    }
                    "sendtomobile" -> {
                        findNavController().navigate(R.id.enterCardFragment_to_confirmsendingmoney)
                    }
                    "withdrawfromwallet" -> {
                        findNavController().navigate(R.id.withdrawConfirmationFragment)
                    }
                    "dstvpayments" -> {
                        findNavController().navigate(R.id.billPaymentsConfirmationFragment)
                    }
                }

            }
        }
    }


    private fun validDetails(): Boolean {
        cardNumber = binding.etCardNumber.text.toString().trim()
        expiryDay = binding.etExpiry.text.toString().trim()
        cvv = binding.etCvv.text.toString().trim()
        if (cardNumber.isNullOrBlank() || cardNumber.length != 16) {
            binding.tilCardNumber.error = null
            binding.tilCardNumber.isErrorEnabled = false
            binding.tilCardNumber.error = "Please enter a valid Card Number"
            return false
        }

        if (expiryDay.isNullOrBlank() || expiryDay.length > 6) {
            binding.tilExpirydate.error = "Please enter an Expiry Date"
            binding.tilExpirydate.error
            return false
        }
        if (cvv.isNullOrBlank() || cvv.length !=3) {
            binding.tilCvv.error = "Please enter CVV"
            binding.tilCvv.error
            return false
        }

        encryptCardPayload()
        return true
    }

    private fun encryptCardPayload() {

        val cardObject = JSONObject()
        cardObject.put("expiry_date", expiryDay)
        cardObject.put("card_number", cardNumber)
        cardObject.put("csv", cvv)

       // cardPayload = encryptRSA(cardObject.toString())!!

        SharedPreferencesManager.setcardDetails(requireContext(), "isTcW8R27Wv6dJYkqYbSjjBgvufRVRb0Q+C7SiUV9GU8HAtk7E1fyGIiWkDe2FY61VyUVGz3Keq/tkNb9c0pWb2wR7Rnni0I4aUYoOpKT8ZaatefwIKIlJaKFUll1eSqk9iw9wsDmG+hGFbgpsHpaar/7Tiuvq5rksyOFbrePif5ACaDEkJ2xINRr1h/KgIgWJeukW0wGHCua77mjbGyFrHrMEvMJFO5/iznXR6joPVV8MUoR01XBXFnHlbyJqj02dmKbJ/BYYtKHuKebj2QtOsrkMSDdWMamwm0CFXI8nys1GemrfexDdk7fbAGPLEAr6IN/hZ1lymTFBFyI/rFeQ=="/*cardPayload*/)
        //SharedPreferencesManager.setcardDetails(requireContext(), cardPayload)
        Log.d("TAG", "encryptCardPayload: $cardPayload")

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.scan_creditcard, menu)
        menu.getItem(0).isVisible = true


        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (when (item.itemId) {
            R.id.action_scanCard -> {

                toastMessage("Feature Coming")
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        })
    }


}