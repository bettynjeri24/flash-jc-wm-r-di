package com.ekenya.lamparam.storage

import android.content.Context
import javax.inject.Inject

/**
 * Performs storage and retrieval of SharedPreferences data
 */
class SharedPreferencesStorage @Inject constructor(private val context: Context): Storage {

    private val sharedPreferences = context.getSharedPreferences("Lamparam", Context.MODE_PRIVATE)

    // Step 1: Create or retrieve the Master Key for encryption/decryption
//    private val mainKey = MasterKey.Builder(context)
//        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//        .build()
//
//    // Step 2: Initialize/open an instance of EncryptedSharedPreferences
//    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
//        context,
//        "Lamparam",
//        mainKey,
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )

    override fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }
}