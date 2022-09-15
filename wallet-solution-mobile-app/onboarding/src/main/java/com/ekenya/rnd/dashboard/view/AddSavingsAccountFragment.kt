package com.ekenya.rnd.dashboard.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.RoomDBViewModel
import com.ekenya.rnd.dashboard.viewmodels.SavingsViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentAddSavingsAccountBinding
import java.text.SimpleDateFormat
import java.util.*


class AddSavingsAccountFragment : Fragment() {
    private lateinit var binding: FragmentAddSavingsAccountBinding
    private lateinit var savingsViewmodel: SavingsViewModel
    private lateinit var roomDBViewModel: RoomDBViewModel
    private lateinit var confirmSendingMoneyViewModel:ConfirmSendingMoneyViewModel

    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var accountType: String
    private lateinit var amount: String
    private lateinit var depositAmount: String
    private lateinit var date: String
    private lateinit var savingsTitle: String
    private lateinit var savingsCategory: String
    private lateinit var amountSaved: String
    private lateinit var savingsCycle: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddSavingsAccountBinding.inflate(inflater, container, false)


        initUI()
        initViewModel()
        initObservers()
        initClickListeners()

        return binding.root
    }


    private fun initUI() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            resources.getStringArray(R.array.frequency)
        )
        (binding.textField as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun initObservers() {

        savingsViewmodel.accountType.observe(viewLifecycleOwner, {
            binding.tvAccountType.text = it
            // toastMessage(it)
            accountType = it
        })

    }

    private fun initClickListeners() {
        binding.tvWalletBalance.text =
            "Balance : ${SharedPreferencesManager.getAccountBalance(requireContext())}0"

        binding.btnContinue.setOnClickListener {
            if (validDetails()) {
                authorizeTransactionViewModel.setRequestingFragment(Constants.ADD_SAVINGS_FRAGMENT)
                //if personal savings account display bottom dialog else proceed to select friends
                if (accountType == getString(R.string.personal_savings_text)) {

                  //  showDialog()
                } else {
                    Navigation.findNavController(binding.tilSavingstitle)
                        .navigate(R.id.action_addsavingsAccount_to_inviteFriends)

                }
            }
        }

        val cal = Calendar.getInstance()
        var date = ""
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.etCalender.setText(sdf.format(cal.time).toString())
                SharedPreferencesManager.setDateOfBirth(requireContext(), true)
            }


        binding.etCalender.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

            }
        }
    }


    private fun initViewModel() {
         confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
        confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)

        roomDBViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(RoomDBViewModel::class.java)
        savingsViewmodel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(SavingsViewModel::class.java)
    }

    /*
        private fun showDialog() {
            val bottomsheetlayoutBinding = PaymentsModeBottomsheetBinding.inflate(layoutInflater)
            val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
            dialog.setContentView(bottomsheetlayoutBinding.root)
            dialog.show()
            bottomsheetlayoutBinding.cardViewMobileMoney.makeVisible()
            bottomsheetlayoutBinding.cardViewWallet.setOnClickListener {

                toastMessage("Feature Coming Soon")
                //after successful payments add to db


            }
            bottomsheetlayoutBinding.cardViewMobileMoney.setOnClickListener {
                toastMessage("Feature Coming Soon")

            }
            bottomsheetlayoutBinding.cardviewDebitorCredit.setOnClickListener {
                toastMessage("Feature Coming Soon")

            }
            bottomsheetlayoutBinding.btnContinue.setOnClickListener {
                if (validDetails()) {
                    dialog.dismiss()
                    val savingsAccount =
                        SavingsAccount(savingsTitle, accountType, "0.00", amount, "date")
                    roomDBViewModel.insertSavingsAccount(savingsAccount)
                    showSuccessSnackBar("Savings Account created Succesfully")
                    findNavController().navigate(R.id.savingsFragment)
                }

            }


        }
    */
/*
    private fun showDialog() {
        //var requestingFragment = requestingFragment2




        var selectedOption = Constants.MARI_WALLET

        confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner, {
            selectedOption = it
        })
        */
/*confirmSendingMoneyViewModel.requestingFragment.observe(viewLifecycleOwner, {
            requestingFragment = it
        })*//*



        val bottomsheetlayoutBinding = PaymentsModeBottomsheetBinding.inflate(layoutInflater)
        bottomsheetlayoutBinding.tvSelectSourceofFunds.text = getString(R.string.pay_from)
        bottomsheetlayoutBinding.cardViewWallet.makeVisible()

        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        dialog.setContentView(bottomsheetlayoutBinding.root)
        dialog.show()


        bottomsheetlayoutBinding.cardViewWallet.setOnClickListener {
            confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)

            makeActive(
                bottomsheetlayoutBinding.tvOptionMariWallet,bottomsheetlayoutBinding.walletcardview,
                bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,bottomsheetlayoutBinding.debitcardview,
                bottomsheetlayoutBinding.tvOptionEthiocash,bottomsheetlayoutBinding.ethiocashview,
                bottomsheetlayoutBinding.tvOptionMpesaCash,bottomsheetlayoutBinding.mpesaview,
                bottomsheetlayoutBinding.tvOptionMpesaCash,bottomsheetlayoutBinding.mpesaview,
                bottomsheetlayoutBinding.tvOptionMpesaCash,bottomsheetlayoutBinding.mpesaview,
            )

        }
        bottomsheetlayoutBinding.cardViewWallet.setOnClickListener {
            */
/*confirmSendingMoneyViewModel.setPaymentOption(Constants.MOBILE_MONEY)

            makeActive(
                bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
                bottomsheetlayoutBinding.tvOptionMariWallet,
                bottomsheetlayoutBinding.debitcardview,
                bottomsheetlayoutBinding.walletcardview,

                )*//*

            toastMessage("Feature Coming Soon")
        }
        bottomsheetlayoutBinding.cardviewDebitorCredit.setOnClickListener {
            */
/* confirmSendingMoneyViewModel.setPaymentOption(Constants.DEBIT_OR_CREDIT)

             makeActive(
                 bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
                 bottomsheetlayoutBinding.tvOptionMariWallet,
                 bottomsheetlayoutBinding.debitcardview,
                 bottomsheetlayoutBinding.walletcardview,

                 )*//*

            toastMessage("Feature Coming Soon")

        }

        bottomsheetlayoutBinding.btnContinue.setOnClickListener {
            dialog.dismiss()
            //toastMessage(" $selectedOption, $requestingFragment")

            if (selectedOption.isNotEmpty() && selectedOption == Constants.MARI_WALLET) {
                // confirmSendingMoneyViewModel.setPaymentOption(requestingFragment)
*/
/*
            when (requestingFragment) {
                Constants.SEND_TO_WALLET -> {
                    findNavController().navigate(R.id.sendMoneyConfirmationFragment)
                }
                Constants.WITHDRAW_FRAGMENT -> {
                    findNavController().navigate(R.id.withdrawConfirmationFragment)
                }
                else -> {
                    findNavController()
                        .navigate(R.id.billPaymentsConfirmationFragment)

                }
            }
*//*

                findNavController().navigate(R.id.authorizeTransactionFragment)


            } else if (selectedOption.isNotEmpty() && selectedOption == Constants.DEBIT_OR_CREDIT) {
                // confirmSendingMoneyViewModel.setRequestingFragment(requestingFragment)
                toastMessage("Feature Coming soon")
                */
/*findNavController()
                    .navigate(R.id.enterCardFragment)*//*


            }

        }
    }
*/

    private fun validDetails(): Boolean {
        amount = binding.etTargetAmount.text.toString().trim()
        depositAmount = binding.etDepositamount.text.toString().trim()
        savingsTitle = binding.etSavingsTitle.text.toString().trim()
        date = binding.etCalender.text.toString().trim()
        if (savingsTitle.isNullOrBlank()) {
            binding.tilSavingstitle.error = "Please enter a Title"
            return false
        } else if (depositAmount.isNullOrBlank()) {
            binding.etDepositamount.error = "Please enter a valid Amount"
            binding.etCalender.requestFocus()
            return false

        }
        else if (depositAmount.toInt()<100) {
            binding.etDepositamount.error = "Minimum Deposit Amount is GHS 100"
            binding.etCalender.requestFocus()
            return false
        }
        else if (!isInsufficientFunds(depositAmount.toInt(),requireContext())) {
            binding.etDepositamount.error = "Insufficient Account Balance"
            binding.etCalender.requestFocus()
            return false
        } else if (date.isNullOrBlank()) {
            binding.etCalender.error = "Please enter a valid Date"
            binding.etCalender.requestFocus()
            return false
        } else if (amount.isNullOrBlank()) {
            binding.tilTargetAmount.error = "Please enter a valid Amount"
            binding.tilTargetAmount.requestFocus()
            return false
        } else {
            SharedPreferencesManager.setAmount(requireContext(), amount)
            SharedPreferencesManager.setDepositAmount(requireContext(), depositAmount)
            confirmSendingMoneyViewModel.setSavingsPurpose(savingsTitle)

        }

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }


}