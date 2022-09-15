package com.ekenya.rnd.walletbaseapp.tollo.di

import android.app.Activity
import androidx.fragment.app.Fragment
import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp
import dagger.android.DispatchingAndroidInjector

interface BaseModuleInjector {

    fun inject(app: WalletSolutionApp)

    fun activityInjector(): DispatchingAndroidInjector<Activity>

    fun fragmentInjector(): DispatchingAndroidInjector<Fragment>
}
