package com.pacee1;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * <p>Redis限流</p>
 *
 * @author : Pace
 * @date : 2020-12-10 14:07
 **/
@Configuration
public class RedisLimiterConfiguration {

    @Bean
    @Primary
    // Key制造器，这里我们以地址为区分Key的约束
    public KeyResolver addressKeyResolver(){
        return exchange ->  Mono.just(
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress()
            );
    }

    @Bean("rateLimiterUser")
    @Primary // 需要一个主Bean
    // 创建User服务限流规则
    public RedisRateLimiter rateLimiterUser(){
        // 这里使用最简单的实现方式，令牌桶
        // 第一个参数为令牌桶的每秒发放令牌速率，第二个参数为令牌桶的最大容量
        return new RedisRateLimiter(5,10);
    }

    @Bean("rateLimiterOrder")
    // 创建Order服务限流规则
    public RedisRateLimiter rateLimiterOrder(){
        return new RedisRateLimiter(20,40);
    }

    @Bean("rateLimiterCart")
    // 创建Cart服务限流规则
    public RedisRateLimiter rateLimiterCart(){
        return new RedisRateLimiter(20,40);
    }

    @Bean("rateLimiterItem")
    // 创建Item服务限流规则
    public RedisRateLimiter rateLimiterItem(){
        return new RedisRateLimiter(50,100);
    }

    @Bean("rateLimiterSearch")
    // 创建Search服务限流规则
    public RedisRateLimiter rateLimiterSearch(){
        return new RedisRateLimiter(50,100);
    }
}
