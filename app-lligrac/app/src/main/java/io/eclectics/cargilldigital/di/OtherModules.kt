package io.eclectics.cargilldigital.di


import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.eclectics.cargilldigital.data.db.CargillDatabase
import io.eclectics.cargilldigital.data.network.ApiClientService
import io.eclectics.cargilldigital.data.network.NetworkInterceptor
import io.eclectics.cargilldigital.data.network.RemoteDataSource
import io.eclectics.cargilldigital.utils.dk.TAG_SYNC_DATA
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class OtherModules {

    @Provides
    fun provideFragmentActivity(@ActivityContext activity: Context): FragmentActivity {
        return activity as FragmentActivity
    }

    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): NetworkInterceptor {
        return NetworkInterceptor(context)
    }


    @Singleton
    @Provides
    fun provideRemoteDataSource(networkInterceptor: NetworkInterceptor): RemoteDataSource {
        return RemoteDataSource(networkInterceptor)
    }

    //
    @Singleton
    @Provides
    fun provideApiClientService(remoteDataSource: RemoteDataSource): ApiClientService {
        return remoteDataSource.buildApi(ApiClientService::class.java)
    }

    /**
     *
     */

    @Singleton
    @Provides
    fun provideCargillDatabase(@ApplicationContext context: Context): CargillDatabase {
        return CargillDatabase(context)
    }

    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }


    @Singleton
    @Provides
    fun provideSavedWorkInfo(workManager: WorkManager): LiveData<List<WorkInfo>> {
        return workManager.getWorkInfosByTagLiveData(TAG_SYNC_DATA)
    }

}
