package com.ekenya.rnd.common.data.db

import android.content.Context
import android.content.SharedPreferences
import com.ekenya.rnd.common.R

private const val CARGILL_PREFERENCE_DATA = "my_preference"

class CommonCargillDataPreferences(context: Context) {
    val applicationContext = context.applicationContext

    //    private object PreferenceKey {
//        val key = preferencesKey<String>("authToken")
//        val guestDataTime = preferencesKey<String>("guestDataTime")
//    }
    companion object {
        const val CHECKED_IN_SAVED_TIME: String = "CHECKED_IN"
        const val CHECKED_OUT_SAVED_TIME: String = "CHECKED_OUT"
        const val NOTIFICATION_DATA_TIME = "NOTIFICATION"
        const val OFFICE_DETAILS_TIME = "OFFICE_DETAILS"
    }


    private var prefs: SharedPreferences =
        context.getSharedPreferences(
            applicationContext.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )

/**
     * Function to save timestamp  when data is loaded
     * Function to fetch timestamp when data was loaded*/


    fun SAVETIMESTAMP_CHECK_IN(time: String) {
        val editor = prefs.edit()
        editor.putString(CHECKED_IN_SAVED_TIME, time)
        editor.apply()
    }

    fun FETCHTIMESTAMP_CHECK_IN(): String? {
        return prefs.getString(CHECKED_IN_SAVED_TIME, null)

    }

    fun SAVETIMESTAMP_CHECK_OUT(time: String) {
        val editor = prefs.edit()
        editor.putString(CHECKED_OUT_SAVED_TIME, time)
        editor.apply()
    }

    fun FETCHTIMESTAMP_CHECK_OUT(): String? {
        return prefs.getString(CHECKED_OUT_SAVED_TIME, null)

    }

    fun SAVETIMESTAMP_NOTIFICATION(time: String) {
        val editor = prefs.edit()
        editor.putString(NOTIFICATION_DATA_TIME, time)
        editor.apply()
    }

    fun FETCHTIMESTAMP_NOTIFICATION(): String? {
        return prefs.getString(NOTIFICATION_DATA_TIME, null)
    }

    fun SAVETIMESTAMP_OFFICE_DETAILS(time: String) {
        val editor = prefs.edit()
        editor.putString(OFFICE_DETAILS_TIME, time)
        editor.apply()
    }

    fun FETCHTIMESTAMP_OFFICE_DETAILS(): String? {
        return prefs.getString(OFFICE_DETAILS_TIME, null)
    }

}
