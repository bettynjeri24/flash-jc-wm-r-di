package com.ekenya.rnd.common.utils.custom

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.telephony.SubscriptionManager
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.R
import com.ekenya.rnd.common.SIMCARDNAMES
import com.ekenya.rnd.common.SIM_MAP
import timber.log.Timber
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private var deviceSessionUUID: String? = null

fun getUTCtime(): String {
    val date = Date()
    /* val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
    val gmt = Date(sdf.format(date))

   val fullDateFormat: DateFormat = DateFormat.getDateTimeInstance(
        DateFormat.FULL,
        DateFormat.FULL
    )*/

    val longDateFormatEN = DateFormat.getDateTimeInstance(
        DateFormat.MEDIUM,
        DateFormat.MEDIUM,
        Locale("FR", "fr")
    )
    var frdate = longDateFormatEN.format(date)
    // var mdateutc = dateToUTC(date)
    return frdate.toString() // gmt
}

fun dateToUTC(): String? {
    val date = Date()
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.timeZone = TimeZone.getTimeZone("UTC")
    val time = calendar.time

    @SuppressLint("SimpleDateFormat")
    val outputFmt =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    outputFmt.timeZone = TimeZone.getTimeZone("UTC")
    return outputFmt.format(time)
}

fun cashFormatter(cash: String): String {
    val number = cash.toInt()
    val COUNTRY = "CI" // CI
    val LANGUAGE = "fr" // fr
    val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
    return str
}

fun deviceSessionUUID(): String {
    return if (deviceSessionUUID.isNullOrEmpty()) UUID.randomUUID().toString()
    else return deviceSessionUUID as String
}

fun Context.getDeviceId(): String {
    return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        .uppercase()
}

fun uuidStringToBigIntPositive2(uuidString: String): BigInteger {
    return BigInteger(uuidString)
}

fun uuidStringFromUuid16(uuid16: String): UUID? {
    // returns a UUID with specified value
    val uuid = UUID.fromString(uuid16)

    val uuid2 = UUID.fromString(
        "5fc03087-d265-11e7-b8c6-83e29cd24f4c"
    )

    println("Node value: " + uuid2.node()) // returns node value

    //        val big: BigInteger = BigInteger(uuid.toString(), 16)
    //        Timber.e("BigInteger $big")
    return uuid
}

fun uuidStringToBigIntPositive(guidString: String?): BigInteger? {
    val g = UUID.fromString(guidString).toString()
    val guidBytes: ByteArray = g.toByteArray()
    Timber.e("GUID BYTES=> $guidBytes")
    // Pad extra 0x00 byte so value is handled as positive integer
    // C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
    // ORIGINAL LINE: var positiveGuidBytes = new byte[guidBytes.Length + 1];
    val positiveGuidBytes = ByteArray(guidBytes.size + 1)
    Timber.e("GUID POSITIVEGUIDBYTES=> $positiveGuidBytes")
    System.arraycopy(guidBytes, 0, positiveGuidBytes, 0, guidBytes.size)
    Timber.e("GUID POSITIVEGUIDBYTES 2=> $positiveGuidBytes")
    return BigInteger(positiveGuidBytes)
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
    if (accPhoneNumber.startsWith("225")) {
        phoneNumber = accPhoneNumber.drop(4)
    } else {
        phoneNumber = accPhoneNumber.drop(1)
    }
    return phoneNumber
}

private var BACKWARDTIMESTAMP: Long = 0

fun comparePassKey(myUnixTimeStamp: Long, passKeyFromFF: String, amount: String): Boolean {
    var result = false
    // val passKeyFromFF = "Eci0IbD3XsMU47I7i5KC9GmN0j+dY3J4uwitMviB/Og="
    // val myUnixTimeStamp: Long = viewModel.unixTimestamp
    for (i in 0L..200L) {
        Timber.e("I == $i")
        BACKWARDTIMESTAMP = myUnixTimeStamp - i
        Timber.e("BACKWARDTIMESTAMP == $BACKWARDTIMESTAMP")
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

fun addLeadingZeroesToNumber(amount: Int, digits: Int = 6): String? {
    var output = amount.toString()
    while (output.length < digits) output = "0$output"
    Timber.e("ADDING ZEROS INFORNT OF AMOUNT $output")
    return output
}

fun Fragment.errorNetworkConnectionFailed(message: String = "ERROR_NETWORK_CONNECTION_FAILED") {
    requireActivity().showCargillCustomWarningDialog(
        title = "ERROR_NETWORK_CONNECTION_FAILED",
        action = {
//            val intent = Intent()
//            intent.putExtra("Message", message)
//            requireActivity().setResult(Activity.RESULT_OK, intent)
//            requireActivity().finish()
        }
    )
}

fun Fragment.successTransactionReceived(message: String = "SUCCESS_TRANSACTION_RECIEVED") {
    requireActivity().showCargillCustomWarningDialog(
        title = getString(R.string.msg_request_was_successful),
        action = {
//            val intent = Intent()
//            intent.putExtra(
//                "Message",
//                message
//            )
//            requireActivity().setResult(Activity.RESULT_OK, intent)
//            requireActivity().finish()
        }
    )
}

fun Fragment.simsDetails() {
    val subscriptionInfo = SubscriptionManager.from(
        requireActivity()
    ).activeSubscriptionInfoList
    // this requires READ_PHONE_STATE permission

    // loop through all info objects and store info we store 2 details here carrier name and subscription Id fro each active SIM card
    // so we need map and an array to set SIM card names to Spinner
    for (subscriptionInfo in subscriptionInfo) {
        SIM_MAP[subscriptionInfo.carrierName.toString()] = subscriptionInfo.subscriptionId
        SIMCARDNAMES!!.add(subscriptionInfo.carrierName.toString())
        Timber.e("SIMCARDNAMES === \n$SIMCARDNAMES")
    }
}
