package com.ekenya.rnd.dashboard.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.ekenya.rnd.dashboard.utils.setSharedElementTransition
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.SendToBankfragmentBinding


class SendToBankFragment : BaseDaggerFragment() {
    private lateinit var  binding: SendToBankfragmentBinding
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private var amount: Int = 0
    private lateinit var metreNumber: String
    private lateinit var accountName: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SendToBankfragmentBinding.inflate(inflater,container,false)

        initUI()
        setSharedElementTransition()
        binding.tvWalletBalance.text = "Balance " +
                SharedPreferencesManager.getAccountBalance(requireContext())!!

        initViewModel()
        setClickListeners()
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
        confirmSendingMoneyViewModel.setRequestingFragment(Constants.SEND_TO_BANK)
    }

    private fun initUI() {
        val items = listOf("National Bank of Ethiopia","Cooperative Bank of  Oromia",
            "Awash International Bank","Buna International Bank",
            "Commercial Bank of Ethiopia","Construction and Business Bank",
            "Bank of Abyssinia", "Berhan International Bank")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (binding.bankSpinner as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun setClickListeners() {
        binding.btnContinue.setOnClickListener{
           // showDialog()
            if (validDetails()){

            confirmSendingMoneyViewModel.setBotswanaPMetreNumber(metreNumber)
            confirmSendingMoneyViewModel.setDstvAccountName(accountName)
                findNavController().navigate(R.id.billPaymentsConfirmationFragment)

                //showCardPaymentOptionsDialog(Constants.SEND_TO_BANK)
           // toastMessage("Feature Coming Soon")
        }}
    }

    private fun validDetails(): Boolean {
        val etamount = binding.etAmount.text.toString().trim()

        metreNumber = binding.etAccountNumber.text.toString().trim()
        accountName = binding.etAccountname.text.toString().trim()

        if (etamount.isBlank()) {
            binding.etAmount.error = getString(R.string.invalidamount_errortext)
            binding.etAmount.requestFocus()
            return false
        }
        if (accountName.isBlank()) {
            binding.tilAccountName.error = "Please enter a valid Account Name"
            binding.tilAccountName.error
            return false
        }
        if (metreNumber.isBlank() || metreNumber.length < 8) {
            binding.tilAccountNumber.error = "Please enter a valid Account number"
            binding.tilAccountNumber.error
            return false
        }

       else {
            amount = Integer.parseInt(etamount)
            confirmSendingMoneyViewModel.setAmount(amount)
            SharedPreferencesManager.setAmount(requireContext(), amount.toString())
        }

        return true
    }


    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fund_wallet_bottomsheet)

        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
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