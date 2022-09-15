package com.ekenya.rnd.dashboard.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.onboarding.R
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


fun Activity.changeStatusBarColor(color: Int) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color
}
fun xywz(){}

fun Activity.setStatusBArColor() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        statusBarColor = Color.TRANSPARENT
    }

}

fun Fragment.changeStatusBarColor(color: Int) {
    requireActivity().window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = color
    }
}

//encrypt payload return payload encrypted in

fun Fragment.getDateFromDialog(v: EditText) {
    val cal = Calendar.getInstance()
    var date = ""
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat2 = "ddMMyyyy" // mention the format you need
            val myFormat = "dd,MMMM yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
            v.setText(sdf.format(cal.time).toString())
            SharedPreferencesManager.setDateOfBirth(requireContext(), true)
            SharedPreferencesManager.setDob( requireContext(),sdf2.format(cal.time).toString())

        }


    v.setOnClickListener {
        context?.let { it1 ->
            DatePickerDialog(
                it1,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR) - 18,
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }
    }
}

fun Fragment.showErrorDialog() {
    val dialog = Dialog(requireContext())
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.error_dialog)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val dialogBtn_manual = dialog.findViewById<Button>(R.id.btn_manualverification)
    val dialogBtn_remove = dialog.findViewById<Button>(R.id.btn_scanID)
    val closeButton = dialog.findViewById<ImageView>(R.id.btn_dismissDialog)
    closeButton.setOnClickListener {
        dialog.dismiss()
    }
    dialogBtn_remove.setOnClickListener {
        //activity!!.finish()
        //requestCameraPermission()
        dialog.dismiss()

    }
    dialogBtn_manual.setOnClickListener {
        // Navigation.findNavController(binding.appbarlayout).navigate(R.id.action_verifyIdentityFragment_to_manualVerificationFragment)
        dialog.dismiss()

    }
    dialog.show()
}

fun Fragment.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        requireActivity().window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}


fun Fragment.hideSupportActionBar() {
    (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    val layoutparams = view!!.layoutParams as FrameLayout.LayoutParams
    layoutparams.updateMargins(top = 0)
}

fun Fragment.removeActionbarTitle() {
    (requireActivity() as AppCompatActivity).supportActionBar?.title = ""
}

fun Fragment.removeActionBarElevation() {


    (requireActivity() as AppCompatActivity).supportActionBar!!.elevation = 0F;
}

fun Fragment.changeActionbarColor(color: Int) {
    (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            color
        )
    )
}

fun Fragment.makeActionBarTransparent() {
    (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            Color.TRANSPARENT
        )
    )
}

fun Fragment.showSupportActionBar() {
    (requireActivity() as AppCompatActivity).supportActionBar?.show()
    (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            Color.parseColor("#FFFFFF")
        )
    )
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    var statusBarHeight = 0
    if (resourceId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    val layoutparams = view!!.layoutParams as FrameLayout.LayoutParams
    layoutparams.updateMargins(top = statusBarHeight)

}

fun Fragment.extendStatusBarBackground() {
    if (Build.VERSION.SDK_INT >= 19) {
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= 21) {
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window.statusBarColor = Color.TRANSPARENT
    }
}


fun Fragment.ChangeActionBarandBackArrowColor(color: String) {
    requireActivity().changeStatusBarColor(Color.parseColor(color))
    (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            Color.parseColor("#100fd6")
        )
    )
    (requireActivity() as AppCompatActivity).supportActionBar?.setBackgroundDrawable(
        ColorDrawable(
            Color.parseColor("#100fd6")
        )
    )

    val upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
    (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(upArrow)
}

fun Fragment.showErrorSnackBar(text: String) {
    if (text.isNotBlank()) {
        val snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
        val customSnackView: View = layoutInflater.inflate(R.layout.error_snack_bar_layout, null)

        snackbar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        val error: TextView = customSnackView.findViewById(R.id.snackbar_text)
        val close: ImageView = customSnackView.findViewById(R.id.snackbar_closeIcon)


        error.text = text
        close.setOnClickListener {
            snackbar.dismiss()
        }


        snackbarLayout.setPadding(-16, 0, -16, 16)
        snackbarLayout.addView(customSnackView, 0);



        snackbar.show();
    }
}

fun Fragment.showSuccessSnackBar(text: String) {
    val snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
    val customSnackView: View = layoutInflater.inflate(R.layout.success_snackbar, null)

    snackbar.view.setBackgroundColor(Color.TRANSPARENT)

    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    val error: TextView = customSnackView.findViewById(R.id.snackbar_text)
    val close: ImageView = customSnackView.findViewById(R.id.snackbar_closeIcon)


    error.text = text
    close.setOnClickListener {
        snackbar.dismiss()
    }


    snackbarLayout.setPadding(-16, 0, -16, 16)
    snackbarLayout.addView(customSnackView, 0);



    snackbar.show();
}

fun getDeviceId(context: Context): String? {
    val deviceId: String
    deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID
        )
    } else {
        val mTelephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (mTelephony.deviceId != null) {
            mTelephony.deviceId
        } else {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }
    }
    return deviceId
}

fun blurPhoneNumber(context: Context): String {


    var phoneNumber = SharedPreferencesManager.getPhoneNumber(context)!!

    val char = '*'
    val index = 6
    if (phoneNumber.isNotBlank()) {
        phoneNumber = phoneNumber.substring(
            0,
            index
        ) + char + char + char + char + phoneNumber.substring(index + 4)


    }
    return phoneNumber
}

fun blurCardNumber(context: Context): String {
    var cardNumber = SharedPreferencesManager.getCardNumber(context)!!

    val char = "  ****  ****  "
    val index = 4

    cardNumber = cardNumber.substring(
        0,
        index
    ) + char + cardNumber.substring(index + 8)

    return cardNumber


}


fun View.makeRadioButtonActive() {

    background = context?.let { ContextCompat.getDrawable(it, R.drawable.activestepsbackground) }

}

fun TextView.makeInactivePaymentOption() {
    setTextColor(Color.parseColor("#6f7070"))

}
fun View.makeRadioButtonInActive() {

    background = context?.let { ContextCompat.getDrawable(it, R.drawable.inactive_pin_bg) }

}



fun TextView.makeActivePaymentOption() {

    setTextColor(Color.parseColor("#ffee1a23"))

}

fun Fragment.makeActiveAirtimeOption(activeOption: Button, inactiveOption: Button) {


    activeOption.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.bg_blue);
    activeOption.setTextColor(Color.WHITE)
    inactiveOption.backgroundTintList =
        ContextCompat.getColorStateList(context!!, R.color.btn_grey);
    inactiveOption.setTextColor(Color.BLACK)


}

fun Fragment.makeActiveBillOption(
    active: TextView,
    inactive1: TextView,
    inactive2: TextView,
    inactive3: TextView,
) {
    active.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.bg_blue);
    active.setTextColor(Color.WHITE)

    inactive1.makeInActiveBillOption()
    inactive2.makeInActiveBillOption()
    inactive3.makeInActiveBillOption()
}

fun TextView.makeInActiveBillOption() {
    backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.btn_grey);
    setTextColor(Color.BLACK)
}


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun Fragment.convertBitmapToFile(fileName: String, bitmap: Bitmap, con: Context): File {
    val file = File(con.cacheDir, fileName)
    file.createNewFile()
    //Convert bitmap to byte array
    val bos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
    val bitMapData = bos.toByteArray()
    //write the bytes in file
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}








