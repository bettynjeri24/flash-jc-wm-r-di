package com.ekenya.rnd.cargillbuyer.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.cargillbuyer.CargillBuyerMainActivity
import com.ekenya.rnd.cargillbuyer.ui.BaseCargillBuyerMainViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CargillBuyerActivityModule {

    /**
     * Main Activity
     */
    ///////////////////////////////////////////////////////////////////////////////////
    @ContributesAndroidInjector(modules = [RemittanceMainActivityModule::class])
    abstract fun contributeMainActivity(): CargillBuyerMainActivity

    @Module
    abstract class RemittanceMainActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(BaseCargillBuyerMainViewModel::class)
        abstract fun bindMainViewModel(viewModel: BaseCargillBuyerMainViewModel): ViewModel
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
