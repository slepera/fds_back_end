package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {

    @Autowired
    AuthenticationFilter filter;


    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://user-service"))

                .route("ext-com-service", r -> r.path("/ecs/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://ext-com-service"))

                .route("log-service", r -> r.path("/log/**")
                        .filters(f -> f.filter(filter).stripPrefix(1))
                        .uri("lb://log-service"))
                .build();
    }

}
