package com.ekenya.rnd.tmd.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
class SMSBroadcastReceiver : BroadcastReceiver() {

    private var otpListener: SmsBroadcastReceiverListener? = null

    fun setOTPListener(otpListener: SmsBroadcastReceiverListener?) {
        this.otpListener = otpListener
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == SmsRetriever.SMS_RETRIEVED_ACTION) {

            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when (smsRetrieverStatus.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT).also {
                        otpListener?.onSuccess(it)
                    }
                }

                CommonStatusCodes.TIMEOUT -> {
                    otpListener?.onFailure()
                }
            }
        }
    }

    interface SmsBroadcastReceiverListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()
    }
}