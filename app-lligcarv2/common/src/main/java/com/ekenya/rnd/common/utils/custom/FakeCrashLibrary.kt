package com.ekenya.rnd.common.utils.custom

import android.util.Log
import timber.log.Timber

/** Not a real crash reporting library!  */
class FakeCrashLibrary private constructor() {
    companion object {
        fun log(priority: Int, tag: String?, message: String?) {
            // TODO add log entry to circular buffer.
        }

        fun logWarning(t: Throwable?) {
            // TODO report non-fatal warning.
        }

        fun logError(t: Throwable?) {
            // TODO report non-fatal error.
        }
    }

    init {
        throw AssertionError("No instances.")
    }
}


/** A tree which logs important information for crash reporting.  */
class CrashReportingTree : Timber.Tree() {
    protected override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }
        FakeCrashLibrary.log(priority, tag, message)
        if (t != null) {
            if (priority == Log.ERROR) {
                FakeCrashLibrary.logError(t)
            } else if (priority == Log.WARN) {
                FakeCrashLibrary.logWarning(t)
            }
        }
    }
}
