package com.ekenya.lamparam.user

import javax.inject.Inject

class UserDataRepository @Inject constructor(private val userManager: UserManager) {

    val firstName: String
        get() = userManager.name

    val phoneNumber: String
        get() = userManager.phoneNumber

    val language: String
        get() = userManager.userLanguage

    val token: String
        get() = userManager.userToken

    val imei: String
        get() = userManager.deviceID

    /**
     * Returns true if a token is available
     */
    val tokenAvailable: Boolean
        get() = userManager.hasToken

    /**
     * Returns true for registered user
     */
    val registered: Boolean
        get() = userManager.registered

    /**
     * Registers a user to shared pref
     */
    fun saveUser(phoneNumber: String, name: String){
        userManager.registerMM(phoneNumber, name)
    }

    /**
     * set language
     */
    fun setLanguage(language: String) = userManager.chooseLanguage(language)

    /**
     * Stores token to be used in all requests
     */
    fun storeToken(token: String) = userManager.saveToken(token)

    fun deleteToken() = userManager.eraseToken()

    /**
     * Stores IMEI in encrypted shared preferences
     */
    fun storeImei(id: String) = userManager.saveImei(id)

}