package com.ekenya.rnd.dashboard.view


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
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.common.utils.toastMessage
import com.ekenya.rnd.dashboard.base.ViewModelFactory2
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper
import com.ekenya.rnd.dashboard.datadashboard.api.ApiHelper2
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder
import com.ekenya.rnd.dashboard.datadashboard.api.RetrofitBuilder2
import com.ekenya.rnd.dashboard.datadashboard.model.LoginUserReq
import com.ekenya.rnd.dashboard.datadashboard.model.LoginUserResp
import com.ekenya.rnd.dashboard.database.DatabaseBuilder
import com.ekenya.rnd.dashboard.database.DatabaseHelperImpl
import com.ekenya.rnd.dashboard.utils.*
import com.ekenya.rnd.dashboard.utils.AppUtils.getTimeofTheDay
import com.ekenya.rnd.dashboard.viewmodels.LoginViewModel
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.databinding.LoginFragmentBinding
import java.util.concurrent.Executor


class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var dialog: Dialog
    private lateinit var pin: String
    private var formattedPhoneNo = ""
    private lateinit var loginViewModel: LoginViewModel
    private var one1: String? = null
    private var isDone = false
    private var two2: String? = null
    private var three3: String? = null
    private var four4: String? = null
    private var mConfirmPin: String? = null
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = LoginFragmentBinding.inflate(layoutInflater)
        initUI()

        setupViewModel()
        initBioMetrics()

        //SharedPreferencesManager.setNumberOfSavingsAccount(requireContext(),"0")
        initClickListeners()
        setupObservers()
        resetPin()

        return binding!!.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            formattedPhoneNo = requireArguments().getString("formattedPhoneNo").toString()
            SharedPreferencesManager.setPhoneNumber(requireContext(), formattedPhoneNo)
        }
    }

    private fun initUI() {
        dialog = Dialog(requireContext())
        (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(
            R.drawable.ic_baseline_arrow_back_24
        )
        SharedPreferencesManager.setAccountBalance(
            requireContext(),
            "GHS " + "0.0"
        )

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

    private fun setupObservers() {
        loginViewModel.bioMetric.observe(viewLifecycleOwner, Observer {
            if (it)
                prompt()
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initClickListeners() {
        KeyPadListeners()
    }


    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory2(
                ApiHelper(RetrofitBuilder.apiServiceDashBoard),
                DatabaseHelperImpl(
                    DatabaseBuilder.getInstance(requireContext())
                ), ApiHelper2(RetrofitBuilder2.apiService)
            )
        ).get(LoginViewModel::class.java)
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

        binding.tvForgotPin.setOnClickListener {
            findNavController().navigate(R.id.action_loginfragment_to_resetpin)
        }
        val name = SharedPreferencesManager.getMiddleName(requireContext())

        binding.tvGreetings.text = "${getTimeofTheDay()} $name!  \n"
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
                        //SharedPreferencesManager.setPin(requireContext(), pin)
                        mLoginUser(LoginUserReq(formattedPhoneNo,pin))

                    } else {

                        showTransactionErrorDialog(
                            "Oops Sorry",
                            "Check your Internet Connectivity\n And try again"
                        )
                        //findNavController().navigate(R.id.action_loginFragment2_to_landingPageFragment)

                    }
                }
            }

        }
    }

    fun showTransactionErrorDialog(errorTitle: String, errorMessage: String) {

        if (!dialog.isShowing && dialog != null) {


            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.transaction_failed_dialog)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val btnAlldone = dialog.findViewById<Button>(R.id.btn_AllDone)
            val tvErrorMessage = dialog.findViewById<TextView>(R.id.tv_errorMessage)
            val tvErrorTitle = dialog.findViewById<TextView>(R.id.tv_sorry)

            tvErrorMessage.text = errorMessage
            tvErrorTitle.text = errorTitle
            /* when (errorMessage) {
                 "Check your Internet Connectivity\n" +
                         " And try again" -> {
                 }
             }*/
            btnAlldone.text = "OK"


            val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
            closeButton.setOnClickListener {


                when (errorTitle) {
                    "Invalid phone number or PIN" -> {
                        dialog.dismiss()
                        resetPin()
                    }
                    "Oops Sorry" -> {
                        dialog.dismiss()
                        resetPin()
                    }
                    else -> {
                        dialog.dismiss()
                        findNavController().popBackStack(R.id.landingPageFragment, false)
                    }
                }


            }

            btnAlldone.setOnClickListener {
                when (errorTitle) {
                    "Invalid phone number or PIN" -> {
                        dialog.dismiss()
                        resetPin()
                    }
                    "Oops Sorry" -> {
                        dialog.dismiss()
                        resetPin()
                    }
                    else -> {
                        dialog.dismiss()
                        findNavController().popBackStack(R.id.landingPageFragment, false)
                    }
                }

            }
            dialog.show()
            setDialogLayoutParams(dialog)
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


    private fun mLoginUser(loginUserReq: LoginUserReq)
    {

        Constants.callDialog2("Signing in...", requireContext())
        loginViewModel.userLoginCbg(loginUserReq).observe(viewLifecycleOwner) { myAPIResponse ->
            if(myAPIResponse.requestName == "LoginUserReq") {
                Constants.cancelDialog()
                if (myAPIResponse.code == 200)
                {
                    val loginResponse = myAPIResponse.responseObj as LoginUserResp
                    if(loginResponse.status == 200){
                        val customer = loginResponse.data!!.customer
                        SharedPreferencesManager.setWalletAccNumber(requireContext(),customer!!.walletAccountNo)
                        findNavController().navigate(R.id.homeFragment2)
                    }
                    else if(loginResponse.status == 401)
                    {
                        Toast.makeText(requireContext(),"Invalid credentials try again",Toast.LENGTH_LONG).show()
                    }
                }

            }
            else
            {
                Log.d("tagged","else")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showSupportActionBar()
        makeStatusBarWhite()
        if (SharedPreferencesManager.hasSetInitialPin(requireContext()) == 0) {
            //findNavController().navigate(R.id.loginFragment2_toaction_enterinitialpin)
            //findNavController().navigate(R.id.createNewPinFragment2)

        }
        /*if (SharedPreferencesManager.isRegisteredUser(requireContext())==false){
        }*/
    }

    private fun prompt() {
        biometricPrompt.authenticate(promptInfo)
        loginViewModel.checkedBiometrics()
    }

    private fun initBioMetrics() {
        //biometrics
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    toastMessage("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    toastMessage("Authentication succeeded!")
                    // viewModel.navigate()
                    findNavController().navigate(R.id.action_loginFragment2_to_homeFragment)

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    toastMessage("Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_login))
            .setSubtitle(" ")
            .setNegativeButtonText(getString(R.string.use_password))
            .setDescription(getString(R.string.fingerprint_desc))
            .build()
    }
}


