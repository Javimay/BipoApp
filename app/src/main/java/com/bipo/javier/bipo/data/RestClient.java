package com.bipo.javier.bipo.data;
import android.content.Context;

import com.google.gson.Gson;
        import com.google.gson.GsonBuilder;

        import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
        import javax.net.ssl.SSLContext;
        import javax.net.ssl.SSLSession;
        import javax.net.ssl.SSLSocketFactory;
        import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

        import retrofit.GsonConverterFactory;
        import retrofit.Retrofit;

        import com.squareup.okhttp.Interceptor;
        import com.squareup.okhttp.OkHttpClient;
        import com.squareup.okhttp.Request;
        import com.squareup.okhttp.Response;

import java.util.concurrent.TimeUnit;

/**
 * Created by apineda on 3/02/2017.
 */

public class RestClient {
    private static final String BASE_URL = "http://www.bipoapp.com/services/v1/";
    private IRestClient apiService;

    //token es:650E01A1B8F9A4DA4A2040FF86E699B7 puede ponerlo quemado o crear una variable global
    public RestClient(Context context,final String token) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();
// loading CAs from an InputStream

        Retrofit retrofit = new Retrofit.Builder()
                .client(getUnsafeOkHttpClient(token))
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(IRestClient.class);
    }

    /**
     * Retorna una instancia de la interfaz de servicios, sin autenticacion
     * @return Instancia de laa interfaz de servicios
     */
    public IRestClient getApiService()
    {
        return apiService;
    }

    private static OkHttpClient getUnsafeOkHttpClient(final String token) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient().setSslSocketFactory(sslSocketFactory).setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
            okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
            okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
            okHttpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    //AQUI SE USA LA VARIABLE TOKEN QUE SE ENCUENTRA ARRIBA
                    Request request = chain.request().newBuilder().addHeader("Authorization", token)
                            //.addHeader("Content-Type", "application/json")
                            .build();
                    return chain.proceed(request);
                }
            });
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
