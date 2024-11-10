# FROM eclipse-temurin:17-jdk-alpine AS builder
# FROM openjdk:17 AS builder
FROM openjdk:17


WORKDIR /app
COPY target/MegaBliss-MainService-0.0.1-SNAPSHOT.jar /app/user_service.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "user_service.jar"]
