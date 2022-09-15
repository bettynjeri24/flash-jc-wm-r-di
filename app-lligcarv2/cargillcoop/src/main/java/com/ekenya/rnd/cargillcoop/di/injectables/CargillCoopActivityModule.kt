package com.ekenya.rnd.cargillcoop.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.cargillcoop.ui.CoopMainActivity
import com.ekenya.rnd.cargillcoop.ui.CoopMainViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class CargillCoopActivityModule {

    /**
     * Main Activity
     */
    // /////////////////////////////////////////////////////////////////////////////////
    @ContributesAndroidInjector(modules = [RemittanceMainActivityModule::class])
    abstract fun contributeMainActivity(): CoopMainActivity

    @Module
    abstract class RemittanceMainActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(CoopMainViewModel::class)
        abstract fun bindMainViewModel(viewModel: CoopMainViewModel): ViewModel
    }
}
