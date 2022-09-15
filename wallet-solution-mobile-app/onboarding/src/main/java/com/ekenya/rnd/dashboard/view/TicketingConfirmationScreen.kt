package com.ekenya.rnd.dashboard.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.model.PaymentsPayload
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentTicketingConfirmationScreenBinding


class TicketingConfirmationScreen : Fragment() {

    private lateinit var binding: FragmentTicketingConfirmationScreenBinding
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var selectedOption: String
    private lateinit var requestingFragment: String
    private lateinit var accountNumber: String
    private lateinit var token: String
    private lateinit var accountName: String
    private lateinit var metreNumber: String
    private var amount: Int = 0
    private var charge: Double = 0.0

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
        binding = FragmentTicketingConfirmationScreenBinding.inflate(inflater, container, false)

        token = SharedPreferencesManager.getnewToken(requireContext())!!

        initViewModel()

        initObservers()
        //Initialize view Model and based on observers set layout
        initUi()



        initClickListeners()


        return binding.root
    }

    private fun initClickListeners() {

        binding.btnPaynow.setOnClickListener {


            authorizeTransaction()

        }
    }

    private fun authorizeTransaction() {
        // authorizeTransactionViewModel.setRequestingFragment()
        findNavController().navigate(R.id.authorizeTransactionFragment)
    }


    private fun initObservers() {
        confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner, {
            Log.d(TAG, "initObservers: paymentoption $it")
            binding.tvPayFromValue.text = it


            selectedOption = it

        })
       /* confirmSendingMoneyViewModel.dstvAccountNumber.observe(viewLifecycleOwner, {
            binding.tvAccountNumberValue.text = it
            accountNumber = it
        })*/
       /* confirmSendingMoneyViewModel.accountName.observe(viewLifecycleOwner, {

            binding.tvAccountNameValue.text = it
            accountName = it
        })
        confirmSendingMoneyViewModel.botswanaPMetreNumber.observe(viewLifecycleOwner, {
            metreNumber = it
        })*/

        /*confirmSendingMoneyViewModel.botswanaPMetreNumber.observe(viewLifecycleOwner, {
            binding.tvAccountNumberValue.text = it
        })*/
/*
        confirmSendingMoneyViewModel.requestingFragment.observe(viewLifecycleOwner, {
            binding.tvAgentNameValue.text = it
            when (it) {
                Constants.BOTSWANA_POWER_FRAGMENT -> {
                    binding.tvAgentNameValue.text = getString(R.string.botswana_power)
                    binding.tvTitle.text = getString(R.string.botswana_power)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        0,
                        0
                    );
                    authorizeTransactionViewModel.setRequestingFragment(Constants.BOTSWANA_POWER_FRAGMENT)
                    //To Get Transaction Cost
                    doBotSwanaPowerPayments()
                }
                Constants.DSTV_PAYMENTS_FRAGMENT -> {
                    authorizeTransactionViewModel.setRequestingFragment(Constants.DSTV_PAYMENTS_FRAGMENT)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_dstv,
                        0,
                        0,
                        0
                    );

                    binding.tvAgentNameValue.text = getString(R.string.dstv)
                    binding.tvTitle.text = getString(R.string.dstv)
                    doDstvPayments()
                }
                Constants.Orange_AIRTIME_FRAGMENT -> {
                    authorizeTransactionViewModel.setRequestingFragment(Constants.Orange_AIRTIME_FRAGMENT)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.safaricom_small,
                        0,
                        0,
                        0
                    );

                    binding.tvAgentNameValue.text = getString(R.string.safaricom)
                    binding.tvTitle.text = getString(R.string.safaricom)
                }
                Constants.SEND_TO_BANK -> {
                    authorizeTransactionViewModel.setRequestingFragment(Constants.SEND_TO_BANK)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_send_money,
                        0,
                        0,
                        0
                    );

                    binding.tvAgentNameValue.text = ""
                    binding.tvTitle.text = "Send Money"
                }
                Constants.MASCOM_AIRTIME_FRAGMENT -> {
                    authorizeTransactionViewModel.setRequestingFragment(Constants.SEND_TO_MOBILE)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_mascomairtime,
                        0,
                        0,
                        0
                    );

                    binding.tvAgentNameValue.text = getString(R.string.mascom_airtime)
                    binding.tvTitle.text = getString(R.string.mascom_airtime)
                }
                Constants.SEND_TO_WALLET -> {
                    authorizeTransactionViewModel.setRequestingFragment(Constants.SEND_TO_WALLET)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_mascomairtime,
                        0,
                        0,
                        0
                    );

                    binding.tvAgentNameValue.text = getString(R.string.send_to_wallet)
                    binding.tvTitle.text = getString(R.string.send_to_wallet)
                }
                Constants.PAY_MERCHANT_FRAGMENT -> {
                    authorizeTransactionViewModel.setRequestingFragment(Constants.PAY_MERCHANT_FRAGMENT)
                    binding.tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_paymerchantic,
                        0,
                        0,
                        0
                    );

                    binding.tvAgentNameValue.text = getString(R.string.pay_merchant)
                    binding.tvTitle.text = getString(R.string.pay_merchant)
                }
                else -> {
                    binding.tvAgentNameValue.text = "Test Account"
                }


            }


        })
*/


    }

    private fun initViewModel() {

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
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(AuthorizeTransactionViewModel::class.java)

        mobileWalletViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        ).get(MobileWalletViewModel::class.java)
    }

    private fun initUi() {

        selectedOption = confirmSendingMoneyViewModel.paymentOption.value.toString()
        binding.sendingAmountValue.text =
            " GHS 1400.00"
        binding.totalAmount.text =
            " GHS 1400.00"
        binding.tvAccountNameValue.text= SharedPreferencesManager.getseats(requireContext())
        binding.tvAccountNumberValue.text= SharedPreferencesManager.getluggageValue(requireContext())


    }

    private fun doDstvPayments() {


        val paymentsPayload = PaymentsPayload(
            "DstvPayment",
            amount,
            1,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            "MM", "1111", "234", "pre", "",
            SharedPreferencesManager.getAccountNumber(requireContext())!!,
            "Home", "android", "9.1", accountNumber, accountNumber,
            accountName, false

        )



        mobileWalletViewModel.payBotswanaPower(
            token,
            MainDataObject(paymentsPayload)
        )
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {
                                    toastMessage("Session Expired Login to Continue")
                                    findNavController().navigate(R.id.loginFragment2)

                                }
                                "00" -> {
                                    charge =
                                        it.data?.data?.transaction_details?.charge_amount?.toDouble()!!

                                    binding.transactionCostAmount.text = "GHS $charge"
                                    binding.totalAmount.text = "GHS ${
                                        SharedPreferencesManager.getAmouttoTopUP(requireContext())
                                    }.00"


                                }
                                "000" -> {
                                }
                                else -> {


                                }
                            }


                            //findNavController().popBackStack()
                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })


    }

    private fun doBotSwanaPowerPayments() {

        val paymentsPayload = PaymentsPayload(
            "BpcPayment", amount, 1,
            SharedPreferencesManager.getPhoneNumber(requireContext())!!,
            "MM", "1111", "234", "pre", "",
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
                            val responseCode = it.data?.data?.response?.response_code

                            when (responseCode) {
                                "401" -> {

                                    toastMessage("Session Expired Login to Continue")
                                    findNavController().navigate(R.id.loginFragment2)


                                }
                                "00" -> {
                                    charge =
                                        it.data?.data?.transaction_details?.charge_amount?.toDouble()!!

                                    binding.transactionCostAmount.text = "GHS $charge"
                                    binding.totalAmount.text = "GHS ${
                                        SharedPreferencesManager.getAmouttoTopUP(requireContext())
                                    }.00"


                                }
                                "000" -> {
                                }
                                else -> {


                                }
                            }


                            //findNavController().popBackStack()
                        }
                        Status.ERROR -> {

                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })
    }


}