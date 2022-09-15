package com.ekenya.rnd.dashboard.datadashboard.api

import com.ekenya.rnd.common.Constants
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitBuilder2 {


        private fun getRetrofit(): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .connectionSpecs(
                    listOf(
                        ConnectionSpec.CLEARTEXT,  // for http
                        ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                            .allEnabledTlsVersions()
                            .allEnabledCipherSuites()
                            .build() // for https
                    )
                )
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val original = chain.request()
                        val request = original.newBuilder()
                            .header("User-Agent", "android")
                            .header("Accept", "*/*")
                            .header("Content-Type", "application/json")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .header("x-message-type", "0")
                            .header("Connection", "keep-alive")
                            .method(original.method, original.body)
                            .build()
                        return chain.proceed(request)
                    }
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()


            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val apiService: ApiService2 = getRetrofit().create(ApiService2::class.java)

    }
