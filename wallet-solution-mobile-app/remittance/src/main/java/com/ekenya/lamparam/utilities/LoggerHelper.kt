package com.ekenya.lamparam.utilities

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