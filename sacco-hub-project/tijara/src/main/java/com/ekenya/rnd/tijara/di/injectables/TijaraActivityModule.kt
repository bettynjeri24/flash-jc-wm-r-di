package com.ekenya.rnd.tijara.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.baseapp.ui.main.MainViewModel
import com.ekenya.rnd.tijara.MainActivity
import com.ekenya.rnd.tijara.OnBoardingActivity
import com.ekenya.rnd.tijara.TijaraSplashActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TijaraActivityModule {

    /**
     * Main Activity
     */
    ///////////////////////////////////////////////////////////////////////////////////
    @ContributesAndroidInjector(modules = [TijaraMainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @Module
    abstract class TijaraMainActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        abstract fun bindHomeViewModel(viewModel: MainViewModel): ViewModel
    }
    @ContributesAndroidInjector(modules = [TijaraSplashActivityModule::class])
    abstract fun contributeTijaraSplashActivity(): TijaraSplashActivity

    @Module
    abstract class TijaraSplashActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(MainViewModel::class)
        abstract fun bindOnBoardViewModel(viewModel: MainViewModel): ViewModel
    }

}
