package com.ekenya.lamparam.network;

import com.ekenya.lamparam.BuildConfig;
import com.ekenya.lamparam.user.UserDataRepository;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Initialises retrofit client with all prerequisites
 */
public class SafeOkHttpClient {

    /**
     * @return a safe OKHttp client to handles Https
     */
    public OkHttpClient getSafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = getTrustCerts();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //adding ssl
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            //adding hostname verifier
//            builder.hostnameVerifier((hostname, session) -> {
//                String str = GlobalVariables.urlPattern;
//                String url = "mobileapp"+ str.substring(1, str.length() - 1);
//                HostnameVerifier hv =
//                        HttpsURLConnection.getDefaultHostnameVerifier();
//                if (hv == null) {
//                    throw new NullPointerException("null cannot be cast to non-null type org.apache.http.conn.ssl.X509HostnameVerifier");
//                } else {
//                    return url.equalsIgnoreCase(session.getPeerHost());
//                }
//            });

            //Logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BuildConfig.DEBUG) {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }

            builder.writeTimeout(45, TimeUnit.SECONDS)
                    .readTimeout(45, TimeUnit.SECONDS)
                    .connectTimeout(35, TimeUnit.SECONDS)
                    //interceptor for request and response logs
                    .addInterceptor(loggingInterceptor);
                    //interceptor to add auth token
//                    .addInterceptor(chain -> {
//                        Request request = chain.request();
//
//                        if (userDataRepository.getTokenAvailable()) {
//                            request = request.newBuilder()
//                                    .addHeader("Authorization", "Bearer " + userDataRepository.getToken())
//                                    .build();
//                        }
//
//                        return chain.proceed(request);
//                    });

            //pin cert
//                    .certificatePinner(
//                            new CertificatePinner.Builder()
//                                    .add(GlobalVariables.urlPattern, GlobalVariables.shaPin)
//                                    .build())
//                    .retryOnConnectionFailure(false);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return a safe OKHttp client to handles Https for glide
     */
    public static OkHttpClient getSafeGlideOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = getTrustCerts();

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //adding ssl
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            //adding hostname verifier
//            builder.hostnameVerifier((hostname, session) -> {
//                String str = GlobalVariables.urlPattern;
//                String url = "mobileapp"+ str.substring(1, str.length() - 1);
//                HostnameVerifier hv =
//                        HttpsURLConnection.getDefaultHostnameVerifier();
//                if (hv == null) {
//                    throw new NullPointerException("null cannot be cast to non-null type org.apache.http.conn.ssl.X509HostnameVerifier");
//                } else {
//                    return url.equalsIgnoreCase(session.getPeerHost());
//                }
//            });

            builder.writeTimeout(45, TimeUnit.SECONDS)
                    .readTimeout(45, TimeUnit.SECONDS)
                    .connectTimeout(35, TimeUnit.SECONDS)
                    //pin cert
//                    .certificatePinner(
//                            new CertificatePinner.Builder()
//                                    .add(GlobalVariables.urlPattern, GlobalVariables.shaPin)
//                                    .build())
                    .retryOnConnectionFailure(false);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return trusted certs
     */
    private static TrustManager[] getTrustCerts() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        if (!chain[0].getSubjectX500Principal().getName().contains("CN=UGHOFGLUPDN01") &&
                                !chain[0].getSubjectX500Principal().getName().contains("161775675f68656c706465736b4066696e636175672e6f7267")) {
                            throw new CertificateException("Invalid certificate");
                        }
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }

}
