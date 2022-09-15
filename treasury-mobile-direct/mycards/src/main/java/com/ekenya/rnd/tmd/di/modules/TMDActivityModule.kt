package com.ekenya.rnd.tmd.di.modules

import com.ekenya.rnd.tmd.ui.ActivityMainMyTmd
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TMDActivityModule {

    @ContributesAndroidInjector(
        modules = [
//        MainActivityModule::class
        ]
    )
    abstract fun contributeMainActivity(): ActivityMainMyTmd

//    @Module
//    abstract class MainActivityModule {
//        @Binds
//        @IntoMap
//        @ViewModelKey(MainViewModel::class)
//        abstract fun bindPageViewModel(viewModel: MainViewModel): ViewModel
//    }

// /////////////////////////////////////////////////////////////////////////////////
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
}
