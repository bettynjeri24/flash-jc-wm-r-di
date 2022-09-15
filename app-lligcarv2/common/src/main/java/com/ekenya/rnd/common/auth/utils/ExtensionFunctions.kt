package com.ekenya.rnd.common.auth.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerFragment

fun Activity.toast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message : String){
    Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
}