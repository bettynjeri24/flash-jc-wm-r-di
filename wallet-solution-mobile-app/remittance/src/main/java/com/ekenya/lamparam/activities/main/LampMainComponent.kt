package com.ekenya.lamparam.activities.main

import com.ekenya.lamparam.di.ActivityScope
import dagger.Subcomponent

// Definition of a Dagger subcomponent
@Subcomponent
@ActivityScope
interface LampMainComponent {

    // Factory to create instances of RegistrationComponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): LampMainComponent
    }

    fun inject(activityLamp: LampMainActivity)
}