package com.pacee1.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>Order服务</p>
 *
 * @author : Pace
 * @date : 2020-10-21 15:54
 **/
@SpringBootApplication
// tk Mybatis扫描Mapper
@MapperScan(basePackages = "com.pacee1.order.mapper")
// 扫描Component
@ComponentScan(basePackages = {"com.pacee1","org.n3r.idworker"})
@EnableDiscoveryClient
// 开启Feign，并添加需要扫描的包
@EnableFeignClients(basePackages = {
        "com.pacee1.user.service",
        "com.pacee1.item.service",
        "com.pacee1.cart.service"
})
@EnableScheduling // 需要使用定时任务
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
