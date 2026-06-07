package com.derikddev.api_comandvoice.config;

import javax.net.ssl.SSLContext;
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
        return new SimpleClientHttpRequestFactory();
    }
}