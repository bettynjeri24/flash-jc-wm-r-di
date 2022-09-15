package com.ekenya.rnd.common.encryption

import java.util.*

interface EncryptionService {
    fun init() {
        //
    }

    /**
     * Call to set the AES encryption parameters
     * @param cypher The Cypher to be applied, e.g "AES/CBC/PKCS5PADDING"
     * @param iv The Initialisation Vector
     * @param key The AES Key
     * @param ivlength The length of the Initialisation Vector
     * @param delimiter The content unification delimiter
     */
    fun setAESParams(cypher: String, iv: String, key: String, delimiter: String, ivlength: Int)

    /**
     * Call to set the RSA Encryption parameters
     * @param key The RSA public key
     * @param cypher The RSA Cypher e.g "RSA/ECB/PKCS1Padding"
     */
    fun setRSAParams(key: String, cypher: String)

    /**
     * Generate a new AES Initialisation Vector
     * @return The generated IV as [Byte[]]
     */
    fun generateAESIV(): ByteArray

    /**
     * Encrypt the given string with RSA and returns the encrypted result or null if encryption fails
     * @param data The string to encrypted
     * @return The encrypted string or null
     */
    fun encryptRSA(data: ByteArray): Optional<ByteArray>?

    /**
     * Decrypt the given string with RSA and returns the decrypted result or null if decryption fails
     * @param data The string to decrypted
     * @return The decrypted string or null
     */
    fun decryptRSA(data: ByteArray): Optional<ByteArray>?

    /**
     * Encrypt the given string with AES and returns the encrypted result or null if encryption fails
     * @param data The string to encrypted
     * @return The encrypted string or null
     */
    fun encryptAES(data: String): Optional<String>

    /**
     * Encrypt the given string with AES and returns the decrypted result or null if encryption fails
     * @param data The string to decrypted
     * @return The encrypted string or null
     */
    fun decryptAES(data: String): Optional<String>
}
