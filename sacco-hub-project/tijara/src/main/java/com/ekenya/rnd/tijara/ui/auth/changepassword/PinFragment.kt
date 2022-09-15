package com.ekenya.rnd.tijara.ui.auth.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.PinFragmentBinding
import com.ekenya.rnd.tijara.requestDTO.StatementDTO
import com.ekenya.rnd.tijara.requestDTO.VerifyUserDTO
import com.ekenya.rnd.tijara.ui.homepage.home.dashboard.DashboardViewModel
import com.ekenya.rnd.tijara.ui.homepage.loan.payloan.LoanRepaymentViewModel
import com.ekenya.rnd.tijara.ui.homepage.loan.getloan.LoanRequestViewModel
import com.ekenya.rnd.tijara.utils.*
import java.util.concurrent.Executor
import javax.inject.Inject

class PinFragment : BaseDaggerFragment() {
    private lateinit var pinBinding:PinFragmentBinding
    private lateinit var viewModel: PinViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val dashboardModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(DashboardViewModel::class.java)
    }
    private val payLoanViewModel: LoanRepaymentViewModel by activityViewModels()
    /* by lazy {
        ViewModelProvider(requireActivity()).get(LoanRepaymentViewModel::class.java)
    }*/
    //biometrics
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    /**the pin inputs*/
    private var one1: String? = null
    private var two2: String? = null
    private var three3: String? = null
    private var four4: String? = null
    private var mConfirmPin: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().setTheme(R.style.FragmentTheme)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(PinViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.statusBarColor = resources.getColor(R.color.priDarkColor)
        pinBinding= PinFragmentBinding.inflate(layoutInflater)
        hideKeyboard()
        return pinBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bioMetric.observe(viewLifecycleOwner, Observer {
            if (it)
                prompt()
        })
        initBioMetrics()
        pinBinding.apply {
            ivBack.setOnClickListener { findNavController().navigateUp() }
            btnOne.setOnClickListener { controlPinPad2("1") }
            btnTwo.setOnClickListener{controlPinPad2("2")}
            btnThree.setOnClickListener{ controlPinPad2("3")}
            btnFour.setOnClickListener{ controlPinPad2("4")}
            btnFive.setOnClickListener { controlPinPad2("5") }
            btnSix.setOnClickListener{controlPinPad2("6")}
            btnSeven.setOnClickListener { controlPinPad2("7") }
            btnEight.setOnClickListener{controlPinPad2("8")}
            btnNine.setOnClickListener{ controlPinPad2("9")}
            btnZero.setOnClickListener{ controlPinPad2("0")}
            btnDelete.setOnClickListener { deletePinEntry() }
        }
        viewModel.statusVCode.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                clearPin()
                pinBinding.clPin.makeVisible()
                pinBinding.apply {
                    avi.makeGone()
                }
                when (it) {
                    1 -> {
                        pinBinding.avi.makeGone()
                        clearPin()
                        findNavController().navigateUp()

                    }
                    0 -> {
                        pinBinding.avi.makeGone()
                        clearPin()
                        onInfoDialog(context,viewModel.statusMessage.value)
                        viewModel.stopObserving()
                    }
                    else -> {
                        clearPin()
                        pinBinding.avi.makeGone()
                        onInfoDialog(context, getString(R.string.error_occurred))
                        viewModel.stopObserving()

                    }
                }
            }
        })




    }
    private fun controlPinPad2(entry: String){
        pinBinding.apply {
            if (one1==null) {
                one1 = entry
                pin1.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }else if (two2==null){
                two2=entry
                pin2.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }else if (three3==null){
                three3=entry
                pin3.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }else if (four4==null){
                four4=entry
                pin4.background = ContextCompat.getDrawable(requireContext(), R.drawable.active_pin_bg)
            }
            if (mConfirmPin == null) {
                mConfirmPin = entry
            } else {
                mConfirmPin += entry
            }
            if (mConfirmPin!!.length == 4) {
                pinBinding.clPin.visibility=View.GONE
                pinBinding.avi.show()
                if (isNetwork(requireContext())) {
                    val verifyUserDTO = VerifyUserDTO()
                    verifyUserDTO.password = mConfirmPin as String
                    viewModel.verifyUser(verifyUserDTO)
                }else{
                    clearPin()
                    pinBinding.clPin.makeVisible()
                    pinBinding.avi.hide()
                    onNoNetworkDialog(requireContext())
                }
            }

        }


    }
    private fun prompt() {
        biometricPrompt.authenticate(promptInfo)
        viewModel.checkedBiometrics()
    }
    private fun initBioMetrics() {
        //biometrics
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    toastyError(requireContext(),"Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    toastySuccess(requireContext(),"Authentication succeeded!")
                    // viewModel.navigate()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    toastyError(requireContext(),"Authentication failed")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.fingerprint_login))
            .setSubtitle(" ")
            .setNegativeButtonText(getString(R.string.use_password))
            .setDescription(getString(R.string.fingerprint_desc))
            .build()
    }
    private fun deletePinEntry() {
        pinBinding.apply {
            if (mConfirmPin != null && mConfirmPin!!.length > 0) {
                mConfirmPin = mConfirmPin!!.substring(0, mConfirmPin!!.length - 1)
            }
            if (four4 != null) {
                pin4.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                four4 = null
            } else if (three3 != null) {
                pin3.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                three3 = null
            } else if (two2 != null) {
                pin2.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                two2 = null
            } else if (one1 != null) {
                pin1.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
                one1 = null
            }
        }
    }
    private fun clearPin(){
        pinBinding.apply {
            pin1.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            one1 = null
            pin2.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            two2 = null
            pin3.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            three3 = null
            pin4.background= ContextCompat.getDrawable(requireContext(), R.drawable.inactive_pin_bg)
            four4 = null
            mConfirmPin=null
        }


    }
}