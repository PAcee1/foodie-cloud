package com.pacee1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-12-23 09:34
 **/
@SpringBootApplication
@EnableZipkinServer
@EnableDiscoveryClient
public class ZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class,args);
    }
}
