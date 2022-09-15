package com.ekenya.rnd.common.auth.utils

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.ekenya.rnd.common.auth.biometrics.BiometricAuthenticator
import com.ekenya.rnd.common.auth.biometrics.BiometricCallback
import com.ekenya.rnd.common.auth.lockscreen.LockScreenAuthenticator
import com.ekenya.rnd.common.auth.lockscreen.LockScreenCallback


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