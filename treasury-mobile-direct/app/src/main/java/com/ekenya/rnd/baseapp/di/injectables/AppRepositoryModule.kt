package com.ekenya.rnd.baseapp.di.injectables

import android.content.Context
import com.ekenya.rnd.common.repo.*
import com.ekenya.rnd.common.sms.ISendSmsRepository
import com.ekenya.rnd.common.sms.SmsService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppRepositoryModule {

    @Singleton
    @Provides
    fun provideSampleRepository(context: Context): SampleRepository {
        return SampleDataRepo(context)
    }

    @Singleton
    @Provides
    fun provideSmsRepository(smsService: SmsService): ISendSmsRepository {
        return SmsRepository(smsService)
    }

}