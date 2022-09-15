package com.ekenya.rnd.dashboard.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.utils.Status
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.MetreNumber
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.ConfirmSendingMoneyViewModel
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.FragmentBotswanaAccountLookupBinding


class BotswanaAccountLookupFragment : Fragment() {
    private lateinit var binding: FragmentBotswanaAccountLookupBinding
    private lateinit var viewmodel: LoginViewModel
    private lateinit var confirmSendingMoneyViewModel: ConfirmSendingMoneyViewModel
    private lateinit var metreNumber: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        lightStatusBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBotswanaAccountLookupBinding.inflate(inflater, container, false)

        initViewModel()
        setclickListeners()
        return binding.root
    }

    private fun initViewModel() {

        viewmodel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)

        confirmSendingMoneyViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireActivity()))
            )
        ).get(ConfirmSendingMoneyViewModel::class.java)

    }

    private fun setclickListeners() {
        binding.btnContinue.setOnClickListener {

            if (validDetails()) {
               // doMetreNumberLookUp()
                
                dometerNumberMockup()
            }

        }
    }

    private fun dometerNumberMockup() {
        binding.progressBar.makeVisible()

        if (metreNumber.equals( "04040404040")){
            confirmSendingMoneyViewModel.setBotswanaPMetreNumber(metreNumber)

            confirmSendingMoneyViewModel.setDstvAccountName("Test Account")
            confirmSendingMoneyViewModel.setCustomerMetreNumberName("Test Account")

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressBar.makeInvisible()

                findNavController().navigate(R.id.botswanaPowerFragment)


            }, 500)
        }else{
            binding.progressBar.makeInvisible()

            toastMessage("Account not found")




    }}




    private fun doMetreNumberLookUp() {

        val payload = MetreNumber(metreNumber)
        //check on paymentsPayload
        viewmodel.doBpcMetreNumberLookUp(payload)
            .observe(viewLifecycleOwner, {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            binding.progressBar.makeInvisible()

                            when (it.data?.status) {

                                getString(R.string.error_code_401) -> {

                                    showTransactionErrorDialog(
                                        "Session Expired",
                                        "Please Login to Continue"
                                    )
                                }

                                getString(R.string.success_code_00) -> {
                                    showSuccessSnackBar("Account Found")
                                    findNavController().navigate(R.id.botswanaPowerFragment)

                                    confirmSendingMoneyViewModel.setBotswanaPMetreNumber(metreNumber)
                                    confirmSendingMoneyViewModel.setDstvAccountName(it.data!!.kyc.name.trim())
                                    confirmSendingMoneyViewModel.setCustomerMetreNumberName(it.data!!.kyc.name.trim())


                                }


                                getString(R.string.success_code_000) -> {
                                    findNavController().navigate(R.id.botswanaPowerFragment)
                                }


                                getString(R.string.error_code_55) -> {
                                    showTransactionErrorDialog(
                                        "Metre Number Not Found",
                                        "Kindly recheck the metre Number"
                                    )
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
                            binding.progressBar.visibility = View.VISIBLE


                        }
                    }
                }
            })
    }

    private fun validDetails(): Boolean {

        metreNumber = binding.etMetreNumber.text.toString().trim()


        if (metreNumber.isBlank() || metreNumber.length != 11) {
            binding.tilMetreNumber.error = "Please enter a valid Metre number"
            binding.tilMetreNumber.error
            return false
        }
        return true
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

        }


        btnAlldone.setOnClickListener {

            dialog.dismiss()


        }
        dialog.show()
        setDialogLayoutParams(dialog)
    }


}