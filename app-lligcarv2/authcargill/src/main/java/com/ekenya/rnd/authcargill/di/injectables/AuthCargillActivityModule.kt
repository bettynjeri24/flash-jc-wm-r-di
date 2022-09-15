package com.ekenya.rnd.authcargill.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.authcargill.AuthCargillMainActivity
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.baseapp.AppMainViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AuthCargillActivityModule {

    @Module
    abstract class AuthCargillMainActivityModule {
        @Binds
        @IntoMap
        @ViewModelKey(AppMainViewModel::class)
        abstract fun bindMainViewModel(viewModel: AppMainViewModel): ViewModel
    }
    /**
     * Main Activity
     */
    ///////////////////////////////////////////////////////////////////////////////////
    @ContributesAndroidInjector(modules = [AuthCargillMainActivityModule::class])
    abstract fun contributeMainActivity(): AuthCargillMainActivity

}
