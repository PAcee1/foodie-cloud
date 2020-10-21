package com.pacee1.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Created by pace
 * @Date 2020/6/12 15:15
 * @Classname MvcConfig
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

    /**
     * 本地路径映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**") // 所有路径
                .addResourceLocations("classpath:/META-INF/resources/") // Swagger2的映射
                .addResourceLocations("file:E:/pic/foodie-image/") // 添加图片本地映射
                .addResourceLocations("file:/file/image/");
    }
}
