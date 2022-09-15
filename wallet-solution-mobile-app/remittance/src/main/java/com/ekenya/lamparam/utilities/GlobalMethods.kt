package com.ekenya.lamparam.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Base64
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2KtResult
import com.lambdapioneer.argon2kt.Argon2Mode
import com.lambdapioneer.argon2kt.Argon2Version
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


/**
 * Permissions that need to be explicitly requested from end user.
 */
private val REQUIRED_SDK_PERMISSIONS = arrayOf(Manifest.permission.READ_PHONE_STATE)

/**
 * permissions request code
 */
private const val REQUEST_CODE_ASK_PERMISSIONS = 1

class GlobalMethods @Inject constructor(context: Context) {

    private lateinit var progressDialog: SweetAlertDialog

    fun hashPwd(pwd: String): String {
        val argon2Kt = Argon2Kt()
        val hashResult: Argon2KtResult = argon2Kt.hash(
            parallelism = 1,
            version = Argon2Version.V13,
            mode = Argon2Mode.ARGON2_ID,
            password = pwd.toByteArray(),
            salt = "secretsecret".toByteArray(),
            tCostInIterations = 3,
            mCostInKibibyte = 2048,
            hashLengthInBytes = 32
        )
        val hashedPwd = Base64.encodeToString(hashResult.rawHashAsByteArray(), Base64.DEFAULT)
        Timber.d("Base64 %s", hashedPwd)
        return hashedPwd.dropLast(2)
    }

    /**
     * Send a transaction warning message
     */
    fun transactionWarning(activity: FragmentActivity, message: String) {
        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning!"
            contentText = message
            setCancelable(false)
            setConfirmClickListener { SweetAlertDialog ->
                run { SweetAlertDialog.dismiss() }
            }
            show()
        }
    }

    /**
     * Send a transaction warning message
     */
    fun transactionError(activity: FragmentActivity, message: String) {
        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
        progressDialog.apply {
            titleText = "Error!"
            contentText = message
            setCancelable(false)
            setConfirmClickListener { SweetAlertDialog -> run { SweetAlertDialog.dismiss() } }
            show()
        }
    }

    /**
     * Confirms that a transaction has ended
     */
    fun confirmTransactionEnd(
        activity: FragmentActivity,
        message: String,
        okListener: SweetAlertDialog.OnSweetClickListener
    ) {
        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message
            setCancelable(false)
            setConfirmButton("Proceed", okListener)
            show()
        }
    }

    /**
     * Shows a message for okay or canceling
     */
    fun showMessageOKCancel(
        activity: FragmentActivity,
        message: String,
        okListener: SweetAlertDialog.OnSweetClickListener
    ) {
        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning!"
            contentText = message
            setConfirmButton("OK", okListener)
            setCancelButton("Cancel", null)
            setCancelable(false)
//            setCancelClickListener { SweetAlertDialog -> run { SweetAlertDialog.dismiss() } }
//            setConfirmClickListener { SweetAlertDialog -> run { SweetAlertDialog.dismiss() } }
            show()
        }

    }

    /**
     * Shows loading dialog
     */
    fun loader(activity: FragmentActivity, i: Int) {
        if (i == 0) {
            progressDialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
            progressDialog.apply {
                titleText = "Sending request"
                setCancelable(false)
                show()
            }
        } else {
            if (progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
    }

    fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }

    fun getBigSizeSpanned(text: String): String {
        return "<b>$text</b>"
    }

    fun getSmallSizeSpanned(text: String): String {
        return "<s>$text</s>"
    }

    /**
     * Returns the device ID, IMEI
     */
    fun getDeviceId(activity: FragmentActivity): String {
        val missingPermissions: MutableList<String> = ArrayList()
        // check all required dynamic permissions
        var imei = ""

        for (permission in REQUIRED_SDK_PERMISSIONS) {
            val result = activity.checkSelfPermission(permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission)
            }
        }

        if (missingPermissions.isNotEmpty()) {
            // request all missing permissions
            val permissions = missingPermissions.toTypedArray()
            ActivityCompat.requestPermissions(
                activity as Activity,
                permissions,
                REQUEST_CODE_ASK_PERMISSIONS
            )
        } else {
            val grantResults = IntArray(REQUIRED_SDK_PERMISSIONS.size)
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED)
            val telephonyManager = activity.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            imei = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        if (telephonyManager.phoneCount >= 2) {
                            val id1 = telephonyManager.getImei(0)
                            if (id1 != null) {
                                if (id1.contains("/")) {
                                    telephonyManager.getImei(0).split("/")
                                        .toTypedArray()[0].trim { it <= ' ' }
                                    // Constants.setDeviceIMEI2(telephonyManager.getImei(1).split("/")[0].trim());
                                } else {
                                    telephonyManager.getImei(0)
                                    //Constants.setDeviceIMEI2(telephonyManager.getImei(1));
                                }
                            } else {
                                val androidId: String = Settings.Secure.getString(
                                    activity.contentResolver,
                                    Settings.Secure.ANDROID_ID
                                )
                                androidId.uppercase(Locale.getDefault())
                            }
                        } else {
                            val id1 = telephonyManager.imei
                            if (id1.contains("/")) {
                                telephonyManager.imei.split("/")
                                    .toTypedArray()[0].trim { it <= ' ' }
                            } else {
                                telephonyManager.imei
                            }
                        }
                    } else {
                        if (telephonyManager.phoneCount >= 2) {
                            val id1 = telephonyManager.getDeviceId(0)
                            if (id1.contains("/")) {
                                telephonyManager.getDeviceId(0).split("/")
                                    .toTypedArray()[0].trim { it <= ' ' }
                                //Constants.setDeviceIMEI2(telephonyManager.getDeviceId(1).split("/")[0].trim());
                            } else {
                                telephonyManager.getDeviceId(0)
                                // Constants.setDeviceIMEI2(telephonyManager.getDeviceId(1));
                            }
                        } else {
                            val id1 = telephonyManager.getDeviceId(0)
                            if (id1.contains("/")) {
                                telephonyManager.deviceId.split("/")
                                    .toTypedArray()[0].trim { it <= ' ' }
                            } else {
                                telephonyManager.getDeviceId(0)
                            }
                        }
                    }
                } else {
                    // String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    //  imei = androidId.toUpperCase();
                    telephonyManager.deviceId.split("/").toTypedArray()[0].trim { it <= ' ' }
                }
            } else {
                val androidId: String =
                    Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
                androidId.uppercase(Locale.getDefault())
            }
        }
        return imei
    }

    /**
     * Creates and returns randomly generated 10 character string
     */
    fun transactionReference(): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..10)
            .map { allowedChars.random() }
            .joinToString("")
    }

    /**
     * Returns the current datetime
     */
    fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return df.format(c.time)
    }
}