package com.beo.motiongateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EnableFeignClients
@SpringBootApplication
public class MotionGatewayApplication {

    private final static List<String> args = new ArrayList<>();

//    @Autowired
//    MotionRouteLocatorConfig ml;

    public static void main(String[] args) {
        MotionGatewayApplication.args.addAll(Arrays.asList(args));
        SpringApplication.run(MotionGatewayApplication.class, args);
    }

    @Bean
    public CorsWebFilter corsFilter() {

        CorsConfiguration config = new CorsConfiguration();

        // Possibly...
        // config.applyPermitDefaultValues()

        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }

//    @Bean
//    public RouteLocator getLocatorBean(RouteLocatorBuilder builder) {
//        return ml.build();
//    }
//    @Bean
//    public RouteLocator getLocatorBean(RouteLocatorBuilder builder) {
//        String cameraAddress;
//        if (args.isEmpty() || Strings.isEmpty(args.get(0))) {
//            cameraAddress = "localhost";
//        } else {
//            cameraAddress = args.get(0);
//        }
//        return builder.routes()
//                .route("route_cam1", r -> r.path("/cam1")
//                        .uri(String.format("http://%s:8081", cameraAddress)))
//                .route("route_cam2", r -> r.path("/cam2")
//                        .uri(String.format("http://%s:8082", cameraAddress)))
//                .build();
//    }

}
