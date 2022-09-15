package com.ekenya.rnd.onboarding.di

import com.ekenya.rnd.onboarding.di.injectables.OtherModule
import com.ekenya.rnd.onboarding.di.injectables.TourismActivityModule
import com.ekenya.rnd.onboarding.di.injectables.TourismFragmentModule
import com.ekenya.rnd.walletbaseapp.tollo.di.AppComponent
import com.ekenya.rnd.walletbaseapp.tollo.di.ModuleScope
import com.ekenya.rnd.walletbaseapp.tollo.di.injectables.ViewModelModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@ModuleScope
@Component(
    dependencies = [
        AppComponent::class
    ],
    modules = [
        AndroidSupportInjectionModule::class,
        TourismActivityModule::class,
        TourismFragmentModule::class,
        OtherModule::class,
        ViewModelModule::class
    ]
)
interface TourismComponent {
    fun inject(injector: TourismInjector)
}
