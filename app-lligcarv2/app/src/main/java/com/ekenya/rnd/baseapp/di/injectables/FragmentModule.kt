package com.ekenya.rnd.baseapp.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.baseapp.ui.main.AppMainFragment
import com.ekenya.rnd.baseapp.AppMainViewModel
import com.ekenya.rnd.baseapp.ui.offlineussd.OfflineUssdFragment
import com.ekenya.rnd.common.auth.CommonAuthFragment
import com.ekenya.rnd.common.successful.SuccessfulFragmentCommon
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun contribute_CommonAuthFragment(): CommonAuthFragment

    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun contribute_SuccessfulFragmentCommon(): SuccessfulFragmentCommon



    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun contribute_HomeFragment(): OfflineUssdFragment
    @Module
    abstract class MainFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(AppMainViewModel::class)
        abstract fun bindMainViewModel(viewModel: AppMainViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [MainFragmentModule::class])
    abstract fun contributeFirstFragment(): AppMainFragment

}