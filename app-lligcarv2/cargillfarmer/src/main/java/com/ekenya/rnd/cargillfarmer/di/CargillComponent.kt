package com.ekenya.rnd.cargillfarmer.di

import com.ekenya.rnd.baseapp.di.AppComponent
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule
import com.ekenya.rnd.cargillfarmer.di.injectables.ActivityCargillModule
import com.ekenya.rnd.cargillfarmer.di.injectables.CargillOtherModules
import com.ekenya.rnd.cargillfarmer.di.injectables.FragmentCargillModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityCargillModule::class,
        FragmentCargillModule::class,
        ViewModelModule::class,
        CargillOtherModules::class
    ]
)
interface CargillComponent {
    fun inject(injector: CargillInjector)
}