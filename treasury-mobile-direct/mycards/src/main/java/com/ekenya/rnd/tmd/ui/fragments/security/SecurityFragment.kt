package com.ekenya.rnd.tmd.ui.fragments.security

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ekenya.rnd.auth.ui.fragments.login.biometrics.BiometricCallback
import com.ekenya.rnd.auth.ui.fragments.login.lockscreen.LockScreenCallback
import com.ekenya.rnd.common.abstractions.BaseDaggerFragment
import com.ekenya.rnd.mycards.R
import com.ekenya.rnd.mycards.databinding.FragmentSecurityBinding
import com.ekenya.rnd.tmd.ui.fragments.login.lockscreen.LockScreenAuthenticator
import com.ekenya.rnd.tmd.ui.fragments.login.utils.AuthenticationWrapper
import com.ekenya.rnd.tmd.utils.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SecurityFragment : BaseDaggerFragment(), OnSharedPreferenceChangeListener {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentSecurityBinding

    private val authenticationWrapper by lazy {
        AuthenticationWrapper(requireActivity(), biometricCallback, lockScreenCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSecurityBinding.inflate(
            inflater,
            container,
            false
        ).also {
            binding = it
        }.root
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        sharedPreferences.apply {
            registerOnSharedPreferenceChangeListener(this@SecurityFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
    }

    private fun setUpUi() {
        binding.apply {
            materialCardViewEnableFingerPrint.setOnClickListener {
                authenticationWrapper.biometricAuthenticator.authenticate(Authenticators.BIOMETRIC_STRONG)
            }

            materialCardViewEnableVoice.setOnClickListener {
                findNavController().navigate(R.id.action_securityFragment_to_fragmentVoiceRegistration)
            }
            materialCardViewFace.setOnClickListener {
                findNavController().navigate(R.id.action_securityFragment_to_fragmentVoiceRegistration)
            }
            button.setOnClickListener {
                findNavController().navigate(R.id.action_securityFragment_to_authFragment2)
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
                sharedPreferences.getPhoneNumber()
            }
        }

        // Authentication failed
        override fun onAuthenticationFailed() {
            toast("Authentication Failed")
//            simulateLoading()
            sharedPreferences.setUseFingerPrint(true)
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

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences?, key: String?) {
        binding.imageViewCheckFingerPrint.isVisible = sharedPreferences.getUseFingerPrint()
        binding.imageViewCheckFace.isVisible = sharedPreferences.getUseFace()
        binding.imageViewCheckVoice.isVisible = sharedPreferences.getUseVoice()
    }
}
