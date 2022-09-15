package com.ekenya.lamparam.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekenya.lamparam.activities.onboarding.OnBoardingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Responsible for providing mutablemap to ViewModelFactory.
 * Each time we create a new ViewModel, we have to include them in the ViewModelModule
 * so that they can be injected into Android components with the necessary dependencies
 * passed in the constructor
 */
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(OnBoardingViewModel::class)
    abstract fun onboardingViewModel(viewModel: OnBoardingViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(WalletViewModel::class)
//    abstract fun walletViewModel(viewModel: WalletViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(LandingViewModel::class)
//    abstract fun landingViewModel(viewModel: LandingViewModel): ViewModel
}