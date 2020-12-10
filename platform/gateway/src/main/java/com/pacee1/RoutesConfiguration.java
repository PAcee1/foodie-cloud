package com.pacee1;

import com.pacee1.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-12-10 11:24
 **/
@Configuration
public class RoutesConfiguration {

    @Autowired
    private KeyResolver addressKeyResolver;

    @Autowired
    @Qualifier("rateLimiterUser")
    private RedisRateLimiter rateLimiterUser;

    @Autowired
    @Qualifier("rateLimiterOrder")
    private RedisRateLimiter rateLimiterOrder;

    @Autowired
    @Qualifier("rateLimiterCart")
    private RedisRateLimiter rateLimiterCart;

    @Autowired
    @Qualifier("rateLimiterItem")
    private RedisRateLimiter rateLimiterItem;
    @Autowired
    @Qualifier("rateLimiterSearch")
    private RedisRateLimiter rateLimiterSearch;

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // refresh令牌
                .route(r -> r.path("/auth-service/refresh")
                        .uri("lb://FOODIE-AUTH-SERVICE"))
                // user
                .route(r -> r.path("/passport/**", "/address/**", "/center/**", "/userInfo/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setKeyResolver(addressKeyResolver);
                            config.setRateLimiter(rateLimiterUser);
                            config.setStatusCode(HttpStatus.BAD_GATEWAY);
                        }))
                        .uri("lb://FOODIE-USER-SERVICE"))
                // order
                .route(r -> r.path("/orders/**", "/myorders/**", "/mycomments/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setKeyResolver(addressKeyResolver);
                            config.setRateLimiter(rateLimiterOrder);
                            config.setStatusCode(HttpStatus.BAD_GATEWAY);
                        }))
                        .uri("lb://FOODIE-ORDER-SERVICE"))
                // item
                .route(r -> r.path("/items/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setKeyResolver(addressKeyResolver);
                            config.setRateLimiter(rateLimiterItem);
                            config.setStatusCode(HttpStatus.BAD_GATEWAY);
                        }))
                        .uri("lb://FOODIE-ITEM-SERVICE"))
                // search
                .route(r -> r.path("/search/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setKeyResolver(addressKeyResolver);
                            config.setRateLimiter(rateLimiterSearch);
                            config.setStatusCode(HttpStatus.BAD_GATEWAY);
                        }))
                        .uri("lb://FOODIE-SEARCH-SERVICE"))
                // cart
                .route(r -> r.path("/shopcart/**")
                        .filters(f -> f.requestRateLimiter(config -> {
                            config.setKeyResolver(addressKeyResolver);
                            config.setRateLimiter(rateLimiterCart);
                            config.setStatusCode(HttpStatus.BAD_GATEWAY);
                        }))
                        .uri("lb://FOODIE-CART-SERVICE"))
                .build();
    }
}
