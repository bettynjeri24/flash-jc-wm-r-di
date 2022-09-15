package com.ekenya.lamparam

import android.app.Application
import com.ekenya.lamparam.di.DaggerLampAppComponent
import com.ekenya.lamparam.di.LampAppComponent
import timber.log.Timber

/**
 * Main Lamparam app
 */
class LamparamApplication: Application() {

    val appComponent: LampAppComponent = DaggerLampAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree()) //initialize Timber to be used in the app

    }
}