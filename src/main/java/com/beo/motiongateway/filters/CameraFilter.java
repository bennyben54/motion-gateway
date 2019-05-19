package com.beo.motiongateway.filters;

import com.beo.motiongateway.feign.OAuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
@Order(0)
public class CameraFilter implements GlobalFilter {

    public static final String BEARER = "Bearer ";

    @Autowired
    private OAuthClient oAuthClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        System.out.println(String.format("Path = [%s]", request.getPath()));
        request.getPath();
        List<String> authorization = request.getHeaders().get("Authorization");
        System.out.println(String.format("Authorization = [%s]", authorization));
        if (authorization == null || authorization.isEmpty()) {
            throw generateHeaderException("No Authorization header found");
        }
        String bearer = authorization.stream().filter(s -> s.startsWith(BEARER) && !BEARER.equals(s)).findFirst().orElseThrow(
                () -> generateHeaderException("No Authorization Bearer found")
        ).substring(BEARER.length());

        String checkToken = oAuthClient.checkToken(bearer);
        System.out.println(String.format("checkToken = [%s]", checkToken));

        return chain.filter(exchange);
    }

    private ResponseStatusException generateHeaderException(String s) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, s);
    }
}
