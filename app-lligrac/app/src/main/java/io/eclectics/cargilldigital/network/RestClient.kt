package io.eclectics.cargilldigital.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.eclectics.cargill.network.Webservice
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

object RestClient {
    lateinit var apiService: Webservice
    const val CONNECTION_TIMEOUT = 15000L//60000L //40000L
    const val READ_TIMEOUT = 60000L
    private val BASEURL = "http://102.37.14.127:5000/"

    init {
        setRetrofitClient()
    }

    private fun setRetrofitClient() {

//TODO ADD UNSAFE HTTP CERT
        // var unsafeOkhttp = UnsafeOkHttpClient.getUnsafeOkHttpClient()
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val gson = Gson()
        val retrofit = Retrofit.Builder()
            .client(provideOkHttpClient())
            //.client(unsafeOkhttp.build())//http://45.221.73.118:4113/crownbeverage/solution/api/v1.0/mobile
            //live cargill poc endpoint http://102.37.14.127/cargill_poc/api/cargill_api/
            //.baseUrl("http://45.221.73.118:4113/crownbeverage/solution/api/v1.0/")
            //cbl prod
            .baseUrl(BASEURL.toString())//"http://102.37.14.127:5000/")//http://192.168.1.113:4113/crownbeverage/solution/api/v1.0/")
            //.baseUrl( "https://testgateway.ekenya.co.ke:8443/")
            //.addConverterFactory(MoshiCointernalfundstransfernverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        //val authtoken = Credentials.basic("username", "password")
        apiService = retrofit.create(Webservice::class.java)

    }
    /* val certificatePinner=
         CertificatePinner.Builder() //pin nMqjbDWenPPz3XZ/kOv7V8tKAyrlLMZDFR2SuK1ufDs=
             .add("*.ekenya.co.ke", "sha256/hkhkhfahaliuhrfjdbfiaulfhjkldbfaddkjdkj=")
             .build()*/

    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)


        //if(BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        //val logging = LoggingInterceptors() //TODO Interceptors
        logging.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logging)
        client.certificatePinner(
            CertificatePinner.Builder()
                .add("*.ekenya.co.ke", "sha256/5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=")
                .build()
        )
        //}

        return client.build()
    }

    fun provideOkHttpClientx(): OkHttpClient {
        val client = OkHttpClient.Builder().build()
        return client
    }

    internal class LoggingInterceptors : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            var request = chain.request()
            val t1 = System.nanoTime()


            val requestBuilder = request.newBuilder()
            var postData = ""
            if (request.method.compareTo("post", ignoreCase = true) == 0) {
                var rbody: RequestBody? = null
                try {
                    rbody = JSONObject(bodyToString(request)).toString()
                        .toRequestBody("text/plain; charset=utf-8".toMediaTypeOrNull())//bodyToString(request));

                } catch (e: Exception) {

                    Log.v("API", e.message!!)
                }

                request = requestBuilder
                    .addHeader("Content-Type", "text/plain")
                    // .addHeader("Authorization", "Bearer "+Constants.getTokenKey())
                    //.post(request.body())
                    .post(rbody!!)
                    .build()



                postData = bodyToString(request)
            } else {
                request = requestBuilder
                    .get()
                    .build()
            }
            val requestLog = String.format(
                "Sending %s request %s on %s%n%s  \n %s",
                request.method, request.url, chain.connection(), request.headers, postData
            )

            Log.v("API", requestLog)

            var response = chain.proceed(request)
            try {

                val t2 = System.nanoTime()

                val responseLog = String.format(
                    Locale.ENGLISH, "Received response for %s in %.1fms%n",
                    response.request.url, (t2 - t1) / 1e6
                )

                var bodyString = response.body!!.string()

                Log.v("API", "\nresponse: $responseLog\n$bodyString")
                // Log.v("API","\ntkey: " + Constants.getTokenKey() + "\n" + bodyString);
                bodyString =
                    bodyString//DataProtection.decrypt(Constants.getTokenKey(),bodyString);//"$jt^&jt(ITm2J1K9"
                Log.v("API", "\nresponse: $responseLog\n$bodyString")
                return response
                    .newBuilder()
                    .body(bodyString.toResponseBody(response.body!!.contentType()))
                    .build()

            } catch (e: Exception) {
                Log.v("API", "error " + e.message)
                val json = JsonObject()
                json.addProperty("field39", "72")
                json.addProperty("field48", e.message)
                Log.e("timeoutexp", json.toString())
                // response=response.newBuilder().message(json.toString()).build();
                response = response
                    .newBuilder()
                    // .body(response1,json.toString())
                    .body(json.toString().toResponseBody(response.body!!.contentType()))
                    .build()
                Log.e("responceerror", response.toString() + "if null")
                return response

            }

        }

        /**
         * Body to string string.
         *
         * @param request the request
         * @return the string
         */
        fun bodyToString(request: Request): String {
            try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body!!.writeTo(buffer)
                buffer.close()
                return buffer.readUtf8()
            } catch (e: IOException) {
                Log.v("API", "bodyToString " + e.message)
                return ""
            }

        }


    }


}



