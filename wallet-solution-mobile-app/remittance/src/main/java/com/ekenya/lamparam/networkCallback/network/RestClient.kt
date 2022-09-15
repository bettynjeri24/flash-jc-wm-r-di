package com.ekenya.lamparam.networkCallback.network

import android.util.Log
import com.ekenya.lamparam.network.UnsafeOkHttpClient
import com.google.gson.Gson
import com.google.gson.JsonObject
//import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
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
    const val CONNECTION_TIMEOUT = 40000L//60000L

    init {
        setRetrofitClient()
    }

    private fun setRetrofitClient() {

        val gson = Gson()
        val retrofit = Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl("https://70e7-196-188-0-82.ngrok.io/cbg/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        apiService = retrofit.create(Webservice::class.java)

    }



    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
        /* .certificatePinner(CertificatePinner.Builder()
             .add("https://test-api.ekenya.co.ke", "sha256/5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=")
             .build()
         )*/


        //if(BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        //val logging = LoggingInterceptors() //TODO Interceptors
        logging.level = HttpLoggingInterceptor.Level.BODY
        client.addInterceptor(logging)

        /*client.certificatePinner( CertificatePinner.Builder()
            .add("https://test-api.ekenya.co.ke", "sha256/5kJvNEMw0KjrCAu7eXY5HZdvyCS13BbA0VJG1RSP91w=")
            .build())*/
        // }

        return client.build()
    }


    internal class LoggingInterceptors : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {

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

                    Log.v("API", "${e.message}")
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



