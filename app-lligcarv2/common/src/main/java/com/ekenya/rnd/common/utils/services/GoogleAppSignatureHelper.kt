package com.ekenya.rnd.common.utils.services

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.util.Base64
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * This class class generates the HashKey
 */
class GoogleAppSignatureHelper(context: Context?) :
    ContextWrapper(context) {// Get all package signatures for the current package

    // For each signature create a compatible hash
    /**
     * Get all the app signatures for the current package
     *
     * @return
     */
    val appSignatures: ArrayList<String>
        get() {
            val appCodes = ArrayList<String>()
            try {
                // Get all package signatures for the current package
                val packageName = packageName
                val packageManager = packageManager
                val signatures = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                ).signatures

                // For each signature create a compatible hash
                for (signature in signatures) {
                    val hash = hash(packageName, signature.toCharsString())
                    if (hash != null) {
                        appCodes.add(String.format("%s", hash))
                    }
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Timber.tag(TAG).e(e, "Unable to find package to obtain hash.")
            }
            return appCodes
        }

    companion object {
        val TAG = GoogleAppSignatureHelper::class.java.simpleName
        private const val HASH_TYPE = "SHA-256"
        const val NUM_HASHED_BYTES = 9
        const val NUM_BASE64_CHAR = 11
        private fun hash(packageName: String, signature: String): String? {
            val appInfo = "$packageName $signature"
            try {
                val messageDigest = MessageDigest.getInstance(HASH_TYPE)
                messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
                var hashSignature = messageDigest.digest()

                // truncated into NUM_HASHED_BYTES
                hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
                // encode into Base64
                var base64Hash =
                    Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
                base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)
                Timber.e("pkg: %s -- hash: %s", packageName, base64Hash)
                return base64Hash
            } catch (e: NoSuchAlgorithmException) {
                Timber.tag(TAG + "sms_sample_test").v(e, "hash:NoSuchAlgorithm")
            }
            return null
        }
    }
}