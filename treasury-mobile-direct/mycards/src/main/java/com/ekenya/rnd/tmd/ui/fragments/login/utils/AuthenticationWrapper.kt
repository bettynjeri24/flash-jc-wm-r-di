package com.ekenya.rnd.tmd.ui.fragments.login.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.ekenya.rnd.auth.ui.fragments.login.biometrics.BiometricCallback
import com.ekenya.rnd.auth.ui.fragments.login.lockscreen.LockScreenCallback
import com.ekenya.rnd.tmd.ui.fragments.login.biometrics.BiometricAuthenticator
import com.ekenya.rnd.tmd.ui.fragments.login.lockscreen.LockScreenAuthenticator

class AuthenticationWrapper(activity: Activity, biometricCallback: BiometricCallback, lockScreenCallback: LockScreenCallback) {

    val biometricAuthenticator by lazy {
        BiometricAuthenticator(activity as AppCompatActivity, biometricCallback, lockScreenAuthenticator)
    }

    private val lockScreenAuthenticator by lazy {
        LockScreenAuthenticator(activity, lockScreenCallback).apply {
            createKey()
        }
    }
}
