package com.ekenya.rnd.tijara.di

import com.ekenya.rnd.baseapp.di.AppComponent
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule
import com.ekenya.rnd.tijara.di.injectables.TijaraActivityModule
import com.ekenya.rnd.tijara.di.injectables.TijaraFragmentModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        TijaraActivityModule::class,
        TijaraFragmentModule::class,
        ViewModelModule::class
    ]
)
interface TijaraComponent {
    fun inject(injector: TijaraInjector)
}