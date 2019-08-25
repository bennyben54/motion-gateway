package com.beo.motiongateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MotionRouteLocatorConfig {

    @Value("${servers.camera-conf-server}")
    private String cameraConfServer;
    @Value("${servers.camera1-server}")
    private String camera1Server;
    @Value("${servers.camera2-server}")
    private String camera2Server;
    @Value("${servers.auth-server}")
    private String authServer;
    @Value("${servers.api-user-server}")
    private String apiUserServer;
    @Value("${servers.api-custom-server}")
    private String apiCustomServer;

    private RouteLocator build(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("route_cam1", r -> r.path("/camera/conf")
                        .uri(cameraConfServer))
                .route("route_cam1", r -> r.path("/camera1")
                        .uri(camera1Server))
                .route("route_cam2", r -> r.path("/camera2")
                        .uri(camera2Server))

                .route("route_auth", r -> r.path("/oauth/**")
                        //.filters(f -> f.rewritePath("/oauth/(?<segment>.*)", "/$1"))
                        .uri(authServer))

                .route("route_api_user", r -> r.path("/api/user/**")
                        //.filters(f -> f.rewritePath("/api/user/(?<segment>.*)", "/$1"))
                        .uri(apiUserServer))

                .route("route_api_custom", r -> r.path("/api/custom/**")
                        //.filters(f -> f.rewritePath("/api/custom/(?<segment>.*)", "/$1"))
                        .uri(apiCustomServer))

                .build();
    }

    @Bean
    public RouteLocator getLocatorBean(RouteLocatorBuilder builder) {
        return build(builder);
    }

}
