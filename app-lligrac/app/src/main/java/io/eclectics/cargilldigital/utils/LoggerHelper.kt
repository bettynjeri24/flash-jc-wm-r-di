package io.eclectics.cargilldigital.utils

import android.util.Log

class LoggerHelper {

    companion object {
        fun loggerError(tag: String, message: String) {
            Log.e(tag, message)
        }

        fun loggerSuccess(tag: String, message: String) {
            Log.d(tag, message)
        }


    }
}