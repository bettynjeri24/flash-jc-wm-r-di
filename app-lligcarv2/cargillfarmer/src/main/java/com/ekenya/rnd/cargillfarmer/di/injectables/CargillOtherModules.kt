package com.ekenya.rnd.cargillfarmer.di.injectables

import android.content.Context
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.cargillfarmer.data.db.FarmerAppDatabase
import com.ekenya.rnd.cargillfarmer.data.network.CargillFarmerApiService
import com.ekenya.rnd.common.data.db.CommonAppDatabase
import com.ekenya.rnd.common.data.db.CommonCargillDataPreferences
import com.ekenya.rnd.common.data.network.NetworkInterceptor
import com.ekenya.rnd.common.data.network.RemoteDataSource
import com.ekenya.rnd.common.utils.services.GoogleSMSReceiver
import com.ekenya.rnd.common.utils.services.HuaweiSMSReceiver
import dagger.Module
import dagger.Provides

@Module
class CargillOtherModules(private val context: Context) {
    @ModuleScope
    @Provides
    fun provideNetworkInterceptor(): NetworkInterceptor {
        return NetworkInterceptor(context)
    }

    @ModuleScope
    @Provides
    fun provideRemoteDataSource(networkInterceptor: NetworkInterceptor): RemoteDataSource {
        return RemoteDataSource(networkInterceptor)
    }

    @ModuleScope
    @Provides
    fun provideApiService(remoteDataSource: RemoteDataSource): CargillFarmerApiService {
        return remoteDataSource.buildApi(CargillFarmerApiService::class.java)
    }

    @ModuleScope
    @Provides
    fun provideAppDatabase(): FarmerAppDatabase {
        return FarmerAppDatabase(context)
    }
    @ModuleScope
    @Provides
    fun provideCommonAppDatabase(): CommonAppDatabase {
        return CommonAppDatabase(context)
    }


    @ModuleScope
    @Provides
    fun provideDataPreferences(): CommonCargillDataPreferences {
        return CommonCargillDataPreferences(context)
    }


    @ModuleScope
    @Provides
    fun provideGoogleSMSReceiver(): GoogleSMSReceiver {
        return GoogleSMSReceiver()
    }


    @ModuleScope
    @Provides
    fun provideHuaweiSMSReceiver(): HuaweiSMSReceiver {
        return HuaweiSMSReceiver()
    }

}