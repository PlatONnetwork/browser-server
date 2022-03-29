package com.platon.browser.bean.http;

import cn.hutool.http.ssl.TrustAnyHostnameVerifier;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class CustomHttpClient {

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient.Builder().connectionPool(new ConnectionPool(50, 5, TimeUnit.MINUTES))
                                                        .connectTimeout(10, TimeUnit.SECONDS)
                                                        .readTimeout(10, TimeUnit.SECONDS)
                                                        .writeTimeout(10, TimeUnit.SECONDS)
                                                        .hostnameVerifier(new TrustAnyHostnameVerifier())
                                                        .sslSocketFactory(createSSLSocketFactory())
                                                        .build();
        return client;
    }

    /**
     * 默认信任所有的证书
     *
     * @param
     * @return javax.net.ssl.SSLSocketFactory
     * @date 2021/6/8
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

}
