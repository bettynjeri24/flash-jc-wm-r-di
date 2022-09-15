package com.ekenya.rnd.cargillbuyer.di

import com.ekenya.rnd.baseapp.di.AppComponent
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule
import com.ekenya.rnd.cargillbuyer.di.injectables.BuyerOtherModules
import com.ekenya.rnd.cargillbuyer.di.injectables.CargillBuyerActivityModule
import com.ekenya.rnd.cargillbuyer.di.injectables.CargillBuyerFragmentModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        CargillBuyerActivityModule::class,
        CargillBuyerFragmentModule::class,
        ViewModelModule::class,
    BuyerOtherModules::class
    ]
)
interface CargillBuyerComponent {
    fun inject(injector: CargillBuyerInjector)
}