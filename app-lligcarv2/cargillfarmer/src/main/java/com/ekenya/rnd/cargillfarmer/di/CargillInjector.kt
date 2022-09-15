package com.ekenya.rnd.cargillfarmer.di

import android.app.Activity
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.baseapp.di.BaseModuleInjector
import com.ekenya.rnd.cargillfarmer.di.injectables.CargillOtherModules
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Keep
class CargillInjector: BaseModuleInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun inject(app: CargillApp) {
        DaggerCargillComponent.builder()
            .appComponent(app.appComponent)
            .cargillOtherModules(CargillOtherModules(app.applicationContext))
            .build()
            .inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return activityInjector
    }

    override fun fragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return fragmentInjector
    }
}
