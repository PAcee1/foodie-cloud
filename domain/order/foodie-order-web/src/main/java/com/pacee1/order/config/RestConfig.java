package com.pacee1.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-10-27 10:14
 **/
@Configuration
public class RestConfig {

    @Bean("RestTemplateLB")
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
