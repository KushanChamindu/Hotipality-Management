package com.example.megablissapigatewayservice.Filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
        public static final List<String> openApiEndpoints = List.of(
                        "/api/v1/user-service/register",
                        "/api/v1/user-service/token",
                        "/api/v1/user-service/forgot-password",
                        "/api/v1/user-service/reset-password",
                        "/eureka",
                        "/package-service/v3/api-docs",
                        "/hotel-service/v3/api-docs",
                        "/user-service/v3/api-docs",
                        "/restaurant-service/v3/api-docs");

        public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
                        .stream()
                        .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
