package com.ekenya.rnd.common.utils.services

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import android.util.Log
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class HuaweiHashManager {
    private val TAG = HuaweiHashManager::class.java.canonicalName
    fun getHashValue(appContext: Context): String {
        val packageName = appContext.packageName
        val messageDigest = messageDigest
        val signature = getSignature(appContext, packageName)
        val hashCode = getHashCode(packageName, messageDigest, signature)
        Log.d(TAG, hashCode)
        return hashCode
    }

    private val messageDigest: MessageDigest?
        private get() {
            var messageDigest: MessageDigest? = null
            try {
                messageDigest = MessageDigest.getInstance("SHA-256")
            } catch (e: NoSuchAlgorithmException) {
                Log.e(TAG, "No Such Algorithm.", e)
            }
            return messageDigest
        }

    private fun getSignature(context: Context, packageName: String): String {
        val packageManager = context.packageManager
        val signatureArrs: Array<Signature>?
        signatureArrs = try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package name inexistent.")
            return ""
        }
        if (null == signatureArrs || 0 == signatureArrs.size) {
            Log.e(TAG, "signature is null.")
            return ""
        }
        return signatureArrs[0].toCharsString()
    }

    private fun getHashCode(
        packageName: String,
        messageDigest: MessageDigest?,
        signature: String
    ): String {
        val appInfo = "$packageName $signature"
        messageDigest!!.update(appInfo.toByteArray(StandardCharsets.UTF_8))
        var hashSignature = messageDigest.digest()
        hashSignature = Arrays.copyOfRange(hashSignature, 0, 9)
        var base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
        base64Hash = base64Hash.substring(0, 11)
        return base64Hash
    }
}