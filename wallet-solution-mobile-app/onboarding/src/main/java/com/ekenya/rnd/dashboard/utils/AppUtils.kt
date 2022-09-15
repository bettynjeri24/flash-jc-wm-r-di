package com.ekenya.rnd.dashboard.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.ekenya.rnd.common.storage.SharedPreferencesManager
import com.ekenya.rnd.dashboard.datadashboard.model.OrangeAirtimePayload
import com.ekenya.rnd.onboarding.R
import com.ekenya.rnd.onboarding.dataonboarding.model.ChangePinObject
import com.ekenya.rnd.onboarding.dataonboarding.model.UserData
import java.text.SimpleDateFormat
import java.util.*


object AppUtils {


    var backImagePath = ""
    var frontImagePath = ""
    val geolocation = "Home"
    val userAgent = "android"
    val userAgentVersion = "5.1"

    fun getBackGroundImages(): IntArray {
        return intArrayOf(
            R.drawable.orange_bg,
            R.drawable.ic_rectangle_1589__1_,
            R.drawable.orange_bg,

            )
    }

    fun getSliderImages(): IntArray {
        return intArrayOf(
            R.drawable.slideone_image,
            R.drawable.slidetwo_image,
            R.drawable.slidethree_image,

            )
    }

    fun getHeadings(): Array<String> {
        return arrayOf(
            "Simple, Seamless &  Convenient way To send & receive money from Friends & Family ",
            "Link your Debit & Credit card with Your Wallet to make your transactions easier",
            "Spend & track your expenses and Cashflow within your set budget In the wallet"
        )
    }

    fun getSubHeadings(): Array<String> {
        return arrayOf(
            "You can request, send and receive money to your \nWallet from both local and international senders \n& receivers",
            "Connect your bank through your debit & credit cards with  \nYour wallet to enhance your online transactions safely and \nConveniently",
            "Manage your finances on your wallet as you set your budget & \nTrack your expenditure and cashflow of all your transactions \nWithin your wallet"
        )
    }


    fun getUserData(context: Context?): UserData {


        val firstname = context?.let { SharedPreferencesManager.getFirstName(it) }
        val secondname = context?.let { it1 -> SharedPreferencesManager.getMiddleName(it1) }
        val lastname = context?.let { it1 -> SharedPreferencesManager.getLastName(it1) }
        val email = context?.let { it1 -> SharedPreferencesManager.getemailaddress(it1) }
        val idNumber = context?.let { it1 -> SharedPreferencesManager.getIDNumber(it1) }
        val dob = context?.let { it1 -> SharedPreferencesManager.getDob(it1) }
        val phoneNumber = context?.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val geolocation = context?.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val userAgentVersion =
            context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context?.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }

        return UserData(
            "Register",
            firstname,
            secondname,
            lastname,
            email,
            idNumber!!,
            dob,
            phoneNumber,
            geolocation,
            userAgentVersion,
            userAgent,
            getReleaseMode()
        )

    }

    fun getChangePinData(context: Context): ChangePinObject {

        val passWord = context.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val otp = context.let { it1 -> SharedPreferencesManager.getTolloOtp(it1) }
        val grantType = context.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val phoneNumber = context.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val geolocation = context.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val userAgentVersion =
            context.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }

        return ChangePinObject(
            "ChangePassword",
            phoneNumber,
            passWord,
            otp,
            "access_token",
            geolocation,
            userAgentVersion,
            userAgent
        )
    }

    fun buyOrangeAirTime(context: Context): OrangeAirtimePayload {

        val passWord = context.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val otp = context.let { it1 -> SharedPreferencesManager.getOtpToken(it1) }
        val grantType = context.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val phoneNumber = context.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val accountNumber = context.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }
        val geolocation = context.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val userAgentVersion =
            context.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }





        return OrangeAirtimePayload(
            "AirtimeTopup",
            SharedPreferencesManager.getAmouttoTopUP(context)!!.toInt(),
            1,
            phoneNumber!!,
            "04040404040", "MM", phoneNumber!!, " ",
            phoneNumber!!,
            accountNumber!!, SharedPreferencesManager.getFirstName(context)!!+SharedPreferencesManager.getLastName(context)!!, "Home", "android", "5.1"
        )
    }
    fun buyMascomAirtime(context: Context): OrangeAirtimePayload {

        val passWord = context.let { it1 -> SharedPreferencesManager.getPin(it1) }
        val otp = context.let { it1 -> SharedPreferencesManager.getOtpToken(it1) }
        val grantType = context.let { it1 -> SharedPreferencesManager.getGrantType(it1) }
        val phoneNumber = context.let { it1 -> SharedPreferencesManager.getPhoneNumber(it1) }
        val accountNumber = context.let { it1 -> SharedPreferencesManager.getAccountNumber(it1) }
        val geolocation = context.let { it1 -> SharedPreferencesManager.getGeolocation(it1) }
        val userAgentVersion =
            context.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }
        val userAgent = context.let { it1 -> SharedPreferencesManager.getUserAGentVersion(it1) }





        return OrangeAirtimePayload(
            "MascomPayment", 10, 1,
            phoneNumber!!, "04040404040", "MM", "111111", " ",
            phoneNumber!!, accountNumber!!, "G Gaboiphiwe", "Home", "android", "5.1"
        )
    }
    /*fun Fragment.openCameraForPickingImage(code: Int) {
       *//* Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            startActivityForResult(Intent.createChooser(this, "Select File"), code)

        }*//*
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    showWarning(getString(R.string.photo_capture_error))
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.com.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, camera)
                }
            }
        }
    }*/

    fun getTimeofTheDay(): String {
        val c: Calendar = Calendar.getInstance()

        when (c.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> {
                return "Good Morning"
            }
            in 12..15 -> {
                return "Good Afternoon"
            }
            in 16..20 -> {
                return "Good Evening"
            }
            in 21..23 -> {
                return "Good Evening"
            }
        }
        return "Good Morning"
    }

    fun uniqueFileName(): String {
        val randomNum = (0..1000000).random()
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmssSSSSSS", Locale.ENGLISH)

        return "${dateFormat.format(date)}_$randomNum"
    }

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

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }



}