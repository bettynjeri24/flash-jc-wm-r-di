package com.ekenya.rnd.authcargill.di

import com.ekenya.rnd.baseapp.di.AppComponent
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule
import com.ekenya.rnd.authcargill.di.injectables.AuthCargillActivityModule
import com.ekenya.rnd.authcargill.di.injectables.AuthCargillFragmentModule
import com.ekenya.rnd.authcargill.di.injectables.AuthCargillOtherModules
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        AuthCargillActivityModule::class,
        AuthCargillFragmentModule::class,
        AuthCargillOtherModules::class,
        ViewModelModule::class
    ]
)
interface AuthCargillComponent {
    fun inject(injector: AuthCargillInjector)
}