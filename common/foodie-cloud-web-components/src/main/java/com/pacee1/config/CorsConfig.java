package com.pacee1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author pace
 * @version v1.0
 * @Type CorsConfig.java
 * @Desc
 * @date 2020/5/17 17:56
 */
@Configuration
public class CorsConfig {

    public CorsConfig(){}

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许的路径
        corsConfiguration.addAllowedOrigin("http://localhost:8080");
        corsConfiguration.addAllowedOrigin("http://www.enbuys.com:8080");
        corsConfiguration.addAllowedOrigin("http://www.enbuys.com");
        corsConfiguration.addAllowedOrigin("http://center.enbuys.com:8080");
        corsConfiguration.addAllowedOrigin("http://center.enbuys.com");
        // 允许cookie
        corsConfiguration.setAllowCredentials(true);
        // 允许的方法
        corsConfiguration.addAllowedMethod("*");
        // 允许的Header
        corsConfiguration.addAllowedHeader("*");

        // 设置映射
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 所有映射
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsFilter(source);
    }
}
