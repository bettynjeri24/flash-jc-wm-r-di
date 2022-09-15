package com.ekenya.lamparam.user

import com.ekenya.lamparam.storage.Storage
import javax.inject.Inject

private const val REGISTERED_USER = "registered_user" //user registered for LAMPARAM
private const val FIRST_NAME = "first_name"
private const val USER_LANGUAGE = "user_language"
private const val SESSION_TOKEN = "session_token"
private const val DEVICE_ID = "device_id"
private const val CHANGED_PIN = "change_pin"

class UserManager @Inject constructor(private val storage: Storage) {

    /**
     *  UserDataRepository is specific to a logged in user. This determines if the user
     *  is logged in or not, when the user logs in, a new instance will be created.
     *  When the user logs out, this will be null.
     */
    var userDataRepository: UserDataRepository? = null

    val phoneNumber: String
        get() = storage.getString(REGISTERED_USER)

    val name: String
        get() = storage.getString(FIRST_NAME)

    val userLanguage: String
        get() = storage.getString(USER_LANGUAGE)

    val userToken: String
        get() = storage.getString(SESSION_TOKEN)

    val deviceID: String
        get() = storage.getString(DEVICE_ID)

    /**
     * Returns true if it user has registered for LAMPARAM
     */
    val registered: Boolean
        get() = storage.getString(REGISTERED_USER).isNotEmpty()

    /**
     * Returns true if it user has registered for LAMPARAM
     */
    val hasToken: Boolean
        get() = storage.getString(SESSION_TOKEN).isNotEmpty()

    /**
     * Saves the token
     */
    fun saveToken(token: String) = storage.setString(SESSION_TOKEN, token)

    /**
     * Returns true if IMEI is stored
     */
    val hasImei: Boolean
        get() = storage.getString(DEVICE_ID).isNotEmpty()

    /**
     * Marks that a user has registered for LAMPARAM
     */
    fun registerMM(phone: String, name: String) {
        storage.setString(REGISTERED_USER, phone)
        storage.setString(FIRST_NAME, name)
    }

    /**
     * Sets the language of the user
     */
    fun chooseLanguage(language: String) = storage.setString(USER_LANGUAGE, language)

    fun eraseToken() = storage.setString(SESSION_TOKEN, "")

    /**
     * Stores the device imei
     */
    fun saveImei(id: String) = storage.setString(DEVICE_ID, id)

    /**
     * Stores otp PIN
     */
    fun otpPIN(pin: String) = storage.setString(CHANGED_PIN, pin)

    /**
     * Initialize repository for user that has logged in
     */
    fun loginUser() {
        userDataRepository = UserDataRepository(this)
    }

    /**
     * Set the userDataRepository to null
     */
    fun logout() {
        userDataRepository = null
    }

}