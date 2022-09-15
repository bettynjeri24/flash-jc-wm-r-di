package com.ekenya.lamparam.di

import android.content.Context
import com.ekenya.lamparam.activities.main.LampMainComponent
import com.ekenya.lamparam.activities.onboarding.OnboardingComponent
import com.ekenya.lamparam.network.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Main Application graph
 */
@Singleton
@Component(modules = [NetworkModule::class, StorageModule::class, AppSubcomponents::class,ViewModelModule::class])
interface LampAppComponent {

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the Context passed in will be available in the graph
        fun create(@BindsInstance context: Context): LampAppComponent
    }

    // Expose OnboardingComponent factory from the graph
    fun onboardingComponent(): OnboardingComponent.Factory
    fun mainComponent(): LampMainComponent.Factory

}