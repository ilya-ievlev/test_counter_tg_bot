package com.counter.test_counter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy

public class Config {
//    @Bean
//    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(0);
//        httpRequestFactory.setConnectTimeout(0);
//        httpRequestFactory.setReadTimeout(0);
//
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(50);
//        connectionManager.setDefaultMaxPerRoute(20);
//
//        CloseableHttpClient httpClient = HttpClientBuilder.create()
//                .setConnectionManager(connectionManager)
//                .build();
//
//        httpRequestFactory.setHttpClient(httpClient);
//
//        return new RestTemplate(httpRequestFactory);
//    }
}
