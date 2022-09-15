package io.eclectics.cargilldigital.utils.dk

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.data.network.ResourceNetwork
import io.eclectics.cargilldigital.ui.auth.login.LoginPinFragment
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@SuppressLint("MissingPermission")
fun Fragment.isConnetionAvailableAndHandleNoNetwork(
    handleNetworkFailure: (() -> Unit)? = null
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val connectivityManager =
            requireActivity().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Timber.v(" LoginPinFragment Wifi Connected")
                // Helpers().toast(requireContext(), "Wifi Connected")
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Timber.v("Wifi Connected")
                // Helpers().toast(requireContext(), "Mobile Data Connected")
            } else {
                handleNetworkFailure!!.invoke()
            }
        }
    } else {
        val connectivityManager =
            requireActivity().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.activeNetworkInfo!!
        if (!capabilities.isConnected && capabilities == null) {
            handleNetworkFailure!!.invoke()
        } else {
            Timber.v("Wifi Connected")
        }
    }
}

fun Fragment.isConnectionAvailable(): Boolean {
    var result = false
    val connectivityManager =
        requireActivity().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }
    return result

}

fun Fragment.sweetAlertDialogProgressType(showIf: Int) {
    val pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
    pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
    pDialog.titleText = "Loading"
    // pDialog.setCancelable(false)
    if (showIf == 1) {
        Timber.e("sweetAlertDialogProgressType 1")
        pDialog.show()
    } else if (showIf == 0) {
        Timber.e("sweetAlertDialogProgressType 0")
        pDialog.dismiss()
    }
}

fun Activity.snackBarCustom(msg: String, action: (() -> Unit)? = null) {
    Snackbar.make(
        findViewById(android.R.id.content),
        msg,
        Snackbar.LENGTH_LONG
    ).also {
        it.setAction(
            getString(R.string.ok)
        ) { action?.invoke() }
    }.show()
}


fun Fragment.snackBarCustom(msg: String? = "Error", action: (() -> Unit)? = null) {
    Snackbar.make(
        activity!!.findViewById(android.R.id.content),
        msg.toString(),
        Snackbar.LENGTH_LONG
    ).also {
        it.setAction(
            getString(R.string.ok)
        ) { action?.invoke() }
    }.show()
}


fun Context.getDeviceId(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        .uppercase()
}


/**
 * handleApiError method to be reused to handle Api Error incase of an error
 */
fun Fragment.handleApiErrorAndRouteToUssd(
    failure: ResourceNetwork.Failure,
    handleNetworkFailure: (() -> Unit)? = null,
    handleOtherErrors: (() -> Unit)? = null,
) {
    when {
        failure.isNetworkError -> {
            handleNetworkFailure!!.invoke()
        }
        failure.errorCode == 401 -> {
            handleOtherErrors!!.invoke()
        }
        else -> {
            handleOtherErrors!!.invoke()
            val error = failure.errorBody?.string().toString()
            val message = if (error.isNullOrEmpty()) {
                resources.getString(R.string.try_again)
            } else {
                error
            }
            requireView().showSnackBar(message)
        }
    }
}

fun Fragment.handleApiError(
    failure: ResourceNetwork.Failure,
    errMessage: String? = getString(R.string.invalid_credentials),
    retry: (() -> Unit)? = null
) {
    when {
        failure.isNetworkError -> {
            requireView().showSnackBar(
                getString(R.string.check_your_internet_connection),
                retry
            )
        }
        failure.errorCode == 401 -> {
            // requireView().showSnakBar("incorrect details")
            if (this is LoginPinFragment) {
                requireView().showSnackBar(getString(R.string.entered_incorrect_details))
            } else {
                requireView()
                    .showSnackBar(
                        getString(R.string.we_apologise_for_any_inconvenience)
                    )
            }
        }
        failure.errorCode == 400 -> {
            val error = failure.errorString
            val message = StringBuilder()

            error.let {
                try {
                    Timber.e("{}=======1==========> $error")
                    message.append(JSONObject(error).getString("error_description"))
                    Timber.e("{}=======2==========> ${JSONObject(error)}")
                } catch (e: JSONException) {
                }
                message.append("\n")
            }
            Timber.e("{}=======400==========> $message")
            val errorMessage = if (error.isNullOrEmpty()) {
                errMessage
            } else {
                message
            }
            requireActivity().showCargillCustomWarningDialog(
                title = errorMessage.toString(),
                description = errorMessage.toString()
            )
            // basicAlert(errorMessage.toString())
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            val message = if (error.isNullOrEmpty()) {
                resources.getString(R.string.try_again)
            } else {
                error
            }
            requireView().showSnackBar(message)
        }
    }
}

/**
 *SnackBar method to be reused
 */
private fun View.showSnackBar(message: String, action: (() -> Unit)? = null) {
    val sB = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        sB.setAction("Retry") {
            it()
        }
    }
    sB.show()
}


fun getUserIndex(userindex: Int): String {
    var lenght = userindex.toString().length
    return "$lenght$userindex"

}

fun trimAmount(userindex: String): String {
    var lenght = userindex.length
    return "$lenght$userindex"

}

fun getTrimPin(userindex: String): String {
    var lenght = userindex.length
    return "$lenght$userindex"

}

fun trimPhoneNumber(accPhoneNumber: String): String {
    lateinit var phoneNumber: String
    Timber.e("TRIM_PHONE_NUMBER ${accPhoneNumber}")
    if (accPhoneNumber.trim().startsWith("225")) {
        phoneNumber = accPhoneNumber.trim().drop(4)
        Timber.e("TRIM_PHONE_NUMBER with 225 ==== \n ${phoneNumber}")
        return phoneNumber
    } else {
        phoneNumber = accPhoneNumber.trim().drop(1)
        Timber.e("TRIM_PHONE_NUMBER no 225 ==== \n ${phoneNumber}")
        return phoneNumber
    }


}

private var BACKWARDTIMESTAMP: Long = 0

fun comparePassKey(myUnixTimeStamp: Long, passKeyFromFF: String, amount: String): Boolean {
    var result = false
    //val passKeyFromFF = "Eci0IbD3XsMU47I7i5KC9GmN0j+dY3J4uwitMviB/Og="
    //val myUnixTimeStamp: Long = viewModel.unixTimestamp
    for (i in 0L..200L) {
        Timber.e("I == $i")
        BACKWARDTIMESTAMP = myUnixTimeStamp - i
        Timber.e("BACKWARDTIMESTAMP == $BACKWARDTIMESTAMP")
        Timber.e("FARM FORCE PASS KEY IS =========${passKeyFromFF.replace(" ", "+")}")
        if (passKeyFromFF == generateMyPassKey(BACKWARDTIMESTAMP, amount)) {
            Timber.e("GENERATEMYPASSKEY ==${generateMyPassKey(BACKWARDTIMESTAMP, amount)}")
            Timber.e("PAYMENTKEYFROMFF == $passKeyFromFF")
            result = true
            break
        } else {
            Timber.e("DO NOT GENERATEMYPASSKEY == ${generateMyPassKey(BACKWARDTIMESTAMP, amount)}")
            Timber.e("DO NOT PAYMENTKEYFROMFF == $passKeyFromFF")
            continue
        }
    }
    return result
}

fun generateMyPassKey(unixTimeStamp: Long, amount: String): String {
    val data = String.format(
        "%1\$s %2\$s %3\$s %4\$s",
        amount,
        unixTimeStamp,
        "F_318C9F35E09B4",
        "pk_Ecl_09bda23b8e499157d377e0c501c7ce5463479b4e"
    )

    val sha256Hmac = Mac.getInstance("HmacSHA256")

    // CONVENT API KEY AND DATA TO BYTES
    val keyByte =
        "pk_Ecl_09bda23b8e499157d377e0c501c7ce5463479b4e".toByteArray(StandardCharsets.US_ASCII)
    val dataBytes = data.toByteArray(StandardCharsets.US_ASCII)

    // ENCRYPT DATA USING HMACSHA256
    val secretKey = SecretKeySpec(keyByte, "HmacSHA256")
    sha256Hmac.init(secretKey)

    // For base64
    return Base64.getEncoder().encodeToString(sha256Hmac.doFinal(dataBytes))
}

fun addLeadingZeroesToNumber(amount: String, digits: Int = 6): String? {
    var output = amount.toString()
    while (output.length < digits) output = "0$output"
    Timber.e("ADDING ZEROS INFORNT OF AMOUNT ${output}")
    return output
}

