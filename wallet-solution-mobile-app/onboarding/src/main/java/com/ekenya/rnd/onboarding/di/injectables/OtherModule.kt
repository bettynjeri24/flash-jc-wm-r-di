package com.ekenya.rnd.onboarding.di.injectables

import com.ekenya.rnd.dashboard.datadashboard.api.ApiServiceDashBoard
import com.ekenya.rnd.onboarding.dataonboarding.api.RemoteDataSource
import com.ekenya.rnd.walletbaseapp.tollo.di.ModuleScope
import dagger.Module
import dagger.Provides

@Module
class OtherModule {
    @ModuleScope
    @Provides
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }

    //
    @ModuleScope
    @Provides
    fun provideApiClientService(remoteDataSource: RemoteDataSource): ApiServiceDashBoard {
        return remoteDataSource.buildApi(ApiServiceDashBoard::class.java)
    }
}
