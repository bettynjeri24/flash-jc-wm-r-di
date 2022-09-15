package io.eclectics.cargilldigital

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.hilt.work.HiltWorkerFactory
import androidx.multidex.MultiDexApplication
import androidx.work.*
import dagger.hilt.android.HiltAndroidApp
import io.eclectics.cargilldigital.utils.LoggerHelper
import io.eclectics.cargill.utils.NetworkUtility
import io.eclectics.cargilldigital.utils.dk.SYNC_DATA_WORK_NAME
import io.eclectics.cargilldigital.utils.dk.TAG_SYNC_DATA
import io.eclectics.cargilldigital.utils.workmanager.SyncDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.Timber.Forest.plant
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class AppCargillDigital : MultiDexApplication(), androidx.work.Configuration.Provider {

    private val TAG = "AppCargillDigital"

    private val locale: Locale? = null
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        /* val lang: String? = PreferencesUtils().getPrefLang(this)
         lang.let {
             Lingver.init(this, lang!!)
         }*/
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            @SuppressLint("SourceLockedOrientationActivity")
            override fun onActivityCreated(
                activity: Activity,
                savedInstanceState: Bundle?
            ) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                } else {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
/*                //Todo: Comment to Enable  screenshots on Test Environment
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )*/
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        delayedInit()

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        delayedInitForWorkManager()

    }

    /* private fun setupData() {
         val data = InfoSec.getInstance().secParams
         infoSecConstants.setKeyPublic(InfoSec.getInstance().publicYek)
         infoSecConstants.setTokenKey(InfoSec.getInstance().sanitizer)
         Timber.e(InfoSec.getInstance().requestParams)
         setSecParams(GeneralUtils.convertToJsonObject(data))

         //infoSecConstants.setBaseUrl(InfoSec.getInstance().setBaseUrl())  //Production URL
         //Dynamically set the endPoint based on the App build FLAVOR
         infoSecConstants.setBaseUrl(setUpBaseUrl())

     }
 */
    private fun delayedInit() {
        applicationScope.launch {
            if (BuildConfig.DEBUG) {
                //  Timber.plant(Timber.DebugTree())
            }
            populateData()
            //  setupData()
        }
    }

    private fun populateData() {
        LoggerHelper.loggerError("baseUlr", ""/*getStringBaseUrl()+"**"*/)
        LoggerHelper.loggerError("UatbaseUlr", ""/*getStringUATBaseUrl()+"**"*/)
        NetworkUtility.baseUrl = ""//getStringUATBaseUrl()!!
    }

    init {
        instance = this
        deviceSessionUUID = UUID.randomUUID().toString()
        System.loadLibrary("native-lib")


    }

    companion object {
        private var instance: AppCargillDigital? = null
        private var deviceSessionUUID: String? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

        fun deviceSessionUUID(): String {
            return if (deviceSessionUUID.isNullOrEmpty()) UUID.randomUUID().toString()
            else return deviceSessionUUID as String
        }
//        open external fun getStringBaseUrl():String?
//        open external fun getStringUATBaseUrl():String?
    }

    /**
     * Get the String to match UAT or Prod Release version
     *   Dynamically set the endPoint based on the App build FLAVOR
     */
    /*private fun setUpBaseUrl(): String {
        return if (BuildConfig.FLAVOR.contains("dev")) InfoSec.getInstance().setUATBaseUrl() //Test
        else InfoSec.getInstance().setBaseUrl() //Production URL
    }*/


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (locale != null) {
            Locale.setDefault(locale)
            val config = Configuration(newConfig)
            config.locale = locale
            // Timber.e("+++++++++Config Changed++++++++")
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()


    private fun delayedInitForWorkManager() {
        applicationScope.launch {
            //setupRecurringWork()
            delayedInitForWorkManagerPushToServer()
        }
    }

    private fun setupRecurringWork() {
        val repeatingRequest =
            PeriodicWorkRequestBuilder<SyncDataWorker>(1, TimeUnit.HOURS).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )


    }

    private fun delayedInitForWorkManagerPushToServer() {
        // Create Network constraint
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        Timber.e("=============1============== ${Calendar.getInstance().get(Calendar.SECOND)}")
        val periodicSyncDataWork =
            PeriodicWorkRequest.Builder(SyncDataWorker::class.java, 1, TimeUnit.HOURS)
                .addTag(TAG_SYNC_DATA)
                .setConstraints(constraints) // setting a backoff on case the work needs to retry
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            SYNC_DATA_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,  //Existing Periodic Work policy
            periodicSyncDataWork //work request
        )
    }

    /**
     * Change lang.
     *
     * @param lang the lang
     */
    /* fun changeLang(lang: String?) {
         PreferencesUtils().setPrefLang(this, lang)
         Lingver.getInstance().setLocale(this, lang!!)
     }*/

    /**
     * Gets lang.
     *
     * @return the lang
     */
    /*fun getLang(): String? {
       // return PreferencesUtils().getPrefLang(this)
    }*/

}


