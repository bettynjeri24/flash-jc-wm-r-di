package com.ekenya.rnd.dashboard.datadashboard.api

import com.ekenya.rnd.common.Constants.BASE_URL_CBG_DEMO2
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .connectionSpecs(
                listOf(
                    ConnectionSpec.CLEARTEXT, // for http
                    ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .allEnabledTlsVersions()
                        .allEnabledCipherSuites()
                        .build() // for https
                )
            )
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//            .addInterceptor(object : Interceptor {
//                @Throws(IOException::class)
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val original = chain.request()
//                    val request = original.newBuilder()
//                        .header("User-Agent", "android")
//                        .header("Accept", "*/*")
//                        .header("Content-Type", "application/json")
//                        .header("Accept-Encoding", "gzip, deflate, br")
//                        .header("x-message-type", "0")
//                        .header("Connection", "keep-alive")
//                        .method(original.method, original.body)
//                        .build()
//                    return chain.proceed(request)
//                }
//            })
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            // .baseUrl("https://test-api.ekenya.co.ke/cbg/")
            .baseUrl(BASE_URL_CBG_DEMO2)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiServiceDashBoard: ApiServiceDashBoard = getRetrofit().create(ApiServiceDashBoard::class.java)
}
