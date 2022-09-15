package com.ekenya.rnd.dashboard.view

import android.app.Dialog
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
import androidx.annotation.RequiresApi
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
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.AuthorizeTransactionViewModel
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.dashboard.viewmodels.TransactionConfirmationViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentTransactionConfirmationBinding

class TransactionConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentTransactionConfirmationBinding
    private lateinit var mobileWalletViewModel: MobileWalletViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel

    private lateinit var transactionViewModel: TransactionConfirmationViewModel
    private lateinit var authorizeTransactionViewModel: AuthorizeTransactionViewModel

    private lateinit var selectedOption: String
    private lateinit var mobileMoneyPaymentPayload: MainDataObject


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
        binding = FragmentTransactionConfirmationBinding.inflate(inflater, container, false)
        initUi()
        initViewModel()
       // getTransactionCharges()
        initObservers()
        initClickListeners()
        confirmSendingMoneyViewModel.setPaymentOption(selectedOption)


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun initClickListeners() {

        binding.btnSendNow.setOnClickListener {
            findNavController().navigate(R.id.authorizeTransactionFragment)
            /*  if (isConnectedToInternet()) {
                  if (selectedOption.isNotEmpty() && selectedOption == "Mobile Money") {
                      try {
                          doStKPush()
                      } catch (e: Exception) {
                          Log.d("TAG", "initClickListeners: exception occured ${e.localizedMessage}")
                          showErrorSnackBar(" Some Error Occured")
                      }

                  } else if (selectedOption.isNotEmpty() && selectedOption == "Debit Or Credit Card") {

                      try {
                          doCardPayments()
                      } catch (e: Exception) {
                          showErrorSnackBar(" Some Error Occured")
                      }
                  }
              } else {
                  showErrorSnackBar("Kindly Check your Internet Connectivity")

              }*/
        }
    }


    private fun initObservers() {
        selectedOption = arguments?.getString("selectedOption").toString()
        toastMessage("this is transactions payment options $selectedOption")
        confirmSendingMoneyViewModel.setPaymentOption(selectedOption)
        transactionViewModel.setPaymentOption(selectedOption)


        transactionViewModel.paymentOption.observe(viewLifecycleOwner, {
            // binding.senderName.text = it
            selectedOption = it

        })
        transactionViewModel.mobileMoneyPaymentPayload.observe(viewLifecycleOwner, {
            mobileMoneyPaymentPayload = it
        })

    }

    private fun initViewModel() {
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

        transactionViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(TransactionConfirmationViewModel::class.java)

        mobileWalletViewModel = ViewModelProvider(
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
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)
    }

    private fun initUi() {
        binding.sendingAmountValue.text =
            " GHS ${SharedPreferencesManager.getAmouttoTopUP(requireContext())}.00"
        binding.totalAmount.text =
            " GHS ${SharedPreferencesManager.getAmouttoTopUP(requireContext())}.00"
        binding.sendingFromValue.text = /*blurCardNumber(requireContext())*/SharedPreferencesManager.getPhoneNumber(requireContext())


    }

    private fun getTransactionCharges() {
        doCardPayments(mobileWalletViewModel, "pre")
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {


                        Status.SUCCESS -> {
                            val responseCode = it.data?.data?.response?.responseCode

                            when (responseCode) {
                                "000" -> {
                                    // binding.transactionCostAmount.text =

                                }
                                "01" -> {
                                    //toastMessage("invalid expiry date")
                                    showTransactionErrorDialog(
                                        it.data?.data?.response?.responseMessage!!,
                                        "Recheck your Card Details"
                                    )
                                }
                                "401" -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                    findNavController().navigate(R.id.loginFragment2)
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


                        }
                        Status.ERROR -> {
                            ///Unable to fetch transaction Charges


                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })


        /*val token = context?.let { SharedPreferencesManager.getnewToken(it) }


        val topUpData = MainDataObject(DashBoardUtils.topUpWalletFromCard(requireContext(), "pre"))

        mobileWalletViewModel.topUpWallet(token!!, topUpData)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {


                        Status.SUCCESS -> {
                            val responseCode = it.data?.data?.response?.responseCode

                            when (responseCode) {
                                "000" -> {
                                    // binding.transactionCostAmount.text =

                                }
                                "01" -> {
                                    //toastMessage("invalid expiry date")
                                    showTransactionErrorDialog(
                                        it.data?.data?.response?.responseMessage!!,
                                        "Recheck your Card Details"
                                    )
                                }
                                "401" -> {

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


                        }
                        Status.ERROR -> {
                            ///Unable to fetch transaction Charges


                        }
                        Status.LOADING -> {


                        }
                    }
                }
            })*/
    }


    /*private fun doStKPush() {


        val token = context?.let { SharedPreferencesManager.getnewToken(it) }


        val topUpData = MainDataObject(DashBoardUtils.topUpData(context))

        mobileWalletViewModel.topUpWallet(token!!, topUpData)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {

                        Status.SUCCESS -> {
                            binding.progressbar.makeInvisible()

                            if (it.data?.data?.response?.responseCode.equals("000")) {
                                Toast.makeText(
                                    context,
                                    "Request Sent wait for STK Push",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                toastMessage(
                                    it.data!!.data.response.responseMessage,
                                )


                            }
                        }
                        Status.ERROR -> {
                            binding.progressbar.makeInvisible()

                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                        }
                        Status.LOADING -> {
                            binding.progressbar.makeVisible()


                        }
                    }
                }
            })
    }

    private fun doCardPayments() {

        val token = context?.let { SharedPreferencesManager.getnewToken(it) }


        val topUpData = MainDataObject(DashBoardUtils.topUpWalletFromCard(requireContext()))

        mobileWalletViewModel.topUpWallet(token!!, topUpData)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {


                        Status.SUCCESS -> {
                            val responseCode = it.data?.data?.response?.responseCode
                            binding.progressbar.makeInvisible()

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

                            *//* if () {
                                 showSuccessDialog()
                             } else {
                                 showErrorSnackBar(it.data!!.data.response.responseMessage)


                             }*//*
                        }
                        Status.ERROR -> {

                            showTransactionErrorDialog(
                                "Oops Sorry",
                                "Service Unavailable Try\n Again Later"
                            )
                            binding.progressbar.makeInvisible()


                        }
                        Status.LOADING -> {
                            binding.progressbar.makeVisible()


                        }
                    }
                }
            })
    }

*/
    fun showSuccessDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.succesful_transaction_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)


        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_home)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_home)

        }
        dialog.show()
        setDialogLayoutParams(
            dialog
        )
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

            when (errorTitle) {
                "Session Expired" -> {
                    findNavController().navigate(R.id.landingPageFragment)

                }
                else -> {
                    //if (title.contains("Daily Limit will be exceeded", ignoreCase = true)){
                    findNavController().navigate(R.id.topUpWalletFragment2)

                    //}

                }


            }
        }

        btnAlldone.setOnClickListener {

            dialog.dismiss()
            when (errorTitle) {
                "Session Expired" -> {
                    findNavController().navigate(R.id.landingPageFragment)

                }
                else -> {
                    // if (title.contains("Daily Limit will be exceeded", ignoreCase = true)){
                    findNavController().navigate(R.id.topUpWalletFragment2)

                    // }

                }


            }
        }
        dialog.show()
        setDialogLayoutParams(dialog)
    }


}

