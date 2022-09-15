package com.ekenya.lamparam.activities.onboarding

import com.ekenya.lamparam.di.ActivityScope
import com.ekenya.lamparam.ui.login.PinFragment
import com.ekenya.lamparam.ui.onboarding.LanguageScreen
import com.ekenya.lamparam.ui.onboarding.MainOnBoarding
import com.ekenya.lamparam.ui.onboarding.PolicyFragment
import com.ekenya.lamparam.ui.onboarding.SplashScreen
import com.ekenya.lamparam.ui.onboarding.registration.*
import dagger.Subcomponent

// Definition of a Dagger subcomponent
@Subcomponent
@ActivityScope
interface OnboardingComponent {

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): OnboardingComponent
    }

    fun inject(activity: OnBoardingActivity)
    fun inject(fragment: SplashScreen)
    fun inject(fragment: PolicyFragment)
    fun inject(fragment: MainOnBoarding)
    fun inject(fragment: LanguageScreen)
    fun inject(fragment: VerificationCode)
    fun inject(fragment: UploadDocument)
    fun inject(fragment: RegPersonalInfo)
    fun inject(fragment: RegAlternantiveStep)
    fun inject(fragment: CreateAccount)
    fun inject(fragment: PinFragment)
    fun inject(fragment:  ChangePinFragment)

}