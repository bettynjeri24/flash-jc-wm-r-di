package com.ekenya.rnd.common.utils.custom

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.ekenya.rnd.common.CAPTURE_CAMERA
import com.ekenya.rnd.common.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonElement
import es.dmoral.toasty.Toasty
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

fun Activity.sweetAlertDialogProgressType(showIf: Boolean) {
    val pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
    pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
    pDialog.titleText = "Loading"
    pDialog.setCancelable(false)
    if (showIf) {
        pDialog.show()
    } else {
        pDialog.dismiss()
    }
}

fun Fragment.basicAlert(message: String) {
    AlertDialog.Builder(requireActivity())
        .setTitle(getString(R.string.error_message))
        .setMessage(message)
        .setPositiveButton(getString(R.string.ok)) { dialogInterface, which ->
        }
        .show()
}


/**
 *SnackBar method to be reused
 */
private fun View.showSnackBar(message: String, action: (() -> Unit)? = null) {
    val sB = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let {
        sB.setAction("Retry") {
            it()
        }
    }
    sB.show()
}

fun Fragment.onBackPressedCallback() {
    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
}

/**
 * Method fro enable view in class
 */
fun View.enable(isButtonEnabled: Boolean) {
    isEnabled = isButtonEnabled
    alpha = if (isButtonEnabled) 1f else 0.5f
}

fun Activity.loadActivity(goToClass: Class<*>) {
    startActivity(Intent(this, goToClass))
}

fun View.toasty(goToClass: String) {
    Toasty.normal(this.context, goToClass).show()
}

fun getHasMapData(): HashMap<String, String> {
    var hashMap: java.util.HashMap<String, String> = HashMap()
    hashMap["UserDan"] = "Ajay"
    return hashMap
}

/**
 * Validating phone number
 */

fun isFetchNeeded(savedAt: LocalDateTime): Boolean {
    // return ChronoUnit.SECONDS.between(savedAt, LocalDateTime.now()) > 20
    // val thirtyMinutesAgo = LocalDateTime.now().minusSeconds(4)
    // return savedAt.isBefore(thirtyMinutesAgo)
    return true
}

/**
 *SnackBar method to be reused in fragments
 */

fun Fragment.snackBarCustom(msg: String? = "Error", action: (() -> Unit)? = null) {
    Snackbar.make(
        activity!!.findViewById(android.R.id.content),
        msg.toString(),
        Snackbar.LENGTH_LONG
    ).also {
        it.setAction(
            getString(R.string.ok)
        ) { action?.invoke() }
    }.show()
}

fun Activity.snackBarCustom(msg: String, action: (() -> Unit)? = null) {
    Snackbar.make(
        findViewById(android.R.id.content),
        msg,
        Snackbar.LENGTH_LONG
    ).also {
        it.setAction(
            getString(R.string.ok)
        ) { action?.invoke() }
    }.show()
}

fun MaterialToolbar.setBackButton(
    title: Int,
    context: Activity,
    color: Int? = Color.WHITE,
    setNavIcon: Int = R.drawable.ic_arrow_back_common,
    action: (() -> Unit)? = null
) {
    this.setNavigationIcon(setNavIcon)
    this.setTitle(title)
    this.setTitleTextColor(color!!)
    this.setNavigationOnClickListener { view1: View? ->
        if (action == null) {
            context.onBackPressed()
        } else {
            action.invoke()
        }
    }
}

val DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"
val DATE_FORMAT_PATTERN_SSSZZZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZ"
val DATE_FORMAT_PATTERN_SSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS z"
val DATE_FORMAT_PATTERN_SSSXXX = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

// Formatting date to SimpleDateFormat and getting date from it

fun formatRequestDate(date: String?, formatPatten: String = DATE_FORMAT_PATTERN): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    // val formatter = SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss")
    var returnString = ""
    try {
        val date = formatter.parse(date?.replace("Z$".toRegex(), "+0000").toString())
        // val dateToFormat = formatter.parse(date)
        Timber.e("TIME TO FORMAT=date=========== $date")
        Timber.e("TIME TO FORMAT==TimeZone.getDefault().id========== ${TimeZone.getDefault().id}")
        Timber.e("TIME TO FORMAT=formatter.format(date)=========== ${formatter.format(date)}")
        returnString = formatter.format(date).toString()
    } catch (e: Exception) {
        val formatter = SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss", Locale.getDefault())
        val strDate = formatter.format(Date())
        Timber.e("TIME TO FORMAT=Exception=========== ${TimeZone.getDefault().id}")
        Timber.e("TIME TO FORMAT=Exception2=========== ${e.message}")
        returnString = date.toString()
    }
    return returnString
}
// 2014-10-05T15:23:01Z
// 2022-06-13T14:44:43.01Z
// "2022-06-13T14:44:43.553Z"

// extension function to convert dp to equivalent pixels
fun Int.dpToPixels(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)

fun Fragment.makeCameraPermissionsRequest() {
    requestPermissions(
        arrayOf(Manifest.permission.CAMERA),
        CAPTURE_CAMERA
    )
}

fun Fragment.openCameraForPickingImage(code: Int) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
        // startActivityForResult(Intent.createChooser(this, getString(R.string.select_file)), code)
        startActivityForResult(this, code)
    }
}

/**
 * Convert Bitmap to file
 */
fun Fragment.convertBitmapToFile(fileName: String, bitmap: Bitmap): Uri {
    // val file = File(requireActivity().cacheDir, fileName)
    /* val file = File(
         Environment.getExternalStorageDirectory().toString() + File.separator + fileName
     )*/
    val file =
        File(context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$fileName")

    Timber.d("FILE !!!!!   ${file.toUri()}")
    Timber.d("FILE !!!!!   ${file.name}")
    try {
        // Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        Timber.d("FILE Utils 1  $bitmap")
        Timber.d("FILE Utils 1  $fileName")
        Timber.d("FILE Utils 1  $file")
        if (!file.exists()) {
            file.createNewFile()
            file.mkdir()
        }
        // file.createNewFile()
        Timber.d("11 SUCCESS write the bytes in file")
        // write the bytes in file
        val fos = FileOutputStream(file)
        Timber.d("1 SUCCESS write the bytes in file")
        fos.write(bitMapData)
        fos.flush()
        fos.close()
        Timber.d("2 SUCCESS write the bytes in file")
    } catch (e: Exception) {
        Timber.d("IOException  ${e.message}")
        e.printStackTrace()
    }
    Timber.d("FILE Utils  ${file.absoluteFile}")
    return file.toUri()
}

fun String.getFirstLetter(): String {
    val array = this.split(" ")
    return if (array.size == 1) {
        array[0].substring(0, 1)
    } else {
        array[0].substring(0, 1) + array[1].substring(0, 1)
    }
}

// SetAdapter on a spinner
fun AutoCompleteTextView.setUpSpinner(
    arrayResource: Int,
    onItemClick: (
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) -> Unit
) {
    val providers = resources.getStringArray(arrayResource)
    val providerAdapter = ArrayAdapter(
        context,
        R.layout.common_text_layout,
        providers
    )
    setAdapter(providerAdapter)
    setOnFocusChangeListener { v, hasFocus ->
    }
    setOnItemClickListener { parent, view, position, id ->
        onItemClick(parent, view, position, id)
    }
}

fun Fragment.toasty(message: String) {
    Toasty.info(requireContext(), message, Toast.LENGTH_SHORT, true).show()
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

val maskTextInTextView = object : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }

    inner class PasswordCharSequence(private val source: CharSequence) : CharSequence {

        override val length: Int
            get() = source.length

        override fun get(index: Int): Char = 'X'

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return source.subSequence(startIndex, endIndex)
        }
    }
}

fun Fragment.setTransparentBackground() {
    val bottomSheet = (requireView().parent as View)
    bottomSheet.apply {
        backgroundTintMode = PorterDuff.Mode.CLEAR
        backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        setBackgroundColor(Color.TRANSPARENT)
    }
}

fun createSuccessBundle(
    title: String,
    subTitle: String,
    cardTitle: String,
    cardContent: String,
    hashMap: HashMap<String, String>
): Bundle {
    val bundle = Bundle()
    bundle.putString("title", title)
    bundle.putString("subtitle", subTitle)
    bundle.putString("cardTitle", cardTitle)
    bundle.putString("cardContent", cardContent)
    bundle.putSerializable("content", hashMap)
    return bundle
}

fun Fragment.contactDataList(data: Intent?): String {
    val contactData = data!!.data
    var number = ""
    val cursor: Cursor =
        requireActivity().contentResolver
            .query(contactData!!, null, null, null, null)!!
    cursor.moveToFirst()
    val hasPhone =
        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
    val contactId =
        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
    if (hasPhone == "1") {
        val phones: Cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +
                " = " + contactId,
            null,
            null
        )!!
        while (phones.moveToNext()) {
            number =
                phones.getString(phones.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    .replace("[-() ]".toRegex(), "")
        }
        phones.close()

        timber(number)

        // Do something with number
    } else {
        timber(number)
        toasty("This contact has no phone number")
        number = ""
    }
    cursor.close()

    return number
}

fun Activity.showCargillInternalCustomDialog(
    title: String = getString(R.string.home),
    description: String = getString(R.string.home),
    btnConfirmText: String = getString(R.string.confirm),
    positiveButtonFunction: (() -> Unit)? = null,
    negativeButtonFunction: (() -> Unit)? = null
) {
    val dialog = Dialog(this, R.style.Theme_Dialog)
    dialog.window?.requestFeature(Window.FEATURE_NO_TITLE) // if you have blue line on top of your dialog, you need use this code
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_custom_confirm_layout)
    val dialogTitle = dialog.findViewById(R.id.tvUnlinkAccount) as TextView
    val dialogDescription =
        dialog.findViewById(R.id.tv_would_you_like_to_unlink_your_account) as TextView
    val dialogPositiveButton = dialog.findViewById(R.id.btnConfirm) as Button
    dialogPositiveButton.text = btnConfirmText
    val dialogNegativeButton = dialog.findViewById(R.id.btnCancel) as Button
    dialogTitle.text = title
    dialogDescription.text = description
    dialogPositiveButton.setOnClickListener {
        positiveButtonFunction?.invoke()
        dialog.dismiss()
    }
    dialogNegativeButton.setOnClickListener {
        negativeButtonFunction?.invoke()
        dialog.dismiss()
    }
    dialog.show()
}

/**
 *Visibility method to be reused to hide views
 */
fun View.visibilityView(isViewVisible: Boolean) {
    visibility = if (isViewVisible) View.VISIBLE else View.GONE
}

fun <T> convertJsonStringToObject(
    jsonString: String,
    clazz: Class<Array<T>>
): Array<T> =
    Gson().fromJson(jsonString, clazz)

fun <T> convertJsonStringToObjectList(
    jsonString: String,
    clazz: Class<T>
): List<T> {
    val gson = Gson()
    val objects = gson.fromJson(jsonString, JsonElement::class.java).asJsonArray
    return objects.map { gson.fromJson(it, clazz) }
}

fun <T> convertJsonObjectToString(listFromString: Class<Array<T>>): String {
    return Gson().toJson(listFromString)
}

val activityResultContractPickContactContract = object : ActivityResultContract<Uri, Uri?>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        Timber.e("TAG createIntent() called")
        return Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI).also {
            it.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        Timber.e("parseResult() called")
        return if (resultCode != Activity.RESULT_OK || intent == null) null else intent.data
    }
}

fun Activity.hasGooglePlayServices(): Boolean {
    val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
        this.applicationContext
    )
    if (status == ConnectionResult.SUCCESS) {
        Timber.e("GOOGLE  ENABLED")
        return true
    } else {
        Timber.e("HUAWEI  ENABLED")
        return false
    }
}

/**
 * image loader method with glide library
 */
fun ImageView.imageLoader(url: String) {
    Glide.with(this)
        .asBitmap()
        .placeholder(R.drawable.cargill_icon_logo)
        .load(url)
        .into(this)
}

/**
 * Extension Function to convert string to ascii
 */
fun stringToAsciiNumber(name: String): String {
    var asciiStr = ""
    val iterator = name.iterator()
    for (i in iterator) {
        asciiStr += (i.toInt().toString() + ",")
    }
    return asciiStr.removeSuffix(",")
}
