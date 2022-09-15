package com.ekenya.rnd.onboarding.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.dashboard.DashBoardActivity
import com.ekenya.rnd.dashboard.DashboardActivityViewmodel
import com.ekenya.rnd.walletbaseapp.tollo.di.ViewModelKey
import com.ekenya.rnd.onboarding.MainActivity
import com.ekenya.rnd.onboarding.ui.DashboardViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TourismActivityModule {

    /**
     * Main Activity
     */
    ///////////////////////////////////////////////////////////////////////////////////

    @ContributesAndroidInjector(modules = [WalletDashboardActivityModule::class])
    abstract fun contributeDashboardActivity(): DashBoardActivity
    @ContributesAndroidInjector(modules = [TourismMainActivityModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @Module
    abstract class TourismMainActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(DashboardViewModel::class)
        abstract fun bindLoginViewModel(viewModel: DashboardViewModel): ViewModel
    }
    @Module
    abstract class WalletDashboardActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(DashboardActivityViewmodel::class)
        abstract fun bindLoginViewModel(viewModel: DashboardActivityViewmodel): ViewModel
    }
    ///////////////////////////////////////////////////////////////////////////////////
//
//    /**
//     * Slider
//     */
//    ///////////////////////////////////////////////////////////////////////////////////
//    @ContributesAndroidInjector(modules = [SmeSliderActivityModule::class])
//    abstract fun contributeSliderActivity(): SliderActivity
//
//    @Module
//    abstract class SmeSliderActivityModule {
//
//        @Binds
//        @IntoMap
//        @ViewModelKey(CountryViewModel::class)
//        abstract fun bindCountryViewModel(viewModel: CountryViewModel): ViewModel
//    }

    ///////////////////////////////////////////////////////////////////////////////////

    //LIST ALL OTHER ACTIVITIES IN THIS MODULE
}
