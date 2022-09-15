package com.ekenya.rnd.dashboard.datadashboard.remote

import co.infinum.retromock.Retromock
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object
    {
        private var retrofit: Retrofit? = null

        fun getClient(baseUrl: String?): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()
            val client = OkHttpClient.Builder()
                .connectionSpecs(
                    Arrays.asList(
                        ConnectionSpec.CLEARTEXT,  // for http
                        ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                            .allEnabledTlsVersions()
                            .allEnabledCipherSuites()
                            .build() // for https
                    )
                )
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(Interceptor { chain ->
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
                    chain.proceed(request)
                })
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

        fun retrofitMockInstance() : Retrofit = Retrofit.Builder()
            .baseUrl("https://www.randomurlnotused.com/gmail/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        var retromock = Retromock.Builder()
            .retrofit(retrofitMockInstance())
            .build()
    }

}