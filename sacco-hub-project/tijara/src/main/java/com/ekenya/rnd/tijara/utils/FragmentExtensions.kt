package com.ekenya.rnd.tijara.utils

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ekenya.rnd.baseapp.BaseApp
import com.ekenya.rnd.tijara.R
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*


fun Fragment.toastySuccess(msg:String){
    Toasty.success(this.requireContext(),msg,Toasty.LENGTH_SHORT,true).show()
}

fun Fragment.toastyErrors(msg:String) {
    Toasty.error(this.requireContext(),msg, Toast.LENGTH_LONG,true).show()
}
fun Fragment.toastyInfos(msg:String) {
    Toasty
        .info(this.requireContext(),msg, Toast.LENGTH_LONG,true)
        .show()
}

fun TextView.formatDateName(date:String){
    date?.let {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)//
        val dateFormat = format.parse(date)
        val weekdayString =
            SimpleDateFormat("MMM d, yyyy ", Locale.getDefault()).format(dateFormat)
        this.text = weekdayString.toString()
    }
}
fun getGreetings(): String? {
    val date = Date()
    val cal = Calendar.getInstance()
    cal.time = date
    val hour = cal[Calendar.HOUR_OF_DAY]
    var greeting: String?
    greeting = if (hour in 12..16) {
        BaseApp.mresource.getString(R.string.afternoon)
    } else if (hour in 17..20) {
        BaseApp.mresource.getString(R.string.evening)
    } else if (hour in 21..23) {
        BaseApp.mresource.getString(R.string.night)
    } else {
        BaseApp.mresource.getString(R.string.morning)
    }
    return greeting
}
/*
fun TextView.formatNumberComma(number:String){
    val formattedAvailableBalance = StringBuilder()
    val formatter2 = Formatter(formattedAvailableBalance, Locale.forLanguageTag("KSH"))
    val text=formatter2.format(" %(,.2f", number)
    this.text=text
}*/
