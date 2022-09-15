package com.ekenya.rnd.auth.ui.fragments.login.biometrics

import androidx.biometric.BiometricPrompt

interface BiometricCallback {

        fun onAuthenticationError(errorCode: Int, errString: CharSequence)

        fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult)

        fun onAuthenticationFailed()
}