package com.ekenya.rnd.tmd.ui.fragments.login

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.auth.ui.fragments.login.biometrics.BiometricCallback
import com.ekenya.rnd.auth.ui.fragments.login.lockscreen.LockScreenCallback
import com.ekenya.rnd.baseapp.di.injectables.ViewModelFactory
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.common.form_validation.ui_extensions.removeNonDigits
import com.ekenya.rnd.common.utils.Resource
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentLoginAuthBinding
import com.ekenya.rnd.tmd.ui.fragments.login.lockscreen.LockScreenAuthenticator
import com.ekenya.rnd.tmd.ui.fragments.login.utils.AuthenticationWrapper
import com.ekenya.rnd.tmd.utils.getPhoneNumber
import com.ekenya.rnd.tmd.utils.saveAccessToken
import com.ekenya.rnd.tmd.utils.toast
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : BaseDaggerFragment() {

    @Inject
    lateinit var sharedpreferences: SharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewmodel by lazy {
        ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
    }

    private val authenticationWrapper by lazy {

        AuthenticationWrapper(requireActivity(), biometricCallback, lockScreenCallback)
    }

    private lateinit var binding: FragmentLoginAuthBinding

    private var pinAdapter: PinAdapter = PinAdapter()

    // observe this list to get the pin entered
    private var pinList = MutableLiveData(
        mutableListOf<Pin>().apply {
            repeat(4) {
                add(Pin("*"))
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentLoginAuthBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpObserver()
        setUpKeyboard()
//        setUpResultListener()
    }

//    private fun setUpResultListener() {
//        setFragmentResultListener("requestKey") { _, bundle ->
//            // We use a String here, but any type that can be put in a Bundle is supported
//            try {
//                when (bundle.getSerializable("voice") as VOICE) {
//                    VOICE.SUCCESS -> {
//                        showFeatureModule(moduleHome)
//                    }
//                    VOICE.ERROR -> {
//                        // do something
//                    }
//                }
//            } catch (e: Exception) {
//            }
//        }
//    }

    private fun setUpUI() {
        binding.apply {
            // set up pin recyclerview
            recyclerView2.apply {
                pinAdapter.submitList(pinList.value)
                adapter = pinAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }

            textViewFingerPrint.setBiometricsLauncher(BIOMETRIC_STRONG)

            textViewFaceId.setOnClickListener {
                findNavController().navigate(R.id.action_authFragment2_to_selfieFragmentAuth)
            }

            textViewVoiceId.setOnClickListener {
                findNavController().navigate(R.id.action_authFragment2_to_fragmentVoiceRegistration)
            }
        }
    }

    private fun setUpKeyboard() {
        // set up keyboard
        binding.includeKeyBoard.apply {
            btnOne.getKeyboardDigit()

            btnTwo.getKeyboardDigit()

            btnThree.getKeyboardDigit()

            btnFour.getKeyboardDigit()

            btnFive.getKeyboardDigit()

            btnSix.getKeyboardDigit()

            btnSeven.getKeyboardDigit()

            btnEight.getKeyboardDigit()

            btnNine.getKeyboardDigit()

            btnZero.getKeyboardDigit()

            // textViewVoiceId.setBiometricsLauncher()

            // removing the last digit
            btnErase.setOnClickListener {
                pinList.value?.asReversed()?.forEach { pin ->
                    if (pin.digit != "*") {
                        pin.digit = "*"
                        pinList.value = pinList.value
                        return@setOnClickListener
                    }
                }
            }
        }
    }

    // set up observer for the pin list
    private fun setUpObserver() {
        pinList.observe(viewLifecycleOwner) { pin ->
            pinAdapter.submitList(pin)
            pinAdapter.notifyDataSetChanged()
            if (pin.filter { it.digit == "*" }.toList().isEmpty()) {
                simulateSearching(pin.toString().removeNonDigits())
            }
        }
    }

    private fun simulateSearching(pin: String) {
        lifecycleScope.launch {
            viewmodel.login(sharedpreferences.getPhoneNumber().toString(),pin).collect { resource ->
                when (resource) {
                    is Resource.Error -> {
                        showHideProgress(null)
                        Log.e(TAG, "simulateSearching: ${resource.error?.localizedMessage}")
                    }
                    is Resource.Loading -> {
                        showHideProgress("Loading")
                    }
                    is Resource.Success -> {
                        showHideProgress(null)
                        if (resource.data?.status == "Success") {
                            resource.data?.data?.accessToken?.let {
                                sharedpreferences.saveAccessToken(it)
                            }
                        }
                        if (arguments?.getString("intent") == "auth") {
                            setFragmentResult(
                                "requestKey",
                                Bundle().apply {
                                    putBoolean("result", true)
                                }
                            )
                            findNavController().navigateUp()
                            return@collect
                        }

                        findNavController().navigate(R.id.action_authFragment2_to_homeFragment)
                    }
                }
            }
        }
    }

    private fun isBiometricReady(): Boolean {
        val biometricManager = from(requireContext())
        return biometricManager.canAuthenticate() == BIOMETRIC_SUCCESS
    }

    // get value from pressed button
    private fun Button.getKeyboardDigit() {
        setOnClickListener {
            pinList.value?.forEach { digit ->
                if (digit.digit == "*") {
                    digit.digit = text.toString()
                    pinList.value = pinList.value
                    return@setOnClickListener
                }
            }
        }
    }

    // launch biometrics
    private fun TextView.setBiometricsLauncher(biometricAllowedAuth: Int) {
        setOnClickListener {
            if (isBiometricReady()) {
                authenticationWrapper.biometricAuthenticator.authenticate(biometricAllowedAuth)
            }
        }
    }

    private val biometricCallback = object : BiometricCallback {
        /**
         * This three callbacks are for biometric authentication
         */
        // Error occured during authentication
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            /**
             * This when expression handles all the error codes if an authentication error occurs
             * @see {@https://developer.android.com/reference/android/hardware/biometrics/BiometricPrompt}
             */

            toast(errString.toString())
        }

        // Authentication was successful
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            lifecycleScope.launch {
//                simulateLoading()
            }
        }

        // Authentication failed
        override fun onAuthenticationFailed() {
            toast("Authentication Failed")
//            simulateLoading()
        }
    }

    private val lockScreenCallback = object : LockScreenCallback {
        /**
         * This callback methods is invoked  when login screen with PIN is needed
         */

        override fun showAuthenticationScreen() {
            // Create the Confirm Credentials screen. You can customize the title and description. Or
            // we will provide a generic one for you if you leave it null

            val mKeyguardManager: KeyguardManager =
                requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

            val intent: Intent = mKeyguardManager.createConfirmDeviceCredentialIntent(null, null)

            startActivityForResult(
                intent,
                LockScreenAuthenticator.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS
            )
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
    }
}

// TODO bank account in self registration
