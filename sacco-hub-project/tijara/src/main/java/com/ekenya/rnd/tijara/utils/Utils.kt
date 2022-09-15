package com.ekenya.rnd.tijara.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ekenya.rnd.common.Constants
import com.ekenya.rnd.tijara.R
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.lang.NumberFormatException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object PrefUtils {
    private const val PREFS_NAME = "Sacco Hub"
    const val PREF_JTOKEN= "token"
    /**load image */

    /*****set/store shared preferences  */
    fun setPreference(con: Context, key: String?, value: String?) {
        val preferences = con.getSharedPreferences(PREFS_NAME, 0)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }



    /*** get/retrieve shared preferences   */
    fun getPreferences(con: Context, key: String?): String? {
        val sharedPreferences = con.getSharedPreferences(PREFS_NAME, 0)
        return sharedPreferences.getString(key, "0")
    }

    fun clear(con: Context) {
        val sharedPrefs = con.getSharedPreferences(PREFS_NAME, 0)
        val editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
    }

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

        fun toastySuccess(context:Context,msg:String){
            Toasty.success(context,msg,Toasty.LENGTH_SHORT,true).show()
        }

        fun toastyError(context:Context,msg:String) {
            Toasty.error(context,msg,Toast.LENGTH_LONG,true).show()
        }
        fun toastyInfo(context:Context,msg:String) {
            Toasty.info(context,msg,Toast.LENGTH_LONG,true).show()
        }


fun showSuccessSnackBar(context: View, message: String) {
    Snackbar.make(context,message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(context.resources.getColor(R.color.main_green_color)).show()
}
fun showErrorSnackBar(context: View,message:String) {
    Snackbar.make(context,message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(context.resources.getColor(R.color.buttonColor)).show()
}
fun showToast(context: Context,message: String){
    Toast.makeText(context,message,Toast.LENGTH_LONG)
        .show()
}
/**
 * Hide keyboard in activity or fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
private var pagerPosition = 0
fun autoPlayAdvertisement(viewPager: ViewPager) {
    Handler().postDelayed(Runnable {
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
fun uniqueFileName(): String {
    val randomNum = (0..1000000).random()
    val date = Date()
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmssSSSSSS", Locale.ENGLISH)
    return "${dateFormat.format(date)}_$randomNum"
}
fun createMultipartRequestBody(file: File, name: String, mediaType: String): MultipartBody.Part {
    val requestFile = file.asRequestBody(mediaType.toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, file.name, requestFile)
}
fun createRequestBody(value: Any): RequestBody = value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
fun getRealPathFromURIPath(contentURI: Uri, activity: Activity): String {
    val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
    val realPath: String?
    realPath = if (cursor == null) {
        contentURI.path
    } else {
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        cursor.getString(idx)
    }
    cursor?.close()

    return realPath!!
}


fun isNetwork(context: Context): Boolean {
    val service = Context.CONNECTIVITY_SERVICE
    val manager = context.getSystemService(service) as ConnectivityManager?
    val network = manager?.activeNetworkInfo
    Timber.d("hasNetworkAvailable: ${(network != null)}")
    return (network?.isConnected) ?: false
}
/**
 * Failed transaction request dialog
 * @param context
 * @param responseMessage
 */
fun onNoNetworkDialog(context: Context?) {
    val sweetAlertDialog=SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            sweetAlertDialog.setContentText(context!!.getString(R.string.no_network_connection))
        .setCustomImage(R.drawable.ic_connection_error)
        .setConfirmClickListener { obj: Dialog -> obj.dismiss() }
        .show()
    sweetAlertDialog.setCancelable(false)


}
/**
 * Failed transaction request dialog
 * @param context
 * @param responseMessage
 */
fun waringAlertDialog(context: Context?,title:String,message: String){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
            .setCancelable(false)
}
fun waringAlertDialogUp(context: Context?,view:View,title:String,message: String){
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                view.findNavController().navigateUp()

            })
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show()
        .setCancelable(false)
}
fun onInfoDialog(context: Context?, responseMessage: String?) {
    val sweetd=SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
    sweetd
        .setContentText(responseMessage)
        .setConfirmClickListener { obj: Dialog -> obj.dismiss() }
        .show()
    sweetd.setCancelable(false)
}
fun onInfoDialog2(context: Context?, responseMessage: String?) {
    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
        .setContentText(responseMessage)
        .setConfirmClickListener { obj: Dialog ->
            obj.dismiss()
        }
        .show()
}
fun onProgressDialog(context: Context, responseMessage: String?) {
    SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        .setContentText(responseMessage)
        .showCancelButton(false)
        .setConfirmClickListener { obj: Dialog -> obj.dismiss() }
        .show()
}
fun dialogProgress(context: Context?,message: String) {
    SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        .setContentText("responseMessage")
        .showCancelButton(false)
        .setCancelClickListener(null)
        .setConfirmClickListener(null)
        .show()
}

fun camelCase(stringToConvert: String): String {
    if (TextUtils.isEmpty(stringToConvert)) {
        return ""
    }
    return Character.toUpperCase(stringToConvert[0]) +
            stringToConvert.substring(1).toLowerCase(Locale.US)
}
fun formatRequestTime( time: String?):String {
    return when {
        time!=null -> {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US)//
            val dateFormat = format.parse(time)
            val weekdayString =
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(dateFormat!!)
            weekdayString.toString()

        }
        else -> {
            ""
        }
    }
}
fun currentTimeMin( time: String?):String {
    return when {
       /* time!=null -> {
            // val date=Calenda*/
        time!=null -> {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US)//
            val dateFormat = format.parse(time)
            val weekdayString =
                SimpleDateFormat("mm", Locale.getDefault()).format(dateFormat!!)
            weekdayString.toString()
        }
        else -> {
            ""
        }
    }
}

fun Fragment.pickFromGallery(GALLERY_REQUEST_CODE:Int) {
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    intent.type = "image/*"
    val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivityForResult(intent, GALLERY_REQUEST_CODE)
}
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}
fun View.makeGone(){
    visibility=View.GONE
}
fun View.makeVisible(){
    visibility=View.VISIBLE
}
fun getInitials(string: String): String {
    val separated = string.trim().split(" ")
    if (separated.size > 1) {
        return separated.joinToString("") {
            it.first().toString()
        }
    }
    if (separated.first().length > 2) {
        return separated.first().substring(0, 2)
    }
    return string

}

