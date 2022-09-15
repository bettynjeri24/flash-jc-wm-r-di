package io.eclectics.cargilldigital.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class PreferenceProvider(newBase: Context) {

    private val LANGUAGE_DATA = "language"

    private var appContext: Context? = null
    private var preference: SharedPreferences? = null

    init {
        appContext = newBase
        preference = PreferenceManager.getDefaultSharedPreferences(appContext)
    }

    fun setLanguage(language: String) {
        preference!!.edit().putString(LANGUAGE_DATA, language).apply()
    }

    fun getLanguage(): String {
        return preference!!.getString(LANGUAGE_DATA, "en")!!
    }


}