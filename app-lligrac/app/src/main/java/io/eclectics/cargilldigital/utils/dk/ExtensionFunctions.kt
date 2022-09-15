package io.eclectics.cargilldigital.utils.dk

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import io.eclectics.cargilldigital.R
import timber.log.Timber
import java.util.*

fun Activity.toast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message : String){
    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
}

fun Context.greetingsDependingOnTimeOfDay(): String {
    // Get the time of day

    val date = Date()
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(date)
    val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
    Timber.e("GREETEINGS DEEPMDING WITH TIME $hour")
    // Set greeting
    var greeting: String? = null
    when (hour) {
        in 12..16 -> {
            greeting = getString(R.string.greetings_afternoon)
        }
        in 17..20 -> {
            greeting = getString(R.string.greetings_evening)
        }
        in 21..23 -> {
            greeting = getString(R.string.greetings_night)
        }
        else -> {
            greeting = getString(R.string.greetings_morning)
        }
    }
    return greeting
}

fun timber(message: String) {
    Timber.e("{{{{{{{{{{{{{{{{{{{{{{{ $message")
}