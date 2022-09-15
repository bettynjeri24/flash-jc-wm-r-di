package com.ekenya.rnd.common.storage

import android.content.Context
import android.content.SharedPreferences
import com.ekenya.rnd.common.Constants


object SharedPreferencesManager {
    // properties
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.MyPREFERENCES, Context.MODE_PRIVATE)
    }

    fun getFirstName(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.FIRST_NAME, "")
    }

    fun setFirstName(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.FIRST_NAME, value)
        editor.apply()
    }

    fun getTolloOtp(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.TOLLO_OTP, "")
    }

    fun setTolloOtp(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.TOLLO_OTP, value)
        editor.apply()
    }


    fun getTolloDevicetoken(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.DEVICE_TOKEN, "")
    }

    fun setTolloDevicetoken(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.DEVICE_TOKEN, value)
        editor.apply()
    }









    fun getPin(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.PIN, "")
    }

    fun setPin(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.PIN, value)
        editor.apply()
    }
    fun getInitialPin(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.INITIALPIN, "")
    }

    fun setInitialPin(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.INITIALPIN, value)
        editor.apply()
    }
    fun getMiddleName(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.MIDDLE_NAME, "")
    }

    fun setMiddleName(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.MIDDLE_NAME, value)
        editor.apply()
    }
    fun getLastName(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.SURNAME, "")
    }

    fun setLastName(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.SURNAME, value)
        editor.apply()
    }

    fun getemailaddress(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.EMAIL_ADDRESS, "")
    }

    fun setEmailAddress(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.EMAIL_ADDRESS, value)
        editor.apply()
    }
    fun getIDNumber(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.ID_NUMBER, "")
    }

    fun setIDNumber(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.ID_NUMBER, value)
        editor.apply()
    }
    fun getDob(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.DOB, "")
    }

    fun setDob(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.DOB, value)
        editor.apply()
    }

    fun getPhoneNumber(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.PHONE_NUMBER, "")
    }

    fun setPhoneNumber(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.PHONE_NUMBER, value)
        editor.apply()
    }
    fun setWalletAccNumber(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("WALLET_ACC_NUMBER", value)
        editor.apply()
    }

    fun getWalletAccNumber(context: Context): String? {
        return getSharedPreferences(context).getString("WALLET_ACC_NUMBER", "")
    }

    fun getMerchantCode(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.MERCHANT_CODE, "")
    }

    fun setMerchantCode(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.MERCHANT_CODE, value)
        editor.apply()
    }
    fun getseats(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.MERCHANT_CODE, "")
    }

    fun setseats(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.MERCHANT_CODE, value)
        editor.apply()
    }
    fun getluggageValue(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.Luggage, "No")
    }

    fun setLuggageValue(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.Luggage, value)
        editor.apply()
    }
    fun getCardNumber(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.CARD_NUMBER, "")
    }

    fun setCardNumber(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.CARD_NUMBER, value)
        editor.apply()
    }
    fun getPhoneImei(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.IMEI, "")
    }

    fun setPhoneImei(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.IMEI, value)
        editor.apply()
    }
    fun getPassword(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.PASSWORD, "")
    }

    fun setPassword(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.PASSWORD, value)
        editor.apply()
    }
    fun getOtpToken(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.OTP_TOKEN, "")
    }

    fun setOtpToken(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.OTP_TOKEN, value)
        editor.apply()
    }
    fun getAmouttoTopUP(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.AMOUNT_TO_TOPUP, "0")
    }

    fun setAmount(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.AMOUNT_TO_TOPUP, value)
        editor.apply()
    }
    fun getdepositAmount(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.DEPOSIT_AMOUNT, "0")
    }

    fun setDepositAmount(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.DEPOSIT_AMOUNT, value)
        editor.apply()
    }
   /* fun getToken(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.ACCESS_TOKEN, null)
    }

    fun setToken(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.ACCESS_TOKEN, value)
        editor.apply()
    }*/

    fun getnewToken(context: Context): String? {
        return getSharedPreferences(context).getString("access_token", "")
    }

    fun setnewToken(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString("access_token", value)
        editor.apply()
    }
    fun getAccountNumber(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.ACCOUNT_NUMBER, "")
    }

    fun setAccountNumber(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.ACCOUNT_NUMBER, value)
        editor.apply()
    }

    fun setNumberOfSavingsAccount(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.SAVINGSACCOUNTS_NUMBER, value)
        editor.apply()
    }
    fun getNumberOfSavingsAccount(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.SAVINGSACCOUNTS_NUMBER, "0")
    }
    fun getAccountBalance(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.ACCOUNT_BALANCE, "0.00")
    }

    fun getProfilePhoto(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.PROFILE_PHOTO, null)
    }

    fun setAccountBalance(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.ACCOUNT_BALANCE, value)
        editor.apply()
    }
    fun setProfilePhoto(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.PROFILE_PHOTO, value)
        editor.apply()
    }
    fun getUnforMattedAccountBalance(context: Context): Int? {
        return getSharedPreferences(context).getInt(Constants.RAW_BALANCE, 0)
    }
    fun setUnformattedAccountBalance(context: Context, value: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(Constants.RAW_BALANCE, value)
        editor.apply()
    }

    fun getGrantType(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.GRANT_TYPE, "")
    }

    fun setGrantType(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.GRANT_TYPE, value)
        editor.apply()
    }
    fun getGeolocation(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.GEOLOCATION, "Home")
    }

    fun setGeolocation(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.GEOLOCATION, value)
        editor.apply()
    }
    fun getUserAGentVersion(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.USER_AGENT_VERSION, "22 (5.1.1)")
    }

    fun setUserAGentVersion(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.USER_AGENT_VERSION, value)
        editor.apply()
    }
    fun getUserAGent(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.USER_AGENT, "android")
    }

    fun setUserAGent(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.USER_AGENT, value)
        editor.apply()
    }
    fun getcardDetails(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.ENCRYPTED_CARD, "")
    }

    fun setcardDetails(context: Context, value: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.ENCRYPTED_CARD, value)
        editor.apply()
    }

    fun isFirstTimeUser(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.IS_FIRST_TIME_USER, true)
    }

    fun setisFirstTimeUser(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.IS_FIRST_TIME_USER, value)
        editor.apply()
    }
    fun isAmharicSelected(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.IS_AMHARIC_SELECTED, false)
    }

    fun setIsAmharicSelected(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.IS_AMHARIC_SELECTED, value)
        editor.apply()
    }
    fun hasSetDateOfBirth(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.HAS_SET_DATE, false)
    }

    fun setDateOfBirth(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.HAS_SET_DATE, value)
        editor.apply()
    }
    fun isFirstTimeAtLanding(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.IS_FIRST_TIME_AT_LANDING, true)
    }

    fun setisFirstTimeAtLanding(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.IS_FIRST_TIME_AT_LANDING, value)
        editor.apply()
    }
    fun hasReachedHomepage(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.HAS_REACHED_HOMEPAGE, false)
    }

    fun setHasReachedHomepage(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.HAS_REACHED_HOMEPAGE, value)
        editor.apply()
    }

    fun isRegisteredUser(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.IS_REGISTERED_USER, false)
    }

    fun setIsRegistereduser(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.IS_REGISTERED_USER, value)
        editor.apply()
    }
    fun hasSetInitialPin(context: Context): Int? {
        return getSharedPreferences(context).getInt(Constants.HaS_SET_INITIAL_PIN, 0)
    }

    fun setHasSetPin(context: Context, value: Int) {
        val editor = getSharedPreferences(context).edit()
        editor.putInt(Constants.HaS_SET_INITIAL_PIN, value)
        editor.apply()
    }

    fun hasFinishedSliders(context: Context): Boolean? {
        return getSharedPreferences(context).getBoolean(Constants.HAS_FINISHED_SLIDERS, false)
    }

    fun setHasFinishedSliders(context: Context, value: Boolean) {
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.HAS_FINISHED_SLIDERS, value)
        editor.apply()
    }


}