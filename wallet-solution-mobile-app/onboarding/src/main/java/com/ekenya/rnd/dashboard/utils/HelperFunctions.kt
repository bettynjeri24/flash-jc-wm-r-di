package com.ekenya.rnd.dashboard.utils

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.onboarding.BuildConfig
import com.ekenya.rnd.onboarding.R
import com.google.android.material.transition.MaterialContainerTransform
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

fun hasPermissions(context: Context, vararg permissions: String): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
}

fun isInsufficientFunds(amount: Int, context: Context): Boolean {

    val accountBalance = SharedPreferencesManager.getUnforMattedAccountBalance(context)
    if (accountBalance != null) {
        if (accountBalance < amount) {
            return false
        }
    }
    return true
}

fun setDialogLayoutParams(dialog: Dialog) {
    val lp = WindowManager.LayoutParams()
    lp.copyFrom(dialog.window!!.attributes)
    lp.width = WindowManager.LayoutParams.MATCH_PARENT
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT
    val window = dialog.window
    window!!.attributes = lp
}


fun formatAmount(input:String):String{
    return "%,d".format(input)
}


fun makeActive(
    activePaymentOption: TextView, activeRadioButton: View,
    inActivePaymentOption1: TextView,inActiveRadioButton1: View,
    inActivePaymentOption2: TextView,inActiveRadioButton2: View,
    inActivePaymentOption3: TextView,inActiveRadioButton3: View,
    inActivePaymentOption4: TextView,inActiveRadioButton4: View,
    inActivePaymentOption5: TextView,inActiveRadioButton5: View,

) {
    activePaymentOption.makeActivePaymentOption()
    activeRadioButton.makeRadioButtonActive()

    inActivePaymentOption1.makeInactivePaymentOption()
    inActiveRadioButton1.makeRadioButtonInActive()

    inActivePaymentOption2.makeInactivePaymentOption()
    inActiveRadioButton2.makeRadioButtonInActive()

    inActivePaymentOption3.makeInactivePaymentOption()
    inActiveRadioButton3.makeRadioButtonInActive()

    inActivePaymentOption4.makeInactivePaymentOption()
    inActiveRadioButton4.makeRadioButtonInActive()


    inActivePaymentOption5.makeInactivePaymentOption()
    inActiveRadioButton5.makeRadioButtonInActive()


}

fun xyztest(){

}

fun Fragment.setSharedElementTransition() {
    sharedElementEnterTransition = MaterialContainerTransform().apply {
        drawingViewId = R.id.nav_host_fragment_activity_dashboard
        duration = 500
        scrimColor = Color.TRANSPARENT
        setAllContainerColors(ContextCompat.getColor(requireContext(), android.R.color.white))
    }
}

fun formatPhoneNumber(countryCode:String,unformattedPhone: String): String {
    var formatPhone = ""

    if (unformattedPhone.startsWith("0")) {
        formatPhone = countryCode + unformattedPhone.substring(1)
    } else if (unformattedPhone.length < 10) {
        formatPhone = countryCode + unformattedPhone
    }
    return formatPhone
}

fun getReleaseMode(): String {
    return if (BuildConfig.DEBUG) {
        "debug"

    } else {
        "release"
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun Fragment.isConnectedToInternet(): Boolean {
    val connectivityManager =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}


fun encryptRSA(payload: String): String? {
    var encryptedPayload: String? = ""
    try {
        val cipherA =
            Cipher.getInstance(Constants.RSA_PADDING)
        var keySpec: X509EncodedKeySpec? = null
        keySpec = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            X509EncodedKeySpec(java.util.Base64.getDecoder().decode(Constants.PUBLIC_KEY_ADD_CARD))
        } else {
            X509EncodedKeySpec(
                Base64.decode(
                    Constants.PUBLIC_KEY_ADD_CARD,
                    Base64.NO_WRAP
                )
            )
        }
        val keyFactory = KeyFactory.getInstance(Constants.ALGORITHM)
        val publicKey = keyFactory.generatePublic(keySpec)
        cipherA.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipherA.doFinal(payload.toByteArray())
        encryptedPayload = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getEncoder().encodeToString(encrypted)
        } else {
            Base64.encodeToString(encrypted, Base64.NO_WRAP)
        }
    } catch (ex: java.lang.Exception) {
        encryptedPayload = ""
        //Timber.e("ERROR ENCRYPTING $ex")
    }
    return encryptedPayload
}
