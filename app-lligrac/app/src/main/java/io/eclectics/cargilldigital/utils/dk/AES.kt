package com.ekenya.rnd.common.dk

import timber.log.Timber
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

const val secretKey = "ssshhhhhhhhhhh!!!!"

/**
 * https://howtodoinjava.com/java/java-security/java-aes-encryption-example/
 * https://mkyong.com/java/java-aes-encryption-and-decryption/
 */
object AES {
    private var secretKey: SecretKeySpec? = null
    private lateinit var key: ByteArray
    private fun setKey(myKey: String) {
        var sha: MessageDigest? = null
        try {
            key = myKey.toByteArray(charset("UTF-8"))
            sha = MessageDigest.getInstance("SHA-1")
            key = sha.digest(key)
            key = Arrays.copyOf(key, 16)
            secretKey = SecretKeySpec(key, "AES")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    fun encrypt(strToEncrypt: String, secret: String): String? {
        try {
            setKey(secret)
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            return Base64.getEncoder()
                .encodeToString(cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8"))))
        } catch (e: Exception) {
            println("Error while encrypting: $e")
        }
        return null
    }

    fun decrypt(strToDecrypt: String?, secret: String): String? {
        try {
            setKey(secret)
            val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            return String(
                cipher.doFinal(
                    Base64.getDecoder()
                        .decode(strToDecrypt)
                )
            )
        } catch (e: Exception) {
            println("Error while decrypting: $e")
        }
        return null
    }
}


fun getMd5Hash(plainText: String): String {
    val bytes = MessageDigest.getInstance("MD5").digest(plainText.toByteArray())
    Timber.e("TOHEX =>$ ${bytes.joinToString()}")
    val toHex = bytes.joinToString("") { "%02x".format(it) }
    Timber.e("TOHEX2 =>$ ${bytes.joinToString { "%02x".format(it) }}")
    Timber.e("TOHEX3 =>$ ${toHex}")

    return toHex
}

fun getBase64Hash(plainText: String): String {
    val encodedBytes: ByteArray = Base64.getEncoder().encode(plainText.toByteArray())
    Timber.e("encodedBytes " + String(encodedBytes))

    val decodedBytes: ByteArray = Base64.getDecoder().decode(encodedBytes)
    Timber.e("decodedBytes " + String(decodedBytes))

    return String(encodedBytes)
}