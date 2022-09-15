package com.ekenya.rnd.cargillfarmer.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.cargillfarmer.CargillMainActivity
import com.ekenya.rnd.cargillfarmer.FarmerViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class ActivityCargillModule {

    /**
     * Main Activity
     */
    @ContributesAndroidInjector(modules = [CargillBaseViewModelModule::class])
    abstract fun contributeMainActivity(): CargillMainActivity

    @Module
    abstract class CargillBaseViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(FarmerViewModel::class)
        abstract fun bindHomeViewModel(viewModel: FarmerViewModel): ViewModel
    }
}
