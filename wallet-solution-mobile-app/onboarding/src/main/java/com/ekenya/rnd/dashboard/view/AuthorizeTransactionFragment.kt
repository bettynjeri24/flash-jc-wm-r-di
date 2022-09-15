package com.ekenya.rnd.dashboard.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.data.model.BuyAirtimeReq
import com.ekenya.rnd.common.data.model.BuyAirtimeReqWrapper
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.PaymentsPayload
import com.ekenya.rnd.dashboard.datadashboard.model.SavingsPayload
import com.ekenya.rnd.dashboard.datadashboard.model.SendMoneyToAnotherWalletRequest
import com.ekenya.rnd.dashboard.datadashboard.model.TopUpRequest
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.*
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentAuthorizeTransactionBinding

class AuthorizeTransactionFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizeTransactionBinding
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var transactionViewModel: TransactionConfirmationViewModel
    private lateinit var selectedOption: String
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private lateinit var requestingFragment: String
    private var accountNumber: String = ""
    private lateinit var savings_purpose: String
    private lateinit var customer_AccountNumber: String
    private lateinit var token: String
    private lateinit var metreNumber: String
    private lateinit var accountName: String
    private var amount: Int = 0
    private lateinit var buyAirtimeOption: BuyAirtimeReq
    private lateinit var pin: String
    private var one1: String? = null
    private var isDone = false
    private var two2: String? = null
    private var three3: String? = null
    private var four4: String? = null
    private var mConfirmPin: String? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAuthorizeTransactionBinding.inflate(layoutInflater, container, false)


        try
        {
            initToken()
            initViewModel()
            setObservers()
            setClickListeners()

        } catch (e: Exception) {
            Log.d("TAG", "onCreateView Authorize: ${e.localizedMessage}")
        }

        return binding.root
    }

    private fun initToken() {
        token = SharedPreferencesManager.getnewToken(requireContext())!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setClickListeners() {
        KeyPadListeners()

    }

    private fun initViewModel() {

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)

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

        mobileWalletViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(MobileWalletViewModel::class.java)

        authorizeTransactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)
    }

    private fun setObservers() {

        transactionViewModel.paymentOption.observe(viewLifecycleOwner, {
            // binding.senderName.text = it
            selectedOption = it

        })

        authorizeTransactionViewModel.requestingFragment.observe(viewLifecycleOwner, {
            requestingFragment = it

        })
        confirmSendingMoneyViewModel.buyAirtimeReq.observe(viewLifecycleOwner, {

            buyAirtimeOption = it
        })


        confirmSendingMoneyViewModel.dstvAccountNumber.observe(viewLifecycleOwner, {
            accountNumber = it
        })
        confirmSendingMoneyViewModel.savings_purpose.observe(viewLifecycleOwner, {
            savings_purpose = it
        })
        confirmSendingMoneyViewModel.receivingAccountNumber.observe(viewLifecycleOwner, {
            customer_AccountNumber = it
        })
        confirmSendingMoneyViewModel.botswanaPMetreNumber.observe(viewLifecycleOwner, {
            metreNumber = it
        })
        confirmSendingMoneyViewModel.amount.observe(viewLifecycleOwner, {
            amount = it
        })
        confirmSendingMoneyViewModel.accountName.observe(viewLifecycleOwner, {
            accountName = it
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showSupportActionBar()
        makeStatusBarWhite()

    }

    private fun checkRequestingFragment() {

        when (requestingFragment) {
            Constants.SEND_TO_WALLET -> {
                try {
                    processSendMoney(customer_AccountNumber)
                } catch (e: Exception) {
                    toastMessage("Some Error Occured")
                }

            }
            Constants.EXPORT_STATEMENTS -> {
                try {
                    //toastMessage("Get statements was called")
                    getStatementsViaEmail()
                } catch (e: Exception) {
                    toastMessage("Some Error Occured")
                }

            }
            Constants.TOP_WALLET_FRAGMENT -> {
                try {
                    if (buyAirtimeOption.service_name == getString(R.string.safaricom_mpesa)) {
                        doStKPush()
                    }
                    else
                    {
                        doBuyAirtimeRequest(BuyAirtimeReqWrapper(buyAirtimeOption))
                    }
                } catch (e: Exception) {
                    toastMessage("Some Error Occured ${e.toString()}")
                }

            }
            Constants.DO_CARD_PAYMENTS -> {
                try {
                    // doStKPush()
                    doCardPayments("")
                } catch (e: Exception) {
                    toastMessage("Some Error Occured")
                }

            }
            Constants.MASCOM_AIRTIME_FRAGMENT -> {
                try {
                    doMockPayments()
                    //buyMascomAirTime()
                } catch (e: Exception) {
                    toastMessage("Some Error Occured")
                }
            }
            Constants.WITHDRAW_FRAGMENT -> {
                try {
                    doMockPayments()
                    //withdrawMoney()
                } catch (e: Exception) {
                    toastMessage("Some Error Occured")
                }
            }
            Constants.BUY_AIRTIME_FRAGMENT -> {
                try {
                    doBuyAirtimeRequest(BuyAirtimeReqWrapper(buyAirtimeOption))
                } catch (e: Exception) {
                    toastMessage("Some Error Occured")
                }
            }
            Constants.DSTV_PAYMENTS_FRAGMENT -> {
                try {
                    doDstvPayments()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                    Log.d("TAG", "checkRequestingFragment: error with ${e.localizedMessage}")

                }
            }

            Constants.PAY_MERCHANT_FRAGMENT -> {
                try {
                    payMerchant()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                }
            }
            Constants.ADD_SAVINGS_FRAGMENT -> {
                try {
                    createSavingsAccount()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                }
            }
            Constants.BOTSWANA_POWER_FRAGMENT -> {
                try {
                    doMockPayments()
                    //doBotSwanaPowerPayments()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                }
            }
            Constants.SEND_TO_BANK -> {
                try {
                    doMockPayments()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                }
            }
            Constants.SEND_TO_MOBILE -> {
                try {
                    doMockPayments()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                }
            }
            Constants.TICKETING_FRAGMENT -> {
                try {
                    doMockPayments()
                } catch (e: Exception) {
                    showErrorSnackBar("${e.localizedMessage}")
                }
            }

            else -> {
                toastMessage("doing else mock payments")
                doMockPayments()
            }
        }


    }

    private fun doBuyAirtimeRequest(buyAirtimeReq:BuyAirtimeReqWrapper)
    {
        mobileWalletViewModel.buyAirtimeReq(token,buyAirtimeReq)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()

                            val responseCode = it.data?.data?.response?.response_code
                            when (responseCode) {
                                "401" -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                    findNavController().navigate(R.id.loginFragment2)
                                }
                                "00" -> {
                                    showSuccessDialog()
                                }
                                "000" -> {
                                    showSuccessDialog()
                                }
                                else -> {
                                    showSuccessDialog()
                                    /* showTransactionErrorDialog(
                                         "Some Error Occured",
                                         "Try Again Later"
                                     )*/
                                }
                            }

                        }
                        Status.ERROR -> {
                            showSuccessDialog()
                            // showTransactionErrorDialog(it.message!!)

                            // dialog.dismiss()
                            // it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {
                            binding.avi.visibility = View.VISIBLE
                            //binding.avi.

                            // requireacgetwind.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })
    }

    private fun doMockPayments() {
        showSuccessDialog()
    }


    private fun doBotSwanaPowerPayments() {


        val paymentsPayload = PaymentsPayload(
            "BpcPayment", amount, 1,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            "MM", "1111", "234", "", "",
            SharedPreferencesManager.getAccountNumber(requireContext())!!,
            "Home", "android", "9.1", metreNumber, metreNumber,
            "xxxxx", false
        )
        //check on paymentsPayload
        mobileWalletViewModel.payBotswanaPower(token, MainDataObject(paymentsPayload))
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                    findNavController().navigate(R.id.loginFragment2)
                                }
                                "00" -> {
                                    showSuccessDialog()
                                }
                                "000" -> {
                                    showSuccessDialog()
                                }
                                else -> {
                                    showSuccessDialog()
                                    /* showTransactionErrorDialog(
                                         "Some Error Occured",
                                         "Try Again Later"
                                     )*/
                                }
                            }

                        }
                        Status.ERROR -> {
                            showSuccessDialog()
                            // showTransactionErrorDialog(it.message!!)

                            // dialog.dismiss()
                            // it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {
                            binding.avi.visibility = View.VISIBLE
                            //binding.avi.

                            // requireacgetwind.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })
    }

    private fun doDstvPayments() {


        val paymentsPayload = PaymentsPayload(
            "DstvPayment",
            amount,
            1,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            "MM", "1111", "234", "", "",
            SharedPreferencesManager.getAccountNumber(requireContext())!!,
            "Home", "android", "9.1", accountNumber, accountNumber,
            accountName, false

        )


        mobileWalletViewModel.payBotswanaPower(token!!, MainDataObject(paymentsPayload))
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {
                                    findNavController().navigate(R.id.loginFragment2)


                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                }
                                "00" -> {
                                    showSuccessDialog()
                                }
                                "000" -> {
                                    showSuccessDialog()
                                }
                                else -> {
                                    showTransactionErrorDialog(
                                        "Some Error Occured",
                                        "Try Again Later"
                                    )
                                }
                            }


                            //findNavController().popBackStack()
                        }
                        Status.ERROR -> {
                            binding.avi.visibility = View.GONE
                            it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {

                            binding.avi.visibility = View.VISIBLE

                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })


    }

    private fun payMerchant() {


        val paymentsPayload = PaymentsPayload(
            "MerchantPayment",
            amount,
            1,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            "MM", "1111", "234", "", "",
            SharedPreferencesManager.getAccountNumber(requireContext())!!,
            "Home", "android", "9.1", accountNumber, accountNumber,
            "", false, SharedPreferencesManager.getMerchantCode(requireContext())!!, 1

        )


        mobileWalletViewModel.payBotswanaPower(
            token!!,
            MainDataObject(paymentsPayload, "466043953434")
        )
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                }
                                "00" -> {
                                    showSuccessDialog()
                                }
                                "000" -> {
                                    showSuccessDialog()
                                }
                                "01" -> {
                                    showTransactionErrorDialog(
                                        "${it.data?.data?.response?.response_message}",
                                        "Try Again Later"
                                    )
                                }
                                else -> {
                                    showTransactionErrorDialog(
                                        "Some Error Occured",
                                        "Try Again Later"
                                    )
                                }
                            }


                            //findNavController().popBackStack()
                        }
                        Status.ERROR -> {
                            binding.avi.visibility = View.GONE
                            it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {

                            binding.avi.visibility = View.VISIBLE

                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })


    }

    private fun createSavingsAccount() {

        val savingsPayload = SavingsPayload(
            "OpenSavingsAccount",
            SharedPreferencesManager.getdepositAmount(requireContext())!!.toInt(),
            SharedPreferencesManager.getAmouttoTopUP(requireContext())!!.toInt(),
            savings_purpose,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            debit_account = SharedPreferencesManager.getAccountNumber(requireContext())!!,
            transaction_code = "OPEN_ACCOUNT"


        )


        mobileWalletViewModel.payBotswanaPower(token!!, MainDataObject(savingsPayload))
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                    findNavController().navigate(R.id.loginFragment2)

                                }
                                "00" -> {
                                    showSuccessDialog()
                                }
                                "000" -> {
                                    showSuccessDialog()
                                }
                                else -> {
                                    showTransactionErrorDialog(
                                        "Some Error Occured",
                                        "Try Again Later"
                                    )
                                }
                            }


                            //findNavController().popBackStack()
                        }
                        Status.ERROR -> {
                            binding.avi.visibility = View.GONE
                            it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {

                            binding.avi.visibility = View.VISIBLE

                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })
    }

    private fun withdrawMoney() {


        val paymentsPayload = PaymentsPayload(
            "WithdrawMoney",
            amount,
            1,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            "MM",
            SharedPreferencesManager.getAccountNumber(requireContext())!!,
            "", "", "",
            SharedPreferencesManager.getAccountNumber(requireContext())!!,
            "Home", "android", "9.1", "", "",
            "xxxxx", false

        )
        mobileWalletViewModel.withDrawMoneytoMobileMoney(token, MainDataObject(paymentsPayload))
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()

                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "00" -> {
                                    showSuccessDialog()

                                }
                                "401" -> {

                                }


                            }
                            if (responseCode.equals("00")) {


                            } else {
                                showTransactionErrorDialog(it.data?.data?.response?.response_message!!)


                            }
                        }
                        Status.ERROR -> {
                            // dialog.dismiss()
                            //toastMessage(it.data!!)
                            showTransactionErrorDialog(it.message!!)
                            //it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {
                            binding.avi.visibility = View.VISIBLE
                            //binding.avi.

                            // requireacgetwind.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })

    }

    private fun getStatementsViaEmail() {
        val exportStatementPayload = MainDataObject(
            TopUpRequest(
                "FullStatement",
                0,
                1,
                "2",
                SharedPreferencesManager.getPhoneNumber(requireContext())!!,
                "",
                false,
                "",
                "",
                "Home",
                "android",
                "10.1",
                "",
                SharedPreferencesManager.getemailaddress(requireContext())!!,
                SharedPreferencesManager.getAccountNumber(requireContext())!!
            )
        )
        mobileWalletViewModel.getFullStatementViaEmail(token, exportStatementPayload)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                    findNavController().navigate(R.id.loginFragment2)

                                }
                                "00" -> {

                                    showEmailConfirmationDialog()
                                    // showSuccessDialog()
                                }
                                "000" -> {
                                    showEmailConfirmationDialog()
                                }
                                else -> {
                                    showTransactionErrorDialog(
                                        "Some Error Occured",
                                        "Try Again Later"
                                    )
                                }
                            }

                        }
                        Status.ERROR -> {
                            showTransactionErrorDialog(it.message!!)

                            // dialog.dismiss()
                            // it.data?.data?.response?.let { it1 -> toastMessage(it1.response_code) }

                        }
                        Status.LOADING -> {
                            binding.avi.visibility = View.VISIBLE
                            //binding.avi.

                            // requireacgetwind.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


                            binding.pin1.visibility = View.INVISIBLE
                            binding.pin2.visibility = View.INVISIBLE
                            binding.pin3.visibility = View.INVISIBLE
                            binding.pin4.visibility = View.INVISIBLE

                        }
                    }
                }
            })
    }

    private fun showEmailConfirmationDialog() {

        val confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)


        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.succesful_transaction_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
        val btnText = dialog.findViewById<TextView>(R.id.tv_phoneConfirmation)
        btnText.text = "A notification will be sent via Email\nOnce it is processed"


        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.navigation_home, false)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack(R.id.navigation_home, false)

        }
        setDialogLayoutParams(dialog)
        dialog.show()


    }


    @RequiresApi(Build.VERSION_CODES.M)
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


        val name = SharedPreferencesManager.getFirstName(requireContext())

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun controlPinPad2(entry: String) {
        binding.apply {
            if (one1 == null) {
                binding.pin1.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                one1 = entry
            } else if (two2 == null) {
                binding.pin2.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                two2 = entry
            } else if (three3 == null) {
                binding.pin3.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                three3 = entry
            } else if (four4 == null) {
                binding.pin4.background = context?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.activestepsbackground
                    )
                };
                four4 = entry
                isDone = true
            }

            if (isDone) {
                if (validDetails()) {
                    if (isConnectedToInternet()) {
                        pin = one1 + two2 + three3 + four4
                        SharedPreferencesManager.setPin(requireContext(), pin)
                        //check for login

                        Log.d("TAG", "controlPinPad2: reached here")
                        sendLoginRequest()


                    } else {
                        showErrorSnackBar("Check your Internet Connectivity")
                        //findNavController().navigate(R.id.action_loginFragment2_to_landingPageFragment)

                    }
                }
            }

        }
    }


    private fun deletePinEntry() {
        binding.apply {
            if (mConfirmPin != null && mConfirmPin!!.length > 0) {
                mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
            }
            if (four4 != null) {
                binding.pin4.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                four4 = null
                isDone = false

            } else if (three3 != null) {
                binding.pin3.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                three3 = null
            } else if (two2 != null) {
                binding.pin2.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                two2 = null
            } else if (one1 != null) {
                binding.pin1.background = resources.getDrawable(R.drawable.inactive_pin_bg)
                one1 = null
            }
        }

    }

    private fun validDetails(): Boolean {
        return true
    }


    private fun processSendMoney(accountNumber: String) {

        val data = MainDataObject(
            SendMoneyToAnotherWalletRequest(
                "SendMoney",
                amount,
                1,
                SharedPreferencesManager.getPhoneNumber(requireContext())!!,
                SharedPreferencesManager.getPhoneNumber(requireContext())!!,
                false,
                "",
                accountNumber,
                SharedPreferencesManager.getAccountNumber(requireContext())!!,
                "Home",
                "android",
                "5.1",


                )
        )
        val token = context?.let { SharedPreferencesManager.getnewToken(it) }

        Log.d("TAG", "setupObservers: token I am using is $token")
        mobileWalletViewModel.topUpWallet(token!!, data).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.avi.makeInvisible()

                        val responseCode = it.data?.data?.response?.responseCode
                        if (responseCode.equals("00")) {

                            //findNavController().popBackStack(R.id.billPaymentsFragment,false)

                            showSuccessDialog()

                        } else {
                            showTransactionErrorDialog(it.data?.data?.response?.responseMessage!!)

                            // it.data?.data?.response?.response_message?.let { it1 -> toastMessage(it1) }


                        }
                    }


                    Status.ERROR -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                    }
                    Status.LOADING -> {
                        binding.avi.visibility = View.VISIBLE


                        binding.pin1.visibility = View.INVISIBLE
                        binding.pin2.visibility = View.INVISIBLE
                        binding.pin3.visibility = View.INVISIBLE
                        binding.pin4.visibility = View.INVISIBLE
                    }
                }
            }
        })
    }

    fun showTransactionErrorDialog(errorTitle: String, errorMessage: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.transaction_failed_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
        val tvErrorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
        val tvErrorTitle = dialog.findViewById<TextView>(R.id.tv_sorry)
        var title = errorTitle

        tvErrorMessage.text = errorMessage
        tvErrorTitle.text = errorTitle


        when (errorTitle) {
            "Session Expired" -> {
                btnAlldone.text = getString(R.string.login)

            }

        }
        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            if (errorTitle.equals("Invalid Pin")) {
                findNavController().navigate(R.id.authorizeTransactionFragment)
            } else {

                navigateTo()


            }
        }


        btnAlldone.setOnClickListener {

            dialog.dismiss()
            when (errorTitle) {
                "Session Expired" -> {
                    findNavController().navigate(R.id.landingPageFragment)

                }
                "Invalid Pin" -> {
                    findNavController().navigate(R.id.authorizeTransactionFragment)

                }
                else -> {
                    navigateTo()

                    // }

                }


            }
        }
        dialog.show()
        setDialogLayoutParams(dialog)
    }
    private fun resetPin() {
        one1 = null
        isDone = false
        two2 = null
        three3 = null
        four4 = null
        mConfirmPin = null


        binding.pin4.background = resources.getDrawable(R.drawable.inactive_pin_bg)
        binding.pin3.background = resources.getDrawable(R.drawable.inactive_pin_bg)
        binding.pin2.background = resources.getDrawable(R.drawable.inactive_pin_bg)
        binding.pin1.background = resources.getDrawable(R.drawable.inactive_pin_bg)

        binding.pin1.makeVisible()
        binding.pin2.makeVisible()
        binding.pin3.makeVisible()
        binding.pin4.makeVisible()


    }

    private fun navigateTo() {

        when (requestingFragment) {
            Constants.BOTSWANA_POWER_FRAGMENT -> {
                findNavController().popBackStack(R.id.botswanaPowerFragment, true)
            }
            Constants.PAY_MERCHANT_FRAGMENT -> {
                findNavController().popBackStack(R.id.payMerchantFragment2, true)
            }
            Constants.WITHDRAW_FRAGMENT -> {
                findNavController().popBackStack(R.id.withdrawFragment2, true)
            }
            Constants.DSTV_PAYMENTS_FRAGMENT -> {
                findNavController().popBackStack(R.id.dstvPayments, true)


            }
            Constants.MASCOM_AIRTIME_FRAGMENT -> {
                findNavController().popBackStack(R.id.mascomAirtimeFragment, true)


            }
            Constants.BUY_AIRTIME_FRAGMENT -> {
                findNavController().popBackStack(R.id.orangeAirtimeFragment, true)


            }
            else -> {
                //if (title.contains("Daily Limit will be exceeded", ignoreCase = true)){
                findNavController().popBackStack(R.id.topUpWalletFragment2, true)


                //}

            }
        }
    }

    private fun sendLoginRequest() {

        val loginData = MainDataObject(DashBoardUtils.getUserLoginData(context))
        loginViewModel.login(loginData).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.avi.visibility = View.GONE

                        val responsecode = it.data?.data?.response?.response_code
                        // toastMessage(responsecode)


                        when (responsecode) {
                            "00" -> {

                                val token = it.data?.data?.transaction_details?.access_token
                                val accountNumber =
                                    it.data?.data?.transaction_details?.user?.accounts?.get(0)?.account_number
                                val names =
                                    it.data!!.data.transaction_details.user.accounts!![0].customer_names.split(
                                        "\\s".toRegex()
                                    )
                                if (names.size == 3) {

                                    context?.let { it1 ->
                                        SharedPreferencesManager.setFirstName(
                                            it1,
                                            names[0].replaceFirstChar { it.uppercase() }
                                        )
                                    }
                                    context?.let { it1 ->
                                        SharedPreferencesManager.setMiddleName(
                                            it1,
                                            names[1].replaceFirstChar { it.uppercase() }
                                        )
                                    }
                                    context?.let { it1 ->
                                        SharedPreferencesManager.setLastName(
                                            it1,
                                            names[2].replaceFirstChar { it.uppercase() }
                                        )
                                    }
                                } else if (names.size == 2) {
                                    context?.let { it1 ->
                                        SharedPreferencesManager.setFirstName(
                                            it1,
                                            names[0].replaceFirstChar { it.uppercase() }
                                        )
                                    }
                                    context?.let { it1 ->
                                        SharedPreferencesManager.setLastName(
                                            it1,
                                            names[1].replaceFirstChar { it.uppercase() }
                                        )
                                    }

                                }
                                if (token!!.isNotBlank()) {

                                    SharedPreferencesManager.setnewToken(requireContext(), token)
                                    SharedPreferencesManager.setAccountNumber(
                                        requireContext(),
                                        accountNumber
                                    )
                                }

                                initToken()
                                checkRequestingFragment()
                            }
                            "403" -> {
                                resetPin()

                                showTransactionErrorDialog(
                                    "Invalid PIN",
                                    "Try Again"
                                )
                            }
                            "400" -> {
                                resetPin()
                                showTransactionErrorDialog(
                                    it.data!!.data.response.response_message,
                                    "Try Again Later"
                                )
                            }
                            else -> {
                                resetPin()
                                findNavController().navigate(R.id.landingPageFragment)
                                toastMessage(it.data!!.data.response.response_message)


                            }
                        }

                        /* if (responsecode.equals("00")) {


                         } else {
                             toastMessage(it.data!!.data.response.response_message)
                             findNavController().navigate(R.id.action_loginFragment2_to_landingPageFragment)

                         }*/
                    }
                    Status.ERROR -> {
                        binding.avi.visibility = View.GONE


                        showTransactionErrorDialog("Pin Verification Failed", "Try again")
                        //findNavController().navigate(R.id.navigation_home)


                    }
                    Status.LOADING -> {
                        binding.avi.visibility = View.VISIBLE



                        binding.pin1.visibility = View.INVISIBLE
                        binding.pin2.visibility = View.INVISIBLE
                        binding.pin3.visibility = View.INVISIBLE
                        binding.pin4.visibility = View.INVISIBLE


                    }
                }
            }
        })
    }

    private fun doStKPush() {


        val token = context?.let { SharedPreferencesManager.getnewToken(it) }


        val topUpData = MainDataObject(DashBoardUtils.topUpData(context))

        mobileWalletViewModel.topUpWallet(token!!, topUpData)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {

                        Status.SUCCESS -> {
                            binding.avi.makeInvisible()

                            if (it.data?.data?.response?.responseCode.equals("000")) {
                                Toast.makeText(
                                    context,
                                    "Request Sent wait for STK Push",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().navigate(R.id.navigation_home)
                            } else {
                                showTransactionErrorDialog(
                                    it.data!!.data.response.responseMessage,
                                    "Try again Later"
                                )


                            }
                        }
                        Status.ERROR -> {
                            binding.avi.makeInvisible()

                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                        }
                        Status.LOADING -> {
                            binding.avi.makeVisible()


                        }
                    }
                }
            })
    }

    private fun doCardPayments(stage: String) {

        val token = context?.let { SharedPreferencesManager.getnewToken(it) }


        val topUpData = MainDataObject(DashBoardUtils.topUpWalletFromCard(requireContext(), ""))

        mobileWalletViewModel.topUpWallet(token!!, topUpData)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {


                        Status.SUCCESS -> {
                            val responseCode = it.data?.data?.response?.responseCode
                            binding.avi.makeInvisible()

                            when (responseCode) {
                                "000" -> {
                                    showSuccessDialog()

                                }
                                "01" -> {
                                    //toastMessage("invalid expiry date")
                                    showTransactionErrorDialog(
                                        it.data?.data?.response?.responseMessage!!,
                                        "Recheck your Card Details"
                                    )
                                }
                                "401" -> {
                                    findNavController().navigate(R.id.loginFragment2)

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                }
                                "59" -> {
                                    showTransactionErrorDialog(
                                        it.data?.data?.response?.responseMessage!!,
                                        "Recheck Top Up Amount"
                                    )
                                }
                                else -> {
                                    showTransactionErrorDialog(
                                        it.data?.data?.response?.responseMessage!!,
                                        "Try Again"
                                    )
                                }
                            }

                            /* if () {
                                 showSuccessDialog()
                             } else {
                                 showErrorSnackBar(it.data!!.data.response.responseMessage)


                             }*/
                        }
                        Status.ERROR -> {

                            showTransactionErrorDialog(
                                "Oops Sorry",
                                "Service Unavailable Try\n Again Later"
                            )
                            binding.avi.makeInvisible()


                        }
                        Status.LOADING -> {
                            binding.avi.makeVisible()


                        }
                    }
                }
            })
    }


}