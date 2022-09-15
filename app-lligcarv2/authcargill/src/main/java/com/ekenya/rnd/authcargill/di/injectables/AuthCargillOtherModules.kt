package com.ekenya.rnd.authcargill.di.injectables



import android.content.Context
import com.ekenya.rnd.authcargill.data.network.AuthApiClientService
import com.ekenya.rnd.baseapp.di.ModuleScope
import com.ekenya.rnd.common.data.db.CommonAppDatabase
import com.ekenya.rnd.common.data.network.NetworkInterceptor
import com.ekenya.rnd.common.data.network.RemoteDataSource
import com.ekenya.rnd.common.utils.services.GoogleSMSReceiver
import com.ekenya.rnd.common.utils.services.HuaweiSMSReceiver
import dagger.Module
import dagger.Provides

@Module
class AuthCargillOtherModules(private val context: Context) {

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

    //
    @ModuleScope
    @Provides
    fun provideApiClientService(remoteDataSource: RemoteDataSource): AuthApiClientService {
        return remoteDataSource.buildApi(AuthApiClientService::class.java)
    }

    //
    @ModuleScope
    @Provides
    fun provideAppDatabase(): CommonAppDatabase {
        return CommonAppDatabase(context)
    }
//
//
//
    @ModuleScope
    @Provides
    fun provideGoogleSMSReceiver(): GoogleSMSReceiver {
        return GoogleSMSReceiver()
    }
//
//
    @ModuleScope
    @Provides
    fun provideHuaweiSMSReceiver(): HuaweiSMSReceiver {
        return HuaweiSMSReceiver()
    }

}
