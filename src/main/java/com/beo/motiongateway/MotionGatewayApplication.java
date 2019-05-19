package com.beo.motiongateway;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableFeignClients
@SpringBootApplication
public class MotionGatewayApplication {

    private final static List<String> args = new ArrayList<>();

    public static void main(String[] args) {
        MotionGatewayApplication.args.addAll(Arrays.asList(args));
        SpringApplication.run(MotionGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator getLocatorBean(RouteLocatorBuilder builder) {
        String cameraAddress;
        if (args.isEmpty() || Strings.isEmpty(args.get(0))) {
            cameraAddress = "localhost";
        } else {
            cameraAddress = args.get(0);
        }
        return builder.routes()
                .route("route_cam1", r -> r.path("/cam1")
                        .uri(String.format("http://%s:8081", cameraAddress)))
                .route("route_cam2", r -> r.path("/cam2")
                        .uri(String.format("http://%s:8082", cameraAddress)))
                .build();
    }

}
