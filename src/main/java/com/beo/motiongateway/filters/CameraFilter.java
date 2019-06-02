package com.beo.motiongateway.filters;

import com.beo.motiongateway.feign.OAuthClient;
import com.beo.motiongateway.feign.model.User;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Order(0)
@Log
public class CameraFilter implements GlobalFilter {

    private static final List<String> CAMERAS_PATH = Arrays.asList("/camera1", "/camera2");
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    public static final String BEARER_QUERY_PARAM = "bearer";

    @Autowired
    private OAuthClient oAuthClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RequestPath path = request.getPath();
        if (CAMERAS_PATH.contains(path.toString())) {
            specificCheckTokenForCameraRequests(request, path);
        }
        return chain.filter(exchange);
    }

    /**
     * Bearer token can be taken in @QueryParam("bearer") or in "Authorization = Bearer token".<br>
     * Priority is given to  @QueryParam("bearer").
     * @param request
     * @param path
     */
    private void specificCheckTokenForCameraRequests(ServerHttpRequest request, RequestPath path) {
        log.info(String.format("specificCheckTokenForCameraRequests = [%s]", path));
        List<String> bearerList = request.getQueryParams().get(BEARER_QUERY_PARAM);
        if (bearerList == null || bearerList.isEmpty()) {

            List<String> authorization = request.getHeaders().get(AUTHORIZATION);
            log.info(String.format("Authorization = [%s]", authorization));

            if (authorization == null || authorization.isEmpty()) {
                throw generateHeaderException("No Authorization header found");
            }

            bearerList = authorization
                    .stream()
                    .filter(s -> s.startsWith(BEARER) && !BEARER.equals(s))
                    .map(s -> s.substring(BEARER.length()))
                    .collect(Collectors.toList());

        }
        if (bearerList == null || bearerList.isEmpty()) {
            throw generateHeaderException("No token given");
        }

        String bearer = bearerList
                .stream()
                .findFirst()
                .orElseThrow(
                        () -> generateHeaderException("No Bearer found")
                );

        User checkToken;
        try {
            checkToken = oAuthClient.checkToken(bearer);
        } catch (Exception e) {
            throw generateHeaderException(e.getMessage());
        }

        if (checkToken == null || !checkToken.isActive()) {
            throw generateHeaderException("Invalid token");
        }

    }

    private ResponseStatusException generateHeaderException(String s) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, s);
    }
}
