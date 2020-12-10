package com.pacee1;

import com.pacee1.auth.service.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-12-10 11:15
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
        basePackageClasses = {AuthService.class}
)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
