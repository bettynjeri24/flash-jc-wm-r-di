package com.ekenya.rnd.onboarding.dataonboarding.Remote;


import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

;

public class RetrofitClient {

    private static Retrofit retrofit = null;




    public static Retrofit getClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ConnectionSpec spec =
                new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)

                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()

                .connectionSpecs(Arrays.asList(
                ConnectionSpec.CLEARTEXT, // for http
                new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .allEnabledTlsVersions()
                        .allEnabledCipherSuites()
                        .build() // for https
        ))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();

                        Request request = original.newBuilder()
                                .header("User-Agent", "android")
                                .header("Accept", "*/*")
                                .header("Content-Type", "application/json")
                                .header("Accept-Encoding", "gzip, deflate, br")
                                .header("x-message-type", "0")
                                .header("Connection", "keep-alive")
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
        .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();



        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
