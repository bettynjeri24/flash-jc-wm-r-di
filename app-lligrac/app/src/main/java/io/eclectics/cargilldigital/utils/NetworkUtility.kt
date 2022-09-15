package io.eclectics.cargill.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.eclectics.cargilldigital.MainActivity
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class NetworkUtility {

    fun sendRequest(pDialog: SweetAlertDialog) {

        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Processing .."
        pDialog.setCancelable(false)
        pDialog.show()
    }

    fun transactionWarning(
        message: String,
        activity: FragmentActivity
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        progressDialog.apply {
            titleText = "Warning"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    // it?.findNavController()?.navigate(destination)
                    //it?.findNavController()?.popBackStack(destination,false)
                }
            }
            setCancelable(false)

            show()

        }
    }

    fun transactionPopup(
        it: View?,
        message: String,
        activity: FragmentActivity

    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    // it?.findNavController()?.navigate(destination)
                    it?.findNavController()?.popBackStack()
                }
            }
            setCancelable(false)

            show()

        }


    }

    fun confirmTransactionEnd(
        it: View?,
        message: String,
        activity: FragmentActivity,
        destination: Int
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    it?.findNavController()?.navigate(destination)
                    // it?.findNavController()?.popBackStack(destination,false)
                }
            }
            setCancelable(false)

            show()

        }
    }


    fun confirmTransactionEndNav(
        it: View?,
        message: String,
        activity: FragmentActivity,
        destination: Int,
        bundle: Bundle
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    it?.findNavController()?.navigate(destination, bundle)
                    // it?.findNavController()?.popBackStack(destination,false)
                }
            }
            setCancelable(false)

            show()

        }
    }

    fun confirmAndLogout(
        it: View?,
        message: String,
        activity: FragmentActivity,
        destination: Int,
        bundle: Bundle
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    (activity as MainActivity).profileLogout()
                    // it?.findNavController()?.navigate(destination,bundle)
                    // it?.findNavController()?.popBackStack(destination,false)
                }
            }
            setCancelable(false)

            show()

        }
    }

    fun confirmPinRequestResponse(
        it: View?,
        message: String,
        activity: FragmentActivity
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = message

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    //  it?.findNavController()?.navigate(destination,bundle)
                    // it?.findNavController()?.popBackStack(destination,false)
                    (activity as MainActivity?)!!.navigationMgmt()
                }
            }
            setCancelable(false)

            show()

        }
    }

    companion object {
        lateinit var gson: Gson
        var baseUrl = "http://102.37.14.127:5000/"

        @JvmStatic
        fun getJsonParser(): Gson {
            gson = Gson()
            if (gson == null) {
                val builder = GsonBuilder().serializeNulls()
                gson = builder.create()
            }
            return gson
        }

        inline fun <reified T> jsonResponse(json: String): T {
            return getJsonParser().fromJson(json, object : TypeToken<T>() {}.type)
        }

        //WHEN DATABASE IS INACCESSIBLE IT RETURNS A EMPTY ARRAY BLOCK AND NO MESSAGE RESPONSE DISPLAY THIS MSG
        fun emptyResponseMsg(statusCode: String): String {
            return "An unexpected network error occurred c$statusCode, try again later"
        }

    }

    fun getCurrentTime(): String {
        var transactionDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        var transTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        var time = "${transactionDate}T12:27:51.135Z"//T19:27:51.135Z
        return time
    }

    fun transtime(): String {
        var transactionDate = SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("yyyy-MMMM-dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        var date = formatter.parse("2022-03-31T14:35:42.000")
        // var transTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        /*val firstApiFormat = DateTimeFormatter.for("yyyy-MM-dd HH:mm:ss")
        val date = LocalDate.parse("2019-08-07 09:00:00" , firstApiFormat)*/
        //  val result = formatter.parse(transactionDate)

        var st = formatter.format(date)
        // var time = transTime.format(date)
        //var inst = Instant.now()
        return "$st - "//$time
    }

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
            DateFormat.MEDIUM, Locale("FR", "fr")
        )
        var frdate = longDateFormatEN.format(date)
        // var mdateutc = dateToUTC(date)
        return frdate.toString()//gmt
    }

    fun dateToUTC(): String? {
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        val time = calendar.time
        @SuppressLint("SimpleDateFormat") val outputFmt =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        outputFmt.timeZone = TimeZone.getTimeZone("UTC")
        return outputFmt.format(time)
    }

    fun cashFormatter(cash: String): String {
        val number = cash.toInt()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        return str
    }

    fun doubleCashFormatter(cash: String): String {
        val number = cash.toDouble()
        val COUNTRY = "CI"//CI
        val LANGUAGE = "fr"//fr
        val str = NumberFormat.getCurrencyInstance(Locale(LANGUAGE, COUNTRY)).format(number)
        return str
    }

    fun paymerchant(
        it: View?,
        activity: FragmentActivity
    ) {

        val progressDialog = SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
        progressDialog.apply {
            titleText = "Success"
            contentText = "Your payment transaction processed successfully"

            setConfirmClickListener { SweetAlertDialog ->
                run {
                    SweetAlertDialog.dismiss()
                    SweetAlertDialog.dismissWithAnimation()
                    var bundle = Bundle()
                    bundle.putString("menu", "paybill")
                    //currentFragment!!.findNavController().navigate(R.id.farmerHomeFragment,bundle, null)
                    //it?.findNavController()?.navigate(R.id.farmerHomeFragment,bundle, null)
                    //it?.findNavController()?.popBackStack(destination,false)
                }
            }
            setCancelable(false)

            show()

        }
    }
}