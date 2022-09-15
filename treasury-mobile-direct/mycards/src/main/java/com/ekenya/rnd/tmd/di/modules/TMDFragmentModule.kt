package com.ekenya.rnd.tmd.di.modules

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.tmd.ui.fragments.activate.ActivateFragment
import com.ekenya.rnd.tmd.ui.fragments.auction_bid.AuctionBidFragment
import com.ekenya.rnd.tmd.ui.fragments.confirm_pin.ConfirmNewPinFragment
import com.ekenya.rnd.tmd.ui.fragments.confirmation.ConfirmFragment
import com.ekenya.rnd.tmd.ui.fragments.confirmation.ConfirmViewModel
import com.ekenya.rnd.tmd.ui.fragments.get_started.GetStartedFragment
import com.ekenya.rnd.tmd.ui.fragments.home.HomeFragment
import com.ekenya.rnd.tmd.ui.fragments.landing.LandingFragment
import com.ekenya.rnd.tmd.ui.fragments.landing.LandingViewModel
import com.ekenya.rnd.tmd.ui.fragments.login.LoginFragment
import com.ekenya.rnd.tmd.ui.fragments.login.LoginViewModel
import com.ekenya.rnd.tmd.ui.fragments.login.selfie.SelfieFragmentAuth
import com.ekenya.rnd.tmd.ui.fragments.login.speechrecognition.FragmentVoiceRegistration
import com.ekenya.rnd.tmd.ui.fragments.lookup.LookUpFragment
import com.ekenya.rnd.tmd.ui.fragments.new_pin.NewPinFragment
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardFragment
import com.ekenya.rnd.tmd.ui.fragments.onboardingpagerfragment.OnboardViewModel
import com.ekenya.rnd.tmd.ui.fragments.securities_bid.SecurityBidFragment
import com.ekenya.rnd.tmd.ui.fragments.security.SecurityFragment
import com.ekenya.rnd.tmd.ui.fragments.securityoffers.SecurityOffersFragment
import com.ekenya.rnd.tmd.ui.fragments.securityoffers.SecurityOffersViewModel
import com.ekenya.rnd.tmd.ui.fragments.selfie.TakeSelfieFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class TMDFragmentModule {

    @ContributesAndroidInjector(modules = [FragmentMyCardsHomeModule::class])
    abstract fun contributeMainFragment(): LandingFragment

    @Module
    abstract class FragmentMyCardsHomeModule {
        @Binds
        @IntoMap
        @ViewModelKey(LandingViewModel::class)
        abstract fun bindMyCardsHomeViewModel(viewModelSelectContacts: LandingViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [OnboardFragmentModule::class])
    abstract fun contributeOnboardFragment(): OnboardFragment

    @Module
    abstract class OnboardFragmentModule {
        @Binds
        @IntoMap
        @ViewModelKey(OnboardViewModel::class)
        abstract fun bindMyCardsHomeViewModel(viewModelSelectContacts: OnboardViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun contributeLoginFragment(): LoginFragment

    @Module
    abstract class LoginModule {
        @Binds
        @IntoMap
        @ViewModelKey(LoginViewModel::class)
        abstract fun bindMyCardsHomeLoginModule(viewModelSelectContacts: LoginViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [ConfirmModule::class])
    abstract fun contributeConfirmFragment(): ConfirmFragment

    @Module
    abstract class ConfirmModule {
        @Binds
        @IntoMap
        @ViewModelKey(ConfirmViewModel::class)
        abstract fun bindMyCardsHomeLoginModule(viewModelSelectContacts: ConfirmViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeAuctionBidFragment(): AuctionBidFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeGetStartedFragment(): GetStartedFragment

    @ContributesAndroidInjector(modules = [SecurityOffersModule::class])
    abstract fun contributeSecurityOffersFragment(): SecurityOffersFragment

    @Module
    abstract class SecurityOffersModule {
        @Binds
        @IntoMap
        @ViewModelKey(SecurityOffersViewModel::class)
        abstract fun bindMyCardsHomeLoginModule(viewModelSelectContacts: SecurityOffersViewModel): ViewModel
    }

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeSelfieFragmentAuth(): SelfieFragmentAuth

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeFragmentVoiceRegistration(): FragmentVoiceRegistration

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeSecurityBidFragment(): SecurityBidFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeTakeSelfieFragment(): TakeSelfieFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeNewPinFragment(): NewPinFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeConfirmNewPinFragment(): ConfirmNewPinFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeSecurityFragment(): SecurityFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeActivateFragment(): ActivateFragment

    @ContributesAndroidInjector(modules = [])
    abstract fun contributeLookUpFragment(): LookUpFragment
}
