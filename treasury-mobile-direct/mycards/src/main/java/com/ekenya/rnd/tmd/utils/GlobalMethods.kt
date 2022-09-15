package com.ekenya.rnd.tmd.utils

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavOptions
import com.ekenya.rnd.mycards.R
import dagger.Module
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

/**
 * A global utils class
 */
@Module
class GlobalMethods @Inject constructor(val app: Application) {

    /**
     * Converts Bitmap image to string
     * the returns it to calling function
     */
    fun convertBitmapToString(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    /**
     * Converts String to Bitmap
     * the returns it to calling function
     */
    fun convertStringToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    /**
     * @param filename of file to be deleted
     */
    fun deletePhotoFromInternalStorage(filename: String): Boolean {
        return try {
            app.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * @param fileName used to filter list of images
     */
    suspend fun loadPhotoFromInternalStorage(fileName: String): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            val files = app.filesDir.listFiles()
            files?.filter { it.canRead() && it.isFile && it.name.equals(fileName) }?.map {
                val bytes = it.readBytes()
                /*val bmp =*/ BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                InternalStoragePhoto(it.name, bmp)
            } ?: listOf()

//            val file = files.single { it.canRead() && it.isFile && it.name.equals(fileName) }
//            val bytes = file.readBytes()
//           /* val bmp = */ BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//            InternalStoragePhoto(file.name, bmp)
        }
    }

    /**
     * @param fileName used to store the file
     * @param bmp the image to be stored
     * @return result
     */
    fun savePhotoToInternalStorage(fileName: String, bmp: Bitmap): Boolean {
        return try {
            app.openFileOutput("$fileName.png", MODE_PRIVATE).use { stream ->
                if (!bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    throw IOException()
                }
            }
            true
        } catch (e: IOException) {
            e.message?.let { Log.d("GlobalMethods", it) }
            false
        }
    }

    fun generateOtp(): String {
        val allowedChars = ('0'..'9')
        return (1..5)
            .map { allowedChars.random() }
            .joinToString("")
    }

    /**
     * Opens an intent for a web app
     */
    fun openPage(view: View, prodInfo: String?) {
        val con = view.context
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(prodInfo)
            con.startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(con, "You don't have a proper app to perform this action", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Return navOptions for animating navigation
     */
    fun getAppearNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in_auth)
            .setExitAnim(R.anim.fade_out_auth)
            .setPopEnterAnim(R.anim.fade_in_auth)
            .setPopExitAnim(R.anim.fade_out_auth)
            .build()
    }

    /**
     * Return navOptions for animating navigation
     */
    fun getSlideNavOptions(): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_left_auth)
            .setExitAnim(R.anim.slide_out_left_auth)
            .setPopEnterAnim(R.anim.slide_in_right_auth)
            .setPopExitAnim(R.anim.slide_out_right_auth)
            .build()
    }
}
