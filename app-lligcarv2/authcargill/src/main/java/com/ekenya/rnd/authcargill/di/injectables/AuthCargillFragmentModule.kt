package com.ekenya.rnd.authcargill.di.injectables

import androidx.lifecycle.ViewModel
import com.ekenya.rnd.authcargill.ui.AuthCargillViewModel
import com.ekenya.rnd.authcargill.ui.language.LanguageFragment
import com.ekenya.rnd.authcargill.ui.login.LoginPinFragment
import com.ekenya.rnd.baseapp.di.ViewModelKey
import com.ekenya.rnd.authcargill.ui.lookup.CompanyIDLookupFragment
import com.ekenya.rnd.authcargill.ui.lookup.UserPhoneLookupFragment
import com.ekenya.rnd.authcargill.ui.otpverification.OtpVerificationFragment
import com.ekenya.rnd.authcargill.ui.pinmgmt.SetAccountPinFragment
import com.ekenya.rnd.authcargill.ui.login.LookUpPhoneNumberCoopIdFragment
import com.ekenya.rnd.authcargill.ui.splash.SplashFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AuthCargillFragmentModule {
    @Module
    abstract class AuthCargillMainViewModelModule {
        @Binds
        @IntoMap
        @ViewModelKey(AuthCargillViewModel::class)
        abstract fun bindMainViewModel(viewModel: AuthCargillViewModel): ViewModel
    }
//
//    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
//    abstract fun contributeFirstFragment(): FirstFragment


//    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
//    abstract fun contribute_LoginAccountFragment(): LoginAccountFragment

    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_AUserPhoneLookupFragment(): UserPhoneLookupFragment

    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_CompanyIDLookupFragment(): CompanyIDLookupFragment

    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_SetAccountPinFragment(): SetAccountPinFragment

    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_OtpVerificationFragment(): OtpVerificationFragment

    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_LoginPinFragment(): LoginPinFragment


    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_LookUpPhoneNumberCoopIdFragment(): LookUpPhoneNumberCoopIdFragment


    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_SplashFragment(): SplashFragment

    @ContributesAndroidInjector(modules = [AuthCargillMainViewModelModule::class])
    abstract fun contribute_LanguageFragment(): LanguageFragment


}
