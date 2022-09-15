package com.ekenya.rnd.tmd.di.modules

import co.infinum.retromock.Retromock
import com.ekenya.rnd.baseapp.BuildConfig
import com.ekenya.rnd.common.sms.SmsResponse
import com.ekenya.rnd.common.sms.SmsService
import com.ekenya.rnd.tmd.data.network.CBKApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class TMDNetworkModule {

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()
    }

    @Provides
    fun providesRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl("http://10.20.2.12:5000/")
            .baseUrl("https://test-portal.ekenya.co.ke/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesRetromock(retrofit: Retrofit): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit)
            .build()
    }

    @Provides
    fun providesRetrofitService(retrofit: Retrofit): CBKApi {
        return retrofit.create(CBKApi::class.java)
    }

    @Provides
    fun providesSms(): SmsService {
        val httpClient = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level =
                        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://testgateway.ekenya.co.ke:8443/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SmsService::class.java)
    }
}
