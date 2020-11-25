package com.pacee1;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-11-25 10:14
 **/
@EnableTurbine
@SpringCloudApplication
@EnableHystrix
public class HystrixTurbineApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixTurbineApplication.class,args);
    }
}
