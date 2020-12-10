package com.pacee1.filter;

import com.pacee1.auth.service.AuthService;
import com.pacee1.auth.service.pojo.Account;
import com.pacee1.auth.service.pojo.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * <p>鉴权拦截器</p>
 *
 * @author : Pace
 * @date : 2020-12-10 15:49
 **/
@Component("authFilter")
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    private static final String AUTH = "Authorization";
    private static final String USERNAME = "foodie-user-name";
    private static final String USERID = "foodie-user-id";


    private static final List<String> authRoute = Arrays.asList("/shopcart/add"
            ,"/shopcart/del"
            ,"/address/add"
            ,"/address/list"
            ,"/address/update"
            ,"/address/setDefault"
            ,"/address/delete"
            ,"/orders/*"
            ,"/center/*"
            ,"/userInfo/*"
            ,"/myorders/*"
            ,"/mycomments/*"
            ,"/myorders/deliver"
            ,"/orders/notifyMerchantOrderPaid");

    {};

    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 首先校验该路径是否需要鉴权
        RequestPath path = exchange.getRequest().getPath();
        String pathStr = path.pathWithinApplication().value();
        if(!authRoute.contains(pathStr)){
            return chain.filter(exchange);
        }

        log.info("Auth start");
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders header = request.getHeaders();
        String token = header.getFirst(AUTH);
        String userId = header.getFirst(USERID);

        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(token)) {
            log.error("token not found");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        Account acct = Account.builder().token(token).userId(userId).build();
        AuthResponse resp = authService.verify(acct);
        if (resp.getCode() != 1L) {
            log.error("invalid token");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // TODO 将用户信息存放在请求header中传递给下游业务
        ServerHttpRequest.Builder mutate = request.mutate();
        mutate.header(USERID, userId);
        mutate.header(AUTH, token);
        ServerHttpRequest buildReuqest = mutate.build();

        // TODO 如果响应中需要放数据，也可以放在response的header中
        response.getHeaders().add(USERID, userId);
        response.getHeaders().add(AUTH, token);
        return chain.filter(exchange.mutate()
                .request(buildReuqest)
                .response(response)
                .build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
