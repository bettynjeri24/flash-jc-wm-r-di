package com.ekenya.lamparam.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class MySMSBroadcastReceiver: BroadcastReceiver() {

    var otpReceiveInterface: OtpReceivedInterface? = null

    fun setOnOtpListeners(otpReceiveInterface: OtpReceivedInterface) {
        this.otpReceiveInterface = otpReceiveInterface
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String // Get SMS message contents
                    if (otpReceiveInterface != null) { // Extract one-time code from the message and complete verification
                        val otp: String = extractDigits(message) // define this function
                        otpReceiveInterface?.onOtpReceived(otp)
                    }
                }
                CommonStatusCodes.TIMEOUT ->                     // Waiting for SMS timed out (5 minutes)
                    if (otpReceiveInterface != null) { // Handle the error ...
                        otpReceiveInterface?.onOtpTimeout()
                    }
                CommonStatusCodes.API_NOT_CONNECTED -> if (otpReceiveInterface != null) {
                    otpReceiveInterface?.onOTPReceivedError("API NOT CONNECTED")
                }
                CommonStatusCodes.NETWORK_ERROR -> if (otpReceiveInterface != null) {
                    otpReceiveInterface?.onOTPReceivedError("NETWORK ERROR")
                }
                CommonStatusCodes.ERROR -> if (otpReceiveInterface != null) {
                    otpReceiveInterface?.onOTPReceivedError("ERROR READING SMS")
                }
                CommonStatusCodes.SIGN_IN_REQUIRED -> if (otpReceiveInterface != null) {
                    otpReceiveInterface?.onOTPReceivedError("SIGN IN TO GOOGLE ACCOUNT FIRST")
                }
                CommonStatusCodes.INVALID_ACCOUNT -> if (otpReceiveInterface != null) {
                    otpReceiveInterface?.onOTPReceivedError("YOUR ACCOUNT IS INVALID")
                }
                else -> otpReceiveInterface?.onOTPReceivedError("Unknown Error occurred")
            }
        }
    }

    /**
     * This method extracts the verification code from a message
     * @param in: The message where message ought to be extracted
     * @param codeLength: size of the verification code e.g 0100 is 4
     * @return returns the code
     */
    private fun extractDigits(`in`: String): String {
        val p = Pattern.compile("(\\d{4})")
        val m = p.matcher(`in`)
        return if (m.find()) {
            m.group(0)
        } else ""
    }
}

/**
 * Created on : May 21, 2019
 * Author     : Lennox Brown
 */
interface OtpReceivedInterface {
    fun onOtpReceived(otp: String)
    fun onOtpTimeout()
    fun onOTPReceivedError(error: String?)
}

