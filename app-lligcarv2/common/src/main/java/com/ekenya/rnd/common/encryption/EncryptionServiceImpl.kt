package com.ekenya.rnd.common.encryption

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.lang.RuntimeException
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionServiceImpl : EncryptionService {
    private var AES_IV_LENGTH = // = 16;//generate random 16 byte long
        0

    /**
     * The constant AES_CIPHER_NAME.
     */
    private var AES_CIPHER_NAME: String? = // ="AES/CBC/PKCS5PADDING";
        null

    /**
     * The constant RSA_CIPHER_NAME.
     */
    private var RSA_CIPHER_NAME: String? = // = "RSA/ECB/PKCS1Padding";
        null
    private var AES_KEY: String? = null
    private var AES_IV: String? = null
    private var AES_DELIMITER: String? = null
    private var RSA_KEY: String? = null

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun setAESParams(
        cypher: String,
        iv: String,
        key: String,
        delimiter: String,
        ivlength: Int
    ) {
        AES_KEY = key
        AES_IV = iv
        AES_DELIMITER = delimiter
        AES_CIPHER_NAME = cypher
        AES_IV_LENGTH = ivlength
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun setRSAParams(key: String, cypher: String) {
        RSA_KEY = key
        RSA_CIPHER_NAME = cypher
    }

    /*
     *  build the initialization vector (randomly).
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun generateAESIV(): ByteArray {
        val initVector: String? = null
        val random = SecureRandom()
        val iv = ByteArray(AES_IV_LENGTH)
        random.nextBytes(iv)
        return iv
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun encryptRSA(data: ByteArray): Optional<ByteArray>? {
        var key: PublicKey? = null
        try {
            key = KeyFactory.getInstance("RSA")
                .generatePublic(X509EncodedKeySpec(Base64.decode(RSA_KEY, Base64.DEFAULT)))
            val cipher = Cipher.getInstance(RSA_CIPHER_NAME)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val doFinal = cipher.doFinal(data)
            return Optional.ofNullable(doFinal)
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun decryptRSA(data: ByteArray): Optional<ByteArray>? {
        try {
            val key = KeyFactory.getInstance("RSA")
                .generatePublic(X509EncodedKeySpec(Base64.decode(RSA_KEY, Base64.DEFAULT)))
            val cipher = Cipher.getInstance(RSA_CIPHER_NAME)
            cipher.init(Cipher.DECRYPT_MODE, key)
            return Optional.ofNullable(cipher.doFinal(data))
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        }
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun encryptAES(data: String): Optional<String> {
        val iv = generateAESIV()
        return try {
            val decryptkey = fixKey(AES_KEY)!!.toByteArray(StandardCharsets.UTF_8)
            val ivSpec = IvParameterSpec(iv)
            val secretKey = SecretKeySpec(decryptkey, "AES")
            val cipher = Cipher.getInstance(AES_CIPHER_NAME)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            val encryptedData = cipher.doFinal(data.toByteArray())
            val encryptedDataInBase64 = convertToBase64(encryptedData)
            val ivInBase64 = convertToBase64(iv)
            // return Optional.ofNullable(
            Optional.of(convertToBase64((encryptedDataInBase64 + AES_DELIMITER + ivInBase64).toByteArray()))
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun decryptAES(data: String): Optional<String> {
        return try {
            val results = String(Base64.decode(data.toByteArray(), Base64.DEFAULT))
            val parts = results.split(AES_DELIMITER!!.toRegex()).toTypedArray()
            val decryptkey = fixKey(AES_KEY)!!.toByteArray(StandardCharsets.UTF_8)
            val iv = IvParameterSpec(Base64.decode(parts[1], Base64.DEFAULT))
            val secretKey = SecretKeySpec(decryptkey, "AES")
            val cipher = Cipher.getInstance(AES_CIPHER_NAME)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
            val decodedEncryptedData = Base64.decode(parts[0], Base64.DEFAULT)
            val original = cipher.doFinal(decodedEncryptedData)
            // return Optional.ofNullable(
            Optional.of(String(original))
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }

    private fun fixKey(keyy: String?): String? {
        var keyy = keyy
        if (keyy!!.length < AES_IV_LENGTH) {
            val numPad = AES_IV_LENGTH - keyy.length
            for (i in 0 until numPad) {
                keyy += "0" // 0 pad to len 16 bytes
            }
            return keyy
        }
        return if (keyy.length > AES_IV_LENGTH) {
            keyy.substring(0, AES_IV_LENGTH)
        } else keyy
    }

    companion object {
        fun convertToBase64(data: ByteArray?): String {
            return Base64.encodeToString(data, Base64.DEFAULT)
        }
    }
}
