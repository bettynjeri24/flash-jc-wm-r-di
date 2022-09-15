package com.ekenya.rnd.tmd.di

import android.app.Activity
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.ekenya.rnd.baseapp.TMDApp
import com.ekenya.rnd.baseapp.di.BaseModuleInjector
import com.ekenya.rnd.tmd.di.component.DaggerMyCardsComponent
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Keep
class MyCardsInjector : BaseModuleInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun inject(app: TMDApp) {
        DaggerMyCardsComponent.builder()
            .appComponent(app.appComponent)
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
