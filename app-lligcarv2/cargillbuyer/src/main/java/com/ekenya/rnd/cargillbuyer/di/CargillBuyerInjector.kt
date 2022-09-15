package com.ekenya.rnd.cargillbuyer.di

import android.app.Activity
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.baseapp.di.BaseModuleInjector
import com.ekenya.rnd.cargillbuyer.di.injectables.BuyerOtherModules
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Keep
class CargillBuyerInjector : BaseModuleInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun inject(app: CargillApp) {
        DaggerCargillBuyerComponent.builder()
            .appComponent(app.appComponent)
            .buyerOtherModules(BuyerOtherModules(app.applicationContext))
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
