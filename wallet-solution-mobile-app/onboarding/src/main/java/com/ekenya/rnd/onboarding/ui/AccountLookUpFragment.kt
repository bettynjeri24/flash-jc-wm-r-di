package com.ekenya.rnd.onboarding.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.*
import com.ekenya.rnd.dashboard.datadashboard.model.PhoneNumberLookupReq
import com.ekenya.rnd.dashboard.datadashboard.model.RegisterUserResp
import com.ekenya.rnd.dashboard.datadashboard.remote.APIErrorResponse
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.AccountLookupFragmentBinding
import com.hbb20.CountryCodePicker
import java.net.SocketTimeoutException
import javax.inject.Inject

class AccountLookUpFragment : Fragment() {

    private lateinit var binding: AccountLookupFragmentBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var phoneNumber: String
    private lateinit var countryCodeTv: CountryCodePicker
    private lateinit var countryCode: String
    private lateinit var version: String
    private var formattedPhone = ""

    @Inject
    lateinit var apiServiceDashBoard: ApiServiceDashBoard

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = AccountLookupFragmentBinding.inflate(inflater, container, false)

        initUI()
        setupViewModel()
        initializeOnClickListeners()
        binding.tilPhoneNumber.editText!!.setText("720304574")

        return binding.root
    }

    private fun initializeOnClickListeners() {
        binding.btnContinueToLogin.setOnClickListener { v ->

            val phone = binding.tilPhoneNumber.editText!!.text.toString()
            if (phone != null || phone != "") {
                formattedPhone = formatPhoneNumber(binding.ccp.selectedCountryCode, phone)
                doAccountLookup(formattedPhone)
            } else {
                Toast.makeText(requireContext(), "Phone number is required", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ),
                ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        binding.btnContinueToLogin.makeVisible()
        countryCodeTv = binding.ccp
        countryCode = countryCodeTv.defaultCountryCode
    }

    private fun doAccountLookup(phone: String) {
        Constants.callDialog2("Processing lookup...", requireContext())
        try {
            viewModel.performLookup(PhoneNumberLookupReq(phone))
                .observe(viewLifecycleOwner) { myAPIResponse ->
                    Log.e("mRegisterUserResp 1", "${myAPIResponse.responseObj}")
                    if (myAPIResponse.responseObj != null) {
                        Constants.cancelDialog()
                        if (myAPIResponse.requestName == "PhoneNumberLookupReq") {
                            Constants.cancelDialog()
                            val bundle = Bundle()
                            bundle.putSerializable("formattedPhoneNo", formattedPhone)

                            val mRegisterUserResp = myAPIResponse.responseObj as RegisterUserResp
                            Log.e("mRegisterUserResp", "${myAPIResponse.responseObj}")

                            if (myAPIResponse.code == 200) {
                                if (mRegisterUserResp.status == "200") {
                                    findNavController().navigate(R.id.loginFragment, bundle)
                                } else {
                                    findNavController().navigate(R.id.registerStepTwo2, bundle)
                                }
                            } else {
                                if (myAPIResponse.responseObj == null) {
                                    Toast.makeText(
                                        requireContext(),
                                        myAPIResponse.getMessage(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    val error = myAPIResponse.responseObj as APIErrorResponse
                                    Toast.makeText(
                                        requireContext(),
                                        error.error_description,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    } else {
                        Constants.cancelDialog()
                    }
                }
        } catch (e: Exception) {
            Log.e("Exception", "ERROR EXCEPTION IS $e")
        } catch (e: SocketTimeoutException) {
            Log.e("SocketTimeoutException", "ERROR EXCEPTION IS $e")
        }
    }

    fun succesfulRegistrationDialog(error: String) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.user_succesfully_registered)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnAlldone = dialog.findViewById<Button>(R.id.btn_Proceed)
        val errorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)

        errorMessage.text = error

        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.otpVerificationFragment)
        }

        btnAlldone.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.otpVerificationFragment)
        }
        dialog.show()
    }

    fun showManualRegisterDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.register_account_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogbtnCreateAccount = dialog.findViewById<Button>(R.id.btn_createAccount)
        val tvSorry = dialog.findViewById<TextView>(R.id.tv_sorry)
        val dialogBtn_remove = dialog.findViewById<Button>(R.id.btn_Cancel)
        val phoneNumber = dialog.findViewById<TextView>(R.id.tv_phoneConfirmation)
        tvSorry.text = "Account Not Found"
        phoneNumber.setText(
            "Would you like to create\na new account using  +${
            SharedPreferencesManager.getPhoneNumber(
                requireContext()
            )
            }?"
        )

        val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
        closeButton.setOnClickListener {
            dialog.dismiss()
            binding.btnContinueToLogin.makeVisible()
        }
        dialogBtn_remove.setOnClickListener {
            dialog.dismiss()
            binding.btnContinueToLogin.makeVisible()
        }
        dialogbtnCreateAccount.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.action_accountLookUpFragment_to_signUpFragment)
        }
        setDialogLayoutParams(dialog)
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.help, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (
            when (item.itemId) {
                R.id.action_help -> {
                    Navigation.findNavController(binding.btnContinueToLogin)
                        .navigate(R.id.helpFragment2)
                    true
                }
                else ->
                    super.onOptionsItemSelected(item)
            }
            )
    }

    override fun onStop() {
        super.onStop()
        showSupportActionBar()
    }

    override fun onResume() {
        super.onResume()
        showSupportActionBar()
        makeStatusBarWhite()
        removeActionBarElevation()
    }
}
