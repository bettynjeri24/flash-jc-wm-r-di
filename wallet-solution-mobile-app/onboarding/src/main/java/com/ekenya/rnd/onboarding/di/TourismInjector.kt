package com.ekenya.rnd.onboarding.di

import android.app.Activity
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp
import com.ekenya.rnd.walletbaseapp.tollo.di.BaseModuleInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

@Keep
class TourismInjector : BaseModuleInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun inject(app: WalletSolutionApp) {
        DaggerTourismComponent.builder()
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
