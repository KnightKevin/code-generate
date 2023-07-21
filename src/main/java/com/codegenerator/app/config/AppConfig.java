package com.codegenerator.app.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@EnableAspectJAutoProxy
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {

        int connectTimeoutMillis = 3000; // 连接超时时间：5秒
        int readTimeoutMillis = 10000;   // 读取超时时间：10秒

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeoutMillis)
                .setSocketTimeout(readTimeoutMillis)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build());


        return new RestTemplate(factory);
    }
}
