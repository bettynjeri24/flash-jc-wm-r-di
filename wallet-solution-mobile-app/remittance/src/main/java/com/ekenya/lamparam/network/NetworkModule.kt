package com.ekenya.lamparam.network

import com.ekenya.lamparam.BuildConfig
import com.ekenya.lamparam.Keys
import com.ekenya.lamparam.user.UserDataRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import timber.log.Timber
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * @author Lennox Brown.
 */
@Module
class NetworkModule {

    private val BASE_URL = Keys.testURL() //retrieve url from native library

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideTrustCerts(): Array<TrustManager> {
        return arrayOf( object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
//                        if (!chain[0].getSubjectX500Principal().getName().contains("CN=UGHOFGLUPDN01") &&
//                                !chain[0].getSubjectX500Principal().getName().contains("161775675f68656c706465736b4066696e636175672e6f7267")) {
//                            throw new CertificateException("Invalid certificate");
//                        }
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideHttpLogging(): HttpLoggingInterceptor {
        //Logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return loggingInterceptor
    }

    @Provides
    fun provideOkHttpClient(trustAllCerts: Array<TrustManager>, loggingInterceptor: HttpLoggingInterceptor,
    userDataRepository: UserDataRepository): OkHttpClient {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCerts, SecureRandom()) // Install the all-trusting trust manager

        val builder = OkHttpClient.Builder()

        // Enable caching for OkHttp
//            int cacheSize = 10 * 1024 * 1024; // 10 MiB
//            Cache cache = new Cache(getApplication().getCacheDir(), cacheSize);
//            client.setCache(cache);

        builder.writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .connectTimeout(35, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor) //interceptor for request and response logs
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->  // interceptor to add auth token
                var request = chain.request()
                if (userDataRepository.tokenAvailable) {
                    val newRequest = request.newBuilder()
                        .addHeader("Authorization","Bearer ${userDataRepository.token}")
//                        .addHeader("Authorization", "Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJoYXJyeSIsImF1ZCI6IldhbGxldCBUcmFuc2FjdGlvbnMiLCJpYXQiOjE2MjYxNzE4NjgsImV4cCI6MTYyNjYwMzg2OH0.ruhYp4_Q7v5Z66rrwxmbawYyaH5kptAiReRURuw-xWLizRGRkqpp8oph7n5OFj420n-uXNc7ae5vSb2hnFkp0IsseDC_CrfV-GKqgN1X56Pbh4rz2Oe1VdGB7rRk6w3DKjnWU_4tMuoHWpi0dv6mgeExGV1KKF5Ugl-XRUbYF-fN5MDplc7sLtvPhQ8Qm4TyEvR70ZbqZ3hClwYkb-aPNY8HzwAQjqJ4PboiL2maehwyYqzqcv8t5ZWrOIX9sqQjqVHw-t5DOrEeCwagmTfuy19srOmCLC1Jw2cIkXG1fKz1d_V0sfqNP-wVAs3Uy69kOoh117d9jEEfcxOXekQtzQ")
                        .build()
                    Timber.d("--> Sending request ${newRequest}")
                    chain.proceed(newRequest)
                }else{
                    chain.proceed(request)
                }
            })

            //                    .certificatePinner( //pin cert
            //                            new CertificatePinner.Builder()
            //                                    .add(GlobalVariables.urlPattern, GlobalVariables.shaPin)
            //                                    .build())
            .retryOnConnectionFailure(false)



        return builder.build()
//        return SafeOkHttpClient().safeOkHttpClient
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://70e7-196-188-0-82.ngrok.io/cbg/mobile/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): LamparamApi {
        return retrofit.create(LamparamApi::class.java)
    }
}

interface LamparamApi{

    @Headers("Content-Type: application/json")
    @POST("lamparam/")
    suspend fun sendRequest(@Body body: String): Response<DefaultResponse>

    @Headers("Content-Type: application/json")
    @POST("lamparam/auth.php")
    suspend fun appToken(@Query("username") username: String,
                         @Query("password") password: String): Response<DefaultResponse>

}