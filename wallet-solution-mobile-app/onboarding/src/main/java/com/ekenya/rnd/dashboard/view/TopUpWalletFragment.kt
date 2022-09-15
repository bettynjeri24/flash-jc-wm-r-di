package com.ekenya.rnd.dashboard.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.adapters.PaymentOptionsAdapter
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.model.BillItem
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.isConnectedToInternet
import com.ekenya.rnd.dashboard.utils.makeStatusBarWhite
import com.ekenya.rnd.dashboard.utils.setDialogLayoutParams
import com.ekenya.rnd.dashboard.utils.showSupportActionBar
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FundWalletFragmentBinding
import com.ekenya.rnd.onboarding.databinding.PaymentsModeBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class TopUpWalletFragment : BaseDaggerFragment() {
    private lateinit var transactionViewModel: TransactionConfirmationViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var binding: FundWalletFragmentBinding
    private var amount: Long = 0
    private var selectedOption = " "


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FundWalletFragmentBinding.inflate(inflater, container, false)

        initUi()
        setupViewModel()
        setClickListeners()
        initObservers()

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initUi() {

        binding.tvWalletBalance.text =
            "Current Wallet Balance ${SharedPreferencesManager.getAccountBalance(requireContext())!!}0"


    }


    private fun initObservers() {
        transactionViewModel.paymentOption.observe(viewLifecycleOwner, {
            selectedOption = it
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setClickListeners() {
        //KeyPadListeners()
        // binding.etAmount.setOnTouchListener(exitSoftKeyBoard);


        binding.btnContinue.setOnClickListener {

            if (isConnectedToInternet()) {
                if (validDetails()) {

                    /*  if (!isInsufficientFunds(amount.toInt(), requireContext())) {
                          binding.tvMinimumAmount.text = "Insufficient Account Balance"
                          binding.tvMinimumAmount.setTextColor(Color.parseColor("#d60f0f"))
                          binding.btnContinue.makeInvisible()
                          binding.etAmount.requestFocus()
                      } else {*/
                    //showDialog()
                    authorizeTransactionViewModel.setRequestingFragment(Constants.TOP_WALLET_FRAGMENT)
                    confirmSendingMoneyViewModel.setRequestingFragment(Constants.TOP_WALLET_FRAGMENT)
                    showpaymentOptionsDialog(requireContext())
                  //  showCardPaymentOptionsDialog(Constants.TOP_WALLET_FRAGMENT)
                    //}
                }
            }

        }

/*
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (amount < 100) {
                    binding.btnContinue.alpha = .5f;
                    binding.btnContinue.isClickable = false;
                    binding.etAmount.setTextColor(Color.parseColor("#f76710"))
                    binding.btnContinue.setBackgroundColor(Color.TRANSPARENT)

                    binding.tvMinimumAmount.setTextColor(Color.parseColor("#f76710"))
                    binding.tvMinimumAmount.makeVisible()
                    binding.tvMinimumAmount.setText(getString(R.string.minimum_amount_text))
                } else if (amount > 100000) {
                    binding.btnContinue.alpha = .5f;
                    binding.btnContinue.isClickable = false;
                    binding.etAmount.setTextColor(Color.parseColor("#d60f0f"))
                    binding.btnContinue.isEnabled = true
                    binding.btnContinue.setBackgroundColor(Color.BLUE)


                    binding.tvMinimumAmount.setTextColor(Color.parseColor("#d60f0f"))
                    binding.tvMinimumAmount.makeVisible()
                    binding.tvMinimumAmount.setText(getString(R.string.maximum_amount_text))
                } else if (amount in 100..100000) {
                    binding.btnContinue.makeVisible()
                    binding.btnContinue.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.bg_blue);
                    binding.btnContinue.alpha = 1f;

                    binding.btnContinue.isClickable = true
                    binding.btnContinue.isEnabled = true
                    binding.etAmount.setTextColor(Color.parseColor("#52d60f"))
                    binding.tvMinimumAmount.makeInvisible()
                    //binding.tvMinimumAmount.setText(getString(R.string.maximum_amount_text))
                } else {
                    binding.etAmount.setTextColor(Color.parseColor("#8d8d8d"))

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
*/

    }


    /*  @RequiresApi(Build.VERSION_CODES.M)
      private fun KeyPadListeners() {
          binding.apply {
              binding?.btnOne?.setOnClickListener { controlPinPad2("1") }
              binding?.btnTwo?.setOnClickListener { controlPinPad2("2") }
              binding?.btnThree?.setOnClickListener { controlPinPad2("3") }
              binding?.btnFour?.setOnClickListener { controlPinPad2("4") }
              binding?.btnFive?.setOnClickListener { controlPinPad2("5") }
              binding?.btnSix?.setOnClickListener { controlPinPad2("6") }
              binding?.btnSeven?.setOnClickListener { controlPinPad2("7") }
              binding?.btnEight?.setOnClickListener { controlPinPad2("8") }
              binding?.btnNine?.setOnClickListener { controlPinPad2("9") }
              binding?.btnZero?.setOnClickListener { controlPinPad2("0") }
              binding?.btnDelete?.setOnClickListener { deletePinEntry() }
          }

          *//*binding.tvForgotPin.setOnClickListener {
            findNavController().navigate(R.id.action_loginfragment_to_resetpin)
        }*//*
        val name = SharedPreferencesManager.getFirstName(requireContext())

        //  binding.tvGreetings.text = "${AppUtils.getTimeofTheDay()} $name!  \n"
    }

    private fun deletePinEntry() {
        binding.apply {


            if (rawAmount != null && rawAmount != "" && rawAmount!!.length > 0) {
                rawAmount = rawAmount.dropLast(1)
                if (!rawAmount.isEmpty()) {
                    amount = rawAmount.toLong()

                    binding.etAmount.setText("GHS " + amount)

                }
            } else if (rawAmount.isEmpty()) {
                binding.etAmount.setText("Enter Amount")
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun controlPinPad2(entry: String) {
        binding.apply {
            Log.d("TAG", "controlPinPad2: $entry")
            rawAmount += entry
            amount = rawAmount.toLong()
            binding.etAmount.setText("GHS " + amount)

        }
    }*/


    private fun setupViewModel() {

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)

        transactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(TransactionConfirmationViewModel::class.java)
        //transactionViewModel.setPaymentOption("Mobile Money")
        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
      //  confirmSendingMoneyViewModel.setPaymentOption("Debit Or Credit Card")


    }

    private fun validDetails(): Boolean {
        val etAmount = binding.etAmount.text.toString().trim()

        if (etAmount.isBlank()) {
            binding.etAmount.error = "Please enter a valid Amount"
            binding.etAmount.requestFocus()
            return false
        } else {
            amount = etAmount.toLong()
            if (amount > 1000000) {
                binding.etAmount.error = "Top Up limit is GHS 100,000"
                binding.etAmount.requestFocus()
                return false
            } else {
                SharedPreferencesManager.setAmount(requireContext(), amount.toString())


            }
        }
        return true
    }

/*
    private fun showDialog() {
        authorizeTransactionViewModel.setRequestingFragment(Constants.TOP_WALLET_FRAGMENT)

        val dialog = Dialog(requireContext())
        val bottomSheetLayoutBinding = FundWalletBottomsheetBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(bottomSheetLayoutBinding.root)
        bottomSheetLayoutBinding.cardViewMobileMoney.setOnClickListener {


            transactionViewModel.setPaymentOption("Mobile Money")
            val topUpData = MainDataObject(DashBoardUtils.topUpData(context))

            transactionViewModel.setMobileMoneyPaymentPayload(topUpData)

            makeActive(
                bottomsheetlayoutBinding.tvOptionMariWallet,bottomsheetlayoutBinding.walletcardview,
                bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,bottomsheetlayoutBinding.debitcardview,
                bottomsheetlayoutBinding.tvOptionEthiocash,bottomsheetlayoutBinding.ethiocashview,
                bottomsheetlayoutBinding.tvOptionMpesaCash,bottomsheetlayoutBinding.mpesaview,
            )


        }
        bottomSheetLayoutBinding.cardViewMobileMoneyEthiotel.setOnClickListener {


            transactionViewModel.setPaymentOption("Mobile Money")
            val topUpData = MainDataObject(DashBoardUtils.topUpData(context))

            transactionViewModel.setMobileMoneyPaymentPayload(topUpData)
            toastMessage("Feature Coming Soon")

           */
/* makeActive(
                bottomSheetLayoutBinding.tvOptionOne,
                bottomSheetLayoutBinding.tvOptionTwo,
                bottomSheetLayoutBinding.mobileMoneyview,
                bottomSheetLayoutBinding.optionTwoRadioButton
            )*//*



        }
        bottomSheetLayoutBinding.cardviewDebitorCredit.setOnClickListener {
            transactionViewModel.setPaymentOption(Constants.DEBIT_OR_CREDIT)

            makeActive(

                bottomSheetLayoutBinding.tvOptionTwo,
                bottomSheetLayoutBinding.tvOptionOne,
                bottomSheetLayoutBinding.optionTwoRadioButton,
                bottomSheetLayoutBinding.mobileMoneyview
            )


        }
        bottomSheetLayoutBinding.btnContinue.setOnClickListener {


            if (selectedOption.isNotEmpty() && selectedOption == "Mobile Money") {
                confirmSendingMoneyViewModel.setRequestingFragment("topupwallet")

                findNavController().navigate(R.id.transactionConfirmationFragment)

                //showTransactionErrorDialog("Feature Coming Soon")
                // findNavController().navigate(R.id.action_topUpWalletFragment2_to_transactionConfirmationFragment)

            } else if (selectedOption.isNotEmpty() && selectedOption == "Debit Or Credit Card") {
                confirmSendingMoneyViewModel.setRequestingFragment("topupwallet")
                findNavController().navigate(R.id.topUpWalletFragment2_to_enterCardDetails)

            }
            dialog.dismiss()


        }


        dialog.show()
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
*/


    fun showTransactionErrorDialog(error: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.transaction_failed_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
        val errorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
        val errorTitle = dialog.findViewById<TextView>(R.id.tv_sorry)

        errorMessage.text = "Would you like to try\nAnother payment option"
        errorTitle.text = error

        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()

        }
        setDialogLayoutParams(dialog)
        dialog.show()
    }

    private val clickItem: PaymentOptionsAdapter.MyClickListener = object :
        PaymentOptionsAdapter.MyClickListener {
        override fun onItemClick(position: Int) {
            Toast.makeText(requireContext(),position.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun showpaymentOptionsDialog(context: Context){
        val bottomsheetlayoutBinding = PaymentsModeBottomsheetBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        dialog.setContentView(bottomsheetlayoutBinding.root)
        val paymentOptionsAdapter = PaymentOptionsAdapter(context,parentFragment,dialog,clickItem)
        val paymentRecyclerView = bottomsheetlayoutBinding.rvPaymentOptions
        paymentRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        paymentOptionsAdapter.setDashBoardItems(getPaymentOptions())
        bottomsheetlayoutBinding.tvSelectSourceofFunds.text = getString(R.string.pay_from)

        dialog.show()
        paymentRecyclerView.adapter=paymentOptionsAdapter



    }
    fun getPaymentOptions(): List<BillItem> {

        val itemlist = ArrayList<BillItem>()


      /*  itemlist.add(
            BillItem(
                R.drawable.ethiopia_tel,
                getString(R.string.ethio_cash)
            )
        )*/
        itemlist.add(
            BillItem(
                R.drawable.telebir,
                "Telebirr"
            )
        )

        itemlist.add(
            BillItem(
                R.drawable.safaricom,
                "Safaricom Mpesa"
            )
        )
        itemlist.add(
            BillItem(
                R.drawable.ic_bank,
                getString(R.string.bank)
            )
        )




        itemlist.add(
            BillItem(
                R.drawable.ic_bank,
                "Debit or Credit Card"
            )
        )



        return itemlist


    }



}