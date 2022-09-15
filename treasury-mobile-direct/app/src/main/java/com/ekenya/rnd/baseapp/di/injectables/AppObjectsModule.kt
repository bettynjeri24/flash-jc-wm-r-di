package com.ekenya.rnd.baseapp.di.injectables

import android.content.Context
import android.content.SharedPreferences
import com.ekenya.rnd.common.sms.SmsRequest
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppObjectsModule {

    @Provides
    @Singleton
    fun providesSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("CBK", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideSmsRequest(): SmsRequest {
        return SmsRequest(
            clientid = "5166",
            from = "Eclectics",
            message = "Your Otp test is ${(100000..999999).random()} and your one time password is ${(1000..9999).random()}",
            password = "b7c8f6faf1fcfc08ec7481eee2ebe297e7d29a758fdce4613eb5042e6d5c803b38adbf69860687b22b475dee9c9e4a2c32863398ba0923613aab2887e26a0528",
            to = "+254710102720",
            transactionID = "23232323",
            username = "pension"
        )
    }
}
