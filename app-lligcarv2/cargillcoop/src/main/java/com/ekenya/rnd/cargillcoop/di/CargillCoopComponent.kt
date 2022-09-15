package com.ekenya.rnd.cargillcoop.di

import com.ekenya.rnd.baseapp.di.AppComponent
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule
import com.ekenya.rnd.cargillcoop.di.injectables.CargillCoopActivityModule
import com.ekenya.rnd.cargillcoop.di.injectables.CargillCoopFragmentModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        CargillCoopActivityModule::class,
        CargillCoopFragmentModule::class,
        ViewModelModule::class
    ]
)
interface CargillCoopComponent {
    fun inject(injector: CargillCoopInjector)
}