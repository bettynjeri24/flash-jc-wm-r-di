package com.ekenya.lamparam.storage

/**
 * Interface for performing storage and retrieval of data from SharedPreferences
 */
interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String
}