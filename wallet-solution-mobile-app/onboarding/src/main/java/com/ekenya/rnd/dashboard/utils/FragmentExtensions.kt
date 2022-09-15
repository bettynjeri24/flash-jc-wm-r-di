package com.ekenya.rnd.dashboard.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.data.model.MainDataObject
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.model.TopUpWalletResponse
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.MobileWalletViewModel
import com.ekenya.rnd.onboarding.R


fun Fragment.darkStatusBar() {
    requireActivity().window.apply {
        darkStatusBar()
    }

}

fun Fragment.makeStatusBarWhite() {

    changeStatusBarColor(ContextCompat.getColor(requireContext(), android.R.color.white))


}

fun Fragment.lightStatusBar() {
    requireActivity().window.apply {
        lightStatusBar()
    }

}

fun Fragment.makeStatusBarTransparent2() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }*/
            statusBarColor = Color.TRANSPARENT
        }
    }
}

/*fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}


fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}*/


fun Context.hideKeyboard(view: View) {
// Only runs if there is a view that is currently focused
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
// val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
// inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun View.hideKeyboardView() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun Fragment.showSuccessDialog() {
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

fun Fragment.showSuccessDialog(message:String) {
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
    val tvMessage = dialog.findViewById<Button>(R.id.tv_phoneConfirmation)
    tvMessage.text = message

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

fun Fragment.showTransactionErrorDialog(error: String) {
    val dialog = Dialog(requireContext())
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.transaction_failed_dialog)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
    val errorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)

    errorMessage.text = error

    val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
    closeButton.setOnClickListener {
        dialog.dismiss()
        findNavController().popBackStack(R.id.navigation_home, true)
    }

    btnAlldone.setOnClickListener {
        dialog.dismiss()
        findNavController().popBackStack(R.id.navigation_home, true)

    }
    dialog.show()
}


/*
fun Fragment.showCardPaymentOptionsDialog(requestingFragment2: String) {
    var requestingFragment = requestingFragment2

    val confirmSendingMoneyViewModel = ViewModelProvider(
        requireActivity(),
        ViewModelFactory(
            ApiHelper(RetrofitBuilder.apiService),
            DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
        )
    ).get(ConfirmSendingMoneyViewModel::class.java)
    confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)


    var selectedOption = Constants.MARI_WALLET

    confirmSendingMoneyViewModel.paymentOption.observe(viewLifecycleOwner, {
        selectedOption = it
    })
    confirmSendingMoneyViewModel.requestingFragment.observe(viewLifecycleOwner, {
        requestingFragment = it
    })


    val bottomsheetlayoutBinding = PaymentsModeBottomsheetBinding.inflate(layoutInflater)
    bottomsheetlayoutBinding.tvSelectSourceofFunds.text = getString(R.string.pay_from)
    */
/*if (requestingFragment == Constants.TOP_WALLET_FRAGMENT) {
        bottomsheetlayoutBinding.cardViewWallet.makeGone()
        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.safaricom_mpesa))
        makeActive(
            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,

            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,

            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )
    } else {
        confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)

        makeActive(
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,
            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )

    }*//*


    val dialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
    dialog.setContentView(bottomsheetlayoutBinding.root)
    dialog.show()


    bottomsheetlayoutBinding.cardViewWallet.setOnClickListener {
        confirmSendingMoneyViewModel.setPaymentOption(Constants.MARI_WALLET)

    */
/*    makeActive(
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,
            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )*//*


    }
    bottomsheetlayoutBinding.cardviewDebitorCredit.setOnClickListener {

        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.telebirr_cash))
   */
/*     makeActive(
            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,
            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )*//*

    }
    bottomsheetlayoutBinding.cardviewEthiocash.setOnClickListener {
        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.ethio_cash))

   */
/*     makeActive(
            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,

            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )*//*

    }
    bottomsheetlayoutBinding.cardviewMpesa.setOnClickListener {
        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.safaricom_mpesa))
 */
/*       makeActive(
            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,

            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,

            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )*//*



    }
    bottomsheetlayoutBinding.cardviewBank.setOnClickListener {
        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.bank))
 */
/*       makeActive(
            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,

            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,

            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,

            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
            bottomsheetlayoutBinding.tvOptionTelebirr,
            bottomsheetlayoutBinding.telebirview
        )*//*



    }
    bottomsheetlayoutBinding.cardviewtelebirrCash.setOnClickListener {
        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.debit_or_credit_card))
   */
/*     makeActive(
            bottomsheetlayoutBinding.tvOptionTelebirr, bottomsheetlayoutBinding.telebirview,

            bottomsheetlayoutBinding.tvOptionbank,
            bottomsheetlayoutBinding.bankView,

            bottomsheetlayoutBinding.tvOptionMpesaCash,
            bottomsheetlayoutBinding.mpesaview,

            bottomsheetlayoutBinding.tvOptionEthiocash,
            bottomsheetlayoutBinding.ethiocashview,

            bottomsheetlayoutBinding.tvOptionDebitOrCreditCard,
            bottomsheetlayoutBinding.debitcardview,
            bottomsheetlayoutBinding.tvOptionMariWallet,
            bottomsheetlayoutBinding.walletcardview,
        )*//*



    }
    */
/*bottomsheetlayoutBinding.card.setOnClickListener {
        confirmSendingMoneyViewModel.setPaymentOption(getString(R.string.safaricom_mpesa))

    }*//*


    bottomsheetlayoutBinding.btnContinue.setOnClickListener {
        dialog.dismiss()
//toastMessage(" $selectedOption, $requestingFragment")

        if (selectedOption.isNotEmpty() */
/*&& selectedOption == Constants.MARI_WALLET*//*
) {
// confirmSendingMoneyViewModel.setPaymentOption(requestingFragment)
            when (requestingFragment) {
                Constants.SEND_TO_WALLET -> {
                    findNavController().navigate(R.id.sendMoneyConfirmationFragment)
                }
                Constants.SEND_TO_MOBILE -> {
                    findNavController().navigate(R.id.sendMoneyConfirmationFragment)
                }
                Constants.SEND_TO_BANK -> {
                    findNavController().navigate(R.id.billPaymentsConfirmationFragment)
                }
                Constants.WITHDRAW_FRAGMENT -> {
                    findNavController().navigate(R.id.withdrawConfirmationFragment)
                }
                Constants.TOP_WALLET_FRAGMENT -> {
                    findNavController().navigate(R.id.transactionConfirmationFragment)
                }
                Constants.TICKETING_FRAGMENT -> {
                    //toastMessage("Feature Coming soon")

                    findNavController().navigate(R.id.ticketingConfirmationScreen)
                    //findNavController().navigate(R.id.billPaymentsConfirmationFragment)
                }

                else -> {
                    findNavController()
                        .navigate(R.id.billPaymentsConfirmationFragment)

                }
            }


        } */
/*else if (selectedOption.isNotEmpty() && selectedOption == Constants.DEBIT_OR_CREDIT) {
// confirmSendingMoneyViewModel.setRequestingFragment(requestingFragment)

findNavController()
.navigate(R.id.enterCardFragment)

}*//*


    }


}
*/

fun Fragment.doCardPayments(
    mobileWalletViewModel: MobileWalletViewModel,
    stage: String
): LiveData<Resource<TopUpWalletResponse>> {

    val token = context?.let { SharedPreferencesManager.getnewToken(it) }


    val topUpData = MainDataObject(DashBoardUtils.topUpWalletFromCard(requireContext(), stage))

    return mobileWalletViewModel.topUpWallet(token!!, topUpData)
/*.observe(viewLifecycleOwner, {
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
        binding.avi.makeInvisible()


    }
    Status.LOADING -> {
        binding.avi.makeVisible()


    }
}
}
})*/

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}









