package com.ekenya.rnd.tmd.di.component

import com.ekenya.rnd.baseapp.di.AppComponent
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.baseapp.di.injectables.ViewModelModule
import com.ekenya.rnd.tmd.di.MyCardsInjector
import com.ekenya.rnd.tmd.di.modules.TMDActivityModule
import com.ekenya.rnd.tmd.di.modules.TMDFragmentModule
import com.ekenya.rnd.tmd.di.modules.TMDNetworkModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        TMDActivityModule::class,
        TMDFragmentModule::class,
        ViewModelModule::class,
        TMDNetworkModule::class
    ]
)
interface MyCardsComponent {
    fun inject(injector: MyCardsInjector)
}
