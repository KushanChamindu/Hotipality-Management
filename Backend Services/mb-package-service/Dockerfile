# FROM eclipse-temurin:17-jdk-alpine AS builder
FROM openjdk:17 AS builder

# Set the timezone to Auckland, New Zealand
ENV TZ=Pacific/Auckland

# create app folder for sources
RUN mkdir -p /build
RUN mkdir -p /build/logs

# The WORKDIR instruction sets the working directory for any RUN, CMD, ENTRYPOINT, COPY and ADD instructions that follow it in the Dockerfile.
WORKDIR /build
COPY . /build
#Download all required dependencies into one layer
RUN ./mvnw dependency:resolve && ./mvnw compile
ENV SPRING_PROFILES_ACTIVE=prod
CMD ./mvnw spring-boot:run
