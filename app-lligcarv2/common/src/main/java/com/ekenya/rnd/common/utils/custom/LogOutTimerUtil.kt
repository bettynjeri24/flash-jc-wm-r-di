package com.ekenya.rnd.common.utils.custom

import kotlin.jvm.Synchronized
import timber.log.Timber
import android.os.AsyncTask
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import java.util.*
import java.util.concurrent.ExecutionException

/**
 * The type Log out timer util.
 */
object LogOutTimerUtil {
    /**
     * The Long timer.
     */
    var longTimer: Timer? = null

    /**
     * The Logout time.
     */
    private const val LOGOUT_TIME =
        60000 // delay in milliseconds i.e. 2 min = 60000 ms or use timeout argument

    /**
     * Start logout timer.
     *
     * @param context        the context
     * @param logOutListener the log out listener
     */
    @Synchronized
    fun startLogoutTimer(context: Context?, logOutListener: LogOutListener) {
        if (longTimer != null) {
            longTimer!!.cancel()
            longTimer = null
        }
        if (longTimer == null) {
            longTimer = Timer()
            longTimer!!.schedule(object : TimerTask() {
                override fun run() {
                    cancel()
                    longTimer = null
                    try {
                        val foreGround = ForegroundCheckTask().execute(context).get()
                        if (foreGround) {
                            Timber.e("foreGround--- %s", foreGround)
                            logOutListener.doLogout()
                        }
                    } catch (e: InterruptedException) {
                        Timber.e("foreGround Exception --- %s", e.message)
                    } catch (e: ExecutionException) {
                        Timber.e("foreGround Exception --- %s", e.message)
                    }
                }
            }, LOGOUT_TIME.toLong())
        }
    }

    /**
     * Stop logout timer.
     */
    @Synchronized
    fun stopLogoutTimer() {
        if (longTimer != null) {
            longTimer!!.cancel()
            longTimer = null
        }
    }

    /**
     * The interface Log out listener.
     */
    interface LogOutListener {
        /**
         * Do logout.
         */
        fun doLogout()
    }

    /**
     * The type Foreground check task.
     */
    internal class ForegroundCheckTask : AsyncTask<Context?, Void?, Boolean>() {
        private fun isAppOnForeground(context: Context): Boolean {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appProcesses = activityManager.runningAppProcesses ?: return false
            val packageName = context.packageName
            for (appProcess in appProcesses) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName == packageName) {
                    return true
                }
            }
            return false
        }

        override fun doInBackground(vararg params: Context?): Boolean {
            val context = params[0]!!.applicationContext
            return isAppOnForeground(context)
        }
    }
}