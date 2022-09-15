package com.ekenya.rnd.common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

object Utils {
     fun isEmailValid(rawEmailAddress : String): Boolean{
        val  emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

        return rawEmailAddress.matches(emailPattern)
    }




}
fun Fragment.toastMessage(message: String?){
    if (!message.isNullOrBlank())
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()

}