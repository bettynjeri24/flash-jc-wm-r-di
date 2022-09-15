package com.ekenya.rnd.tmd.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.R

fun Fragment.extendStatusBarBackground() {
    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    requireActivity().window.statusBarColor = Color.TRANSPARENT
    requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
}

fun Fragment.unExtendStatusBarBackground() {
    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    requireActivity().window.statusBarColor = resources.getColor(R.color.colorPrimaryVariant)
    requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

// SetAdapter on a spinner
fun AutoCompleteTextView.setUpSpinner(
    arrayResource: List<String>,
    onItemClick: (
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) -> Unit
) {
    val providerAdapter = ArrayAdapter(
        context,
        com.ekenya.rnd.mycards.R.layout.autocomplete_list_item_cards,
        arrayResource
    )
    setAdapter(providerAdapter)
    setOnFocusChangeListener { v, hasFocus ->
    }
    setOnItemClickListener { parent, view, position, id ->
        onItemClick(parent, view, position, id)
    }
}

fun SharedPreferences.setHasFinishedLanding() {
    this.edit().apply {
        putBoolean("has_finished_login", true)
        apply()
    }
}

fun SharedPreferences.getHasFinishedLanding() =
    this.getBoolean("has_finished_login", false)

fun SharedPreferences.setHasFinishedLogin() {
    this.edit().apply {
        putBoolean("has_finished_login", true)
        apply()
    }
}

fun SharedPreferences.saveAccessToken(token: String) {
    this.edit().apply {
        putString("access_token", token)
        apply()
    }
}

fun SharedPreferences.getAccessToken() =
    this.getString("access_token", "false")

fun SharedPreferences.savePhoneNumber(phoneNumber: String) {
    this.edit().apply {
        putString("phone", phoneNumber)
        apply()
    }
}

fun SharedPreferences.getPhoneNumber() =
    this.getString("phone", "254710102720")

fun SharedPreferences.setUseFingerPrint(boolean: Boolean) {
    this.edit().apply {
        putBoolean("finger", boolean)
        apply()
    }
}

fun SharedPreferences.getUseFingerPrint() =
    this.getBoolean("finger", false)

fun SharedPreferences.setUseFaceAuth(boolean: Boolean) {
    this.edit().apply {
        putBoolean("face", boolean)
        apply()
    }
}

fun SharedPreferences.getUseFace() =
    this.getBoolean("face", false)

fun SharedPreferences.setUseUseVoice(boolean: Boolean) {
    this.edit().apply {
        putBoolean("voice", boolean)
        apply()
    }
}

fun SharedPreferences.getUseVoice() =
    this.getBoolean("voice", false)


fun SharedPreferences.setIsLoggedIn() {
    this.edit().apply {
        putBoolean("logged_in", true)
        apply()
    }
}

fun SharedPreferences.getIsLoggedIn() =
    this.getBoolean("logged_in", false)
