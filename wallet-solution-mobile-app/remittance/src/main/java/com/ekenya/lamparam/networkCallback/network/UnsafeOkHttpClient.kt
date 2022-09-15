package com.ekenya.lamparam.network

import com.ekenya.lamparam.networkCallback.network.RestClient
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class UnsafeOkHttpClient {
    companion object {
        fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                })

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory

                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
                // builder.connectTimeout(5,TimeUnit.MINUTES);
//builder.writeTimeout(5,TimeUnit.MINUTES);
               /* builder.sslSocketFactory(
                    sslSocketFactory,
                    (trustAllCerts[0] as X509TrustManager)
                )*/
               // builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })

                    .connectTimeout(RestClient.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(RestClient.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(RestClient.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .certificatePinner(
                        CertificatePinner.Builder()
                            .add("https://test-api.ekenya.co.ke", "sha256/5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=")
                            .build())


                //if(BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                //val logging = LoggingInterceptors() //TODO Interceptors
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)

                return builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}