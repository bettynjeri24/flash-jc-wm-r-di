package com.ekenya.rnd.common

import android.os.Build
import timber.log.Timber

class Utils {
    companion object{
        fun isEmulator(): Boolean {
            val model = Build.MODEL
            Timber.d( "model=$model")
            val product = Build.PRODUCT
            Timber.d( "product=$product")
            return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.HARDWARE.contains("goldfish")
                    || Build.HARDWARE.contains("ranchu")
                    || Build.HARDWARE.contains("nox")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("SM-G950U1")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || Build.PRODUCT.contains("sdk_google")
                    || Build.PRODUCT.contains("google_sdk")
                    || Build.PRODUCT.contains("sdk")
                    || Build.PRODUCT.contains("sdk_x86")
                    || Build.PRODUCT.contains("vbox86p")
                    || Build.PRODUCT.contains("emulator")
                    || Build.PRODUCT.contains("dreamqlteue")
                    || Build.PRODUCT.contains("simulator"))
        }
    }
}