# API Gateway

The API Gateway is a central entry point for managing and routing requests to various microservices in our system. It leverages Spring Cloud Gateway to provide a unified and secure API layer.

## Table of Contents

- [Configuration](#configuration)
- [Routes](#routes)
- [Swagger Documentation](#swagger-documentation)
- [Service Discovery](#service-discovery)

## Configuration

The API Gateway's configuration is defined in the `application.yaml` file. Key settings include:

```yaml
server:
  port: 9090

spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      # Global CORS configuration
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods: "*"
            allowedHeaders: "*"
      # Route configurations for microservices
      routes:
        - id: USER-SERVICE
          uri: lb://USER-SERVICE
          predicates:
            - Path=/api/v1/user-service/**
        - id: HOTEL-SERVICE
          uri: lb://HOTEL-SERVICE
          predicates:
            - Path=/api/v1/hotel-service/**
        - id: PACKAGE-SERVICE
          uri: lb://PACKAGE-SERVICE
          predicates:
            - Path=/api/v1/package-service/**
        - id: RESTAURANT-SERVICE
          uri: lb://RESTAURANT-SERVICE
          predicates:
            - Path=/api/v1/restaurant-service/**
```
## Routes
Routes are defined for each microservice to specify how requests should be forwarded. In this example, routes are configured for the following services:

* USER-SERVICE
* HOTEL-SERVICE
* PACKAGE-SERVICE
* RESTAURANT-SERVICE

## Swagger Documentation

Swagger documentation is enabled for API Gateway as well as each microservice. You can access the Swagger UI at the following URL:

API Gateway: [Swagger UI](http://localhost:9090/webjars/swagger-ui/4.15.5/index.html?urls.primaryName=API%20Gateway%20Service)

## Service Discovery
The API Gateway registers with Eureka for service discovery. Ensure that Eureka server(s) are running and accessible.