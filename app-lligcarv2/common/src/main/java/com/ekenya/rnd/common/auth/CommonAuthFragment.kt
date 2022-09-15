package com.ekenya.rnd.common.auth

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekenya.rnd.common.auth.biometrics.BiometricCallback
import com.ekenya.rnd.common.auth.lockscreen.LockScreenAuthenticator
import com.ekenya.rnd.common.auth.lockscreen.LockScreenCallback
import com.ekenya.rnd.common.auth.utils.AuthenticationWrapper
import com.ekenya.rnd.common.auth.utils.toast
import com.ekenya.rnd.common.databinding.FragmentAuthBinding
import com.ekenya.rnd.common.form_validation.ui_extensions.removeNonDigits
import com.ekenya.rnd.common.utils.base.BaseCommonCargillDIFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor

class CommonAuthFragment : BaseCommonCargillDIFragment<FragmentAuthBinding>(
    FragmentAuthBinding::inflate
) {

    private val authenticationWrapper by lazy {

        AuthenticationWrapper(requireActivity(), biometricCallback, lockScreenCallback)
    }

    // private lateinit var binding: FragmentAuthBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var pinAdapter: PinAdapter = PinAdapter()

    // observe this list to get the pin entered
    private var pinList = MutableLiveData(
        mutableListOf<Pin>().apply {

            // initialize it with 4 dummy digits
            repeat(4) {
                add(Pin("*"))
            }
        }
    )

/*    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAuthBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpObserver()
    }

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

            // set up keyboard
            includeKeyBoard.apply {
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

                textViewFingerPrint.setBiometricsLauncher(BiometricManager.Authenticators.BIOMETRIC_STRONG)

                textViewFaceId.setBiometricsLauncher(BiometricManager.Authenticators.BIOMETRIC_WEAK)

//                textViewVoiceId.setBiometricsLauncher()

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
    }

    // set up observer for the pin list
    private fun setUpObserver() {
        pinList.observe(viewLifecycleOwner) { pin ->
            pinAdapter.submitList(pin)
            pinAdapter.notifyDataSetChanged()

            if (pin.filter { it.digit == "*" }.toList().isEmpty()) {
                Timber.e(
                    "PIN========================\n ${
                    pin.toString().removeNonDigits()
                    } \n=================="
                )
                simulateLoading(AuthResult.AUTH_SUCCESS, pin = pin.toString().removeNonDigits())
            }
        }
    }

    private fun simulateLoading(authResult: AuthResult, pin: String) {
        lifecycleScope.launch {
            // simulateSearching()
            // Use the Kotlin extension in the fragment-ktx artifact
            setFragmentResult("requestKey", bundleOf(AuthResultTag to authResult, "pin" to pin))
            findNavController().navigateUp()
        }
    }

    private suspend fun simulateSearching() {
        showCustomDialog("Authenticating")
        delay(2000)
        showCustomDialog(null)
    }

    private fun isBiometricReady(): Boolean {
        val biometricManager = BiometricManager.from(requireContext())
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
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
            simulateLoading(AuthResult.AUTH_ERROR, "")

            when (errorCode) {
                BiometricPrompt.AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> {
                }

                BiometricPrompt.AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> {
                }

                // ..... etc Customize this based on your use cases
            }
        }

        // Authentication was successful
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            lifecycleScope.launch {
                simulateLoading(AuthResult.AUTH_SUCCESS, "")
            }
        }

        // Authentication failed
        override fun onAuthenticationFailed() {
            toast("Authentication Failed")
            simulateLoading(AuthResult.AUTH_ERROR, "")
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
        private const val TAG = "PinBottomSheet"
        const val AuthResultTag = "authResult"
    }
}

enum class AuthResult {
    AUTH_SUCCESS,
    AUTH_ERROR
}
