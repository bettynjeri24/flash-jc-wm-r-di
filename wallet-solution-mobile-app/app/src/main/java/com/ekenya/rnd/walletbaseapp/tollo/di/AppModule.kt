package com.ekenya.rnd.walletbaseapp.tollo.di

import android.app.Application
import android.content.Context
import com.ekenya.rnd.walletbaseapp.tollo.WalletSolutionApp
import com.ekenya.rnd.common.repo.SampleDataRepo
import com.ekenya.rnd.common.repo.SampleRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: WalletSolutionApp): Context {
        return app
    }

    @Singleton
    @Provides
    fun provideSampleRepository(context: Context): SampleRepository {
        return SampleDataRepo(context)
    }

    @Singleton
    @Provides
    fun provideApp(context: Context): Application {
        return context.applicationContext as Application
    }
}