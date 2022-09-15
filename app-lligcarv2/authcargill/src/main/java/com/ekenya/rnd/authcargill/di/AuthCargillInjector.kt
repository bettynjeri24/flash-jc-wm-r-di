package com.ekenya.rnd.authcargill.di

import android.app.Activity
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.ekenya.rnd.authcargill.di.injectables.AuthCargillOtherModules
import com.ekenya.rnd.baseapp.CargillApp
import com.ekenya.rnd.baseapp.di.BaseModuleInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Keep
class AuthCargillInjector : BaseModuleInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun inject(app: CargillApp) {
        DaggerAuthCargillComponent.builder()
            .appComponent(app.appComponent)
            .authCargillOtherModules(AuthCargillOtherModules(app.applicationContext))
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
