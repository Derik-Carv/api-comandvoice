package com.derikddev.api_comandvoice.config;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class HttpClientConfig {

    @Bean
    @Profile("!prod")
    public ClientHttpRequestFactory devRequestFactory() {
        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                                    .setSslContext(sslContext)
                                    .build())
                            .build())
                    .build();

            return new HttpComponentsClientHttpRequestFactory(httpClient);
        } catch (Exception e) {
            throw new RuntimeException("Error creating unsafe SSL factory for dev environment", e);
        }
    }

    @Bean
    @Profile("prod")
    public ClientHttpRequestFactory prodRequestFactory() {
        try {
            // Cria um TrustManager que confia em tudo para o ambiente prod (necessário para o domínio da Hostinger)
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            // Ignora a validação de hostname (corrige o CertificateException do log)
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Sobrescreve o SimpleClientHttpRequestFactory para injetar as configurações de SSL ignorado
            return new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    if (connection instanceof HttpsURLConnection httpsConnection) {
                        httpsConnection.setSSLSocketFactory(sc.getSocketFactory());
                        httpsConnection.setHostnameVerifier(allHostsValid);
                    }
                    super.prepareConnection(connection, httpMethod);
                }
            };
        } catch (Exception e) {
            throw new RuntimeException("Error creating SSL factory for prod environment", e);
        }
    }
}