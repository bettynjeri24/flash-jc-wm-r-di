package io.eclectics.cargilldigital.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import io.eclectics.cargilldigital.R
import io.eclectics.cargilldigital.utils.dk.AUTH_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

/**
 * This class help to hand no internet errors and decide what happenes
 * when there is no internet connection
 */

class NetworkInterceptor(context: Context) : Interceptor {
    val appContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isOnline()) {
            val url = chain.request()
                .url
                .newBuilder()
                //.addQueryParameter("apiKey", API_KEY)
                .build()
            /**
             * Add access token if it exists
             * else dont add it when doing request that dont need it
             */
            if (AUTH_TOKEN == "") {
                return chain.proceed(chain.request())
            } else {
                val request = chain.request().newBuilder().url(url)
                    .addHeader("Authorization", "Bearer $AUTH_TOKEN").build()

                Timber.d(
                    "URL WITH KEY >>>^^^^  URL IS $url \n " +
                            "REQUESTHEADERS IS ${request.headers} \n " +
                            "REQUESTBODY IS ${request.body} \n " +
                            "REQUESTMETHOD IS ${request.method} "
                )
                return chain.proceed(request)
            }

        }

        throw NetworkExceptions(appContext.getString(R.string.no_internet))

    }

    /**
     * Returns true if the network is available else
     * false if network is not available
     */
    private fun isOnline(): Boolean {
        var result = false
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result

    }


}

class NetworkExceptions(message: String) : IOException(message)