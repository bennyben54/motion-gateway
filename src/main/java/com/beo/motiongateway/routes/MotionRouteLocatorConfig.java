package com.beo.motiongateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MotionRouteLocatorConfig {

    @Value("${servers.camera-server}")
    private String cameraServer;
    @Value("${servers.auth-server}")
    private String authServer;
    @Value("${servers.rest-server}")
    private String restServer;

    private RouteLocator build(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("route_cam1", r -> r.path("/camera1")
                        .uri(String.format("%s:8081", cameraServer)))
                .route("route_cam2", r -> r.path("/camera2")
                        .uri(String.format("%s:8082", cameraServer)))

                .route("route_auth", r -> r.path("/oauth/**")
                        .filters(f -> f.rewritePath("/oauth/(?<segment>.*)", "/$1"))
                        .uri(authServer))

                .route("route_rest", r -> r.path("/rest/**")
                        .filters(f -> f.rewritePath("/rest/(?<segment>.*)", "/$1"))
                        .uri(restServer))

                .build();
    }

    @Bean
    public RouteLocator getLocatorBean(RouteLocatorBuilder builder) {
        return build(builder);
    }

}
