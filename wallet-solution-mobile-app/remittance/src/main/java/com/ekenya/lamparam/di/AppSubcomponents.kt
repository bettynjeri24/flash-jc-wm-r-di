package com.ekenya.lamparam.di

import com.ekenya.lamparam.activities.main.LampMainComponent
import com.ekenya.lamparam.activities.onboarding.OnboardingComponent
import dagger.Module

// This module tells AppComponent which are its subcomponents
@Module(subcomponents = [OnboardingComponent::class, LampMainComponent::class])
class AppSubcomponents