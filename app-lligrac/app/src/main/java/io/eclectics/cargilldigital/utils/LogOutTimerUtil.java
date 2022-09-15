package io.eclectics.cargilldigital.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * The type Log out timer util.
 */
public class LogOutTimerUtil {

    /**
     * The interface Log out listener.
     */
    public interface LogOutListener {
        /**
         * Do logout.
         */
        void doLogout();
    }

    /**
     * The Long timer.
     */
    static Timer longTimer;
    /**
     * The Logout time.
     */
    private static final int LOGOUT_TIME = 300000;//300000; // delay in milliseconds i.e. 2 min = 60000 ms or use timeout argument

    /**
     * Start logout timer.
     *
     * @param context        the context
     * @param logOutListener the log out listener
     */
    public static synchronized void startLogoutTimer(final Context context, final LogOutListener logOutListener) {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
        if (longTimer == null) {

            longTimer = new Timer();

            longTimer.schedule(new TimerTask() {
                public void run() {
                    cancel();
                    longTimer = null;
                    try {
                        boolean foreGround = new ForegroundCheckTask().execute(context).get();
                        if (foreGround) {
                            Timber.e("foreGround--- %s", foreGround);
                            logOutListener.doLogout();
                        }

                    } catch (InterruptedException | ExecutionException e) {
                        Timber.e("foreGround Exception --- %s", e.getMessage());
                    }

                }
            }, LOGOUT_TIME);
        }
    }

    /**
     * Stop logout timer.
     */
    public static synchronized void stopLogoutTimer() {
        if (longTimer != null) {
            longTimer.cancel();
            longTimer = null;
        }
    }

    /**
     * The type Foreground check task.
     */
    static class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Context... params) {
            final Context context = params[0].getApplicationContext();
            return isAppOnForeground(context);
        }

        private boolean isAppOnForeground(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            final String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }
    }

}

