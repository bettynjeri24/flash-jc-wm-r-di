package com.ekenya.rnd.common.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.android.gms.tasks.Task
import timber.log.Timber
import javax.inject.Inject

class GoogleSMSReceiver @Inject constructor() : BroadcastReceiver() {

    var googleSmsBroadcastReceiverListener: SmsBroadcastReceiverListener? = null
    private var otpGoogleReceiver: GoogleOTPReceiveListener? = null
    // var codeLength = 6

    fun googleInitOTPListener(receiver: GoogleOTPReceiveListener) {
        Timber.e("OTPReceiveListener -%s", "Success")
        this.otpGoogleReceiver = receiver
    }

    fun googleTransOTPListener(googleSmsBroadcastReceiverListener: SmsBroadcastReceiverListener) {
        Timber.e("OTPReceiveListener -%s", "Success")
        this.googleSmsBroadcastReceiverListener = googleSmsBroadcastReceiverListener
    }

    override fun onReceive(context: Context?, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status
            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents
                    var message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE)
                    if (message != null) {
                        message = message as String
                        Timber.e("SMS message contents  -%s", message)
                        // Extract one-time code from the message and complete verification
                        // by sending the code back to your server.
                        if (this.otpGoogleReceiver != null) {
                            Timber.e("SMS message contents  -%s", message)
                            extractDigits(message).let {
                                this.otpGoogleReceiver!!.onGoogleOTPReceived(
                                    it
                                )
                            }
                        } else {
                            Timber.e("Ooops otpGoogleReceiver  -%s", "Is very Null")
                        }
                    }
                }
                CommonStatusCodes.TIMEOUT ->
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    if (this.otpGoogleReceiver != null) {
                        // Timber.e("SMS message contents  -%s", message)
                        this.otpGoogleReceiver!!.onGoogleOTPTimeOut("Timed Out Waiting for verification code")
                    }
            }
        } else {
            if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT).also {
                            if (this.googleSmsBroadcastReceiverListener != null) {
                                googleSmsBroadcastReceiverListener!!.onGoogleSuccess(it)
                            } else {
                                Timber.e(
                                    "Ooops googleSmsBroadcastReceiverListener  -%s",
                                    "Is very Null"
                                )
                            }
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> {
                        if (this.googleSmsBroadcastReceiverListener != null) {
                            googleSmsBroadcastReceiverListener?.onGoogleFailure()
                        }
                    }
                }
            }
        }
    }

    interface GoogleOTPReceiveListener {
        fun onGoogleOTPReceived(message: String)
        fun onGoogleOTPTimeOut(timeoutMessage: String)
    }

    /**
     * This method extracts the verification code from a message
     *
     * @param message:         The message where message ought to be extracted
     * @param codeLength: size of the verification code e.g 0100 is 4
     * @return returns the code
     */
    private fun extractDigits(message: String): String {
        val codePattern = "(\\d{6})".toRegex()
        val code: MatchResult? = codePattern.find(message)
        return if (code?.value != null) {
            Timber.e("MatchResult  -%s", "found!")
            code.value
        } else {
            Timber.e("Match not found -%s", "not found!")
            ""
        }
    }

    fun startGoogleSMSRetriever(ctx: Context) {
        val client: SmsRetrieverClient = SmsRetriever.getClient(ctx)
        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task: Task<Void> = client.startSmsRetriever()
        // Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener {
            Timber.e("addOnSuccessListener Message -%s", "Sms listener started!")
        }
        task.addOnFailureListener { e ->
            Timber.e(
                "addOnFailureListener Message -%s",
                "Failed to start sms retriever: ${e.message}"
            )
        }
    }

    interface SmsBroadcastReceiverListener {
        fun onGoogleSuccess(intent: Intent?)
        fun onGoogleFailure()
    }
}
