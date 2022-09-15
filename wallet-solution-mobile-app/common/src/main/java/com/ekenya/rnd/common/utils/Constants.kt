package com.ekenya.rnd.common.utils

import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object {
        const val IMAGES_DIR = "FieldApp"
        const val PROFILE_DIR = "FieldApp"



        private var progressDialog: ProgressDialog? = null
        fun callDialog(message: String?, context: Context?) {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setMessage(message)
            progressDialog!!.show()
            val handler = Handler()
            val runnable = Runnable {
                progressDialog?.dismiss()
            }
            progressDialog?.setOnDismissListener(DialogInterface.OnDismissListener {
                handler.removeCallbacks(
                    runnable
                )
            })

            handler.postDelayed(runnable, 1000)
        }

        fun callDialog2(message: String?, context: Context?) {
            Handler(Looper.getMainLooper()).post {
                progressDialog = ProgressDialog(context)
                progressDialog!!.setMessage(message)
                progressDialog!!.show()
            }

        }

        fun cancelDialog() {
            progressDialog!!.dismiss()
        }

    }

}

fun getFileFromInternalStorage(directoryPath: String, name: String, context: Context): File? {
    val contextWrapper = ContextWrapper(context)
    val directory: File = contextWrapper.getDir(directoryPath, Context.MODE_PRIVATE)

    try {
        return File(directory.absolutePath, name)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return null
}

val String.capitalizeWords
    get() = this.lowercase(Locale.getDefault()).split(" ").joinToString(" ") {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

fun isNetworkAvailable(context: Context?): Boolean {
    if (context == null) return false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

fun convertPathToFile(imagePath: String): File {
    return File(imagePath)
}

fun generateImageName(): String {
    val dateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss")
    val timeStamp: String = dateFormat.format(Date())
    return "field_app_$timeStamp.png"
}




fun formatMonthFromPicker(month: Int): String {
    Log.d("TAG", "formatMonthFromPicker: $month")
    lateinit var formattedMonth: String
    when (month.toString()) {
        "0" -> formattedMonth = "JANUARY"
        "1" -> formattedMonth = "JANUARY"
        "2" -> formattedMonth = "FEBRUARY"
        "3" -> formattedMonth = "MARCH"
        "4" -> formattedMonth = "APRIL"
        "5" -> formattedMonth = "MAY"
        "6" -> formattedMonth = "JUNE"
        "7" -> formattedMonth = "JULY"
        "8" -> formattedMonth = "AUGUST"
        "9" -> formattedMonth = "SEPTEMBER"
        "10" -> formattedMonth = "OCTOBER"
        "11" -> formattedMonth = "NOVEMBER"
        "12" -> formattedMonth = "DECEMBER"
    }
    return formattedMonth
}

fun doubleToStringNoDecimal(d: Double): String? {
    val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###")
    return formatter.format(d)
}

fun formatLineChartLabels(date: String): String {
    val formattedDate: Date
    var formattedString = ""
    val format = SimpleDateFormat("yyyy-MM-dd")
    val format2 = SimpleDateFormat("dd-MM")
    try {
        formattedDate = format.parse(date)
        formattedString = format2.format(formattedDate).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedString
}

fun formatScannedDOB(date: String): String {
    val formattedDate: Date
    var formattedString = ""
    val format = SimpleDateFormat("dd.MM.yyyy")
    val format2 = SimpleDateFormat("yyyy-MM-dd")
    try {
        formattedDate = format.parse(date)
        formattedString = format2.format(formattedDate).toString()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return formattedString
}

private var pagerPosition = 0
fun autoPlayAdvertisement(viewPager: ViewPager) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable {
        if (pagerPosition == Objects.requireNonNull(viewPager.adapter)!!.count - 1
        ) {
            pagerPosition = 0
            viewPager.currentItem = pagerPosition
        } else {
            viewPager.currentItem = 1.let {
                pagerPosition += it;
                pagerPosition
            }
        }
        autoPlayAdvertisement(viewPager)
    }, 4000)
}

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun formatDate(date: String): String {
    val originalDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(date)
    return SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH).format(originalDate)
}
