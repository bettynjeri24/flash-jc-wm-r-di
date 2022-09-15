package com.ekenya.rnd.common.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.api.CommonStatusCodes
import com.huawei.hms.support.api.client.Status
import com.huawei.hms.support.sms.ReadSmsManager
import com.huawei.hms.support.sms.common.ReadSmsConstant
import timber.log.Timber
import javax.inject.Inject


class HuaweiSMSReceiver @Inject constructor() : BroadcastReceiver() {

    private var isRegistered = false

    fun huaweiSmsRegister(context: Context) {
        if (!isRegistered) {
            Timber.e(" going to register this broadcast receiver")
            context.registerReceiver(this, IntentFilter(ReadSmsConstant.READ_SMS_BROADCAST_ACTION))
            isRegistered = true
        } else {
            Timber.e(" this broadcast receiver is already registered ")
        }
    }

    fun huaweiSmsUnRegister(context: Context) {
        if (isRegistered) {
            Timber.e(" going to unregister this broadcast receiver")
            context.unregisterReceiver(this)
            isRegistered = false
        }
    }

    private var otpHuaweiReceiver: OTPHuaweiReceiveListener? = null
    //var codeLength = 6

    fun initHuaweiOTPListener(receiver: OTPHuaweiReceiveListener) {
        Timber.e("initHuaweiOTPListener -%s", "Success")
        this.otpHuaweiReceiver = receiver
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.e("onReceive Called")
        intent?.let { it ->
            val bundle = it.extras
            bundle?.let { itBundle ->
                if (ReadSmsConstant.READ_SMS_BROADCAST_ACTION == it.action) {
                    val status: Status? = itBundle.getParcelable(ReadSmsConstant.EXTRA_STATUS)
                    if (status?.statusCode == CommonStatusCodes.TIMEOUT) {
                        // The service has timed out and no SMS message that meets the requirements is read. The service process ends.
                        if (this.otpHuaweiReceiver != null) {
                            Timber.e("++++++++SMS message contents TIMEOUT+++++++++")
                            this.otpHuaweiReceiver!!.onHuaweiOTPTimeOut("Timed Out Waiting for verification code")
                        }
                    } else if (status?.statusCode == CommonStatusCodes.SUCCESS) {
                        if (bundle.containsKey(ReadSmsConstant.EXTRA_SMS_MESSAGE)) {
                            // An SMS message that meets the requirement is read. The service process ends.
                            val message = bundle.getString(ReadSmsConstant.EXTRA_SMS_MESSAGE)
                            if (message != null) {
                                Timber.e("SMS message contents  -%s", message)
                                // Extract one-time code from the message and complete verification
                                // by sending the code back to your server.
                                if (this.otpHuaweiReceiver != null) {
                                    Timber.e("SMS message contents  -%s", message)
                                    extractDigits(message).let {
                                        this.otpHuaweiReceiver!!.onHuaweiOTPReceived(
                                            it
                                        )
                                    }
                                } else {
                                    Timber.e("Ooops otpReceiver  -%s", "Is very Null")
                                }
                            }
                        }
                    } else if (status?.statusCode == ReadSmsConstant.FAIL) {
                        Timber.e("ReadSmsConstant  -%s", "FAIL")
                    }
                }
            }
        }
    }

    fun startHuaweiSmsRetriever(context: Context?) {
        val task: Task<Void> = ReadSmsManager.start(context)
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // The service is enabled successfully. Perform other operations as needed.
                //                    doSomethingWhenTaskSuccess();
                Timber.e("startHuaweiSmsRetriever: isSuccessful")
            } else {
                //task false
                Timber.e("startHuaweiSmsRetriever: failed")
            }
        }
    }

    interface OTPHuaweiReceiveListener {
        fun onHuaweiOTPReceived(otp: String?)
        fun onHuaweiOTPTimeOut(timeoutMessage: String)
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
}