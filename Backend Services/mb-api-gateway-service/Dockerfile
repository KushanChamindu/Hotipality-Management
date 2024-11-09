FROM eclipse-temurin:17
WORKDIR /app
COPY target/MegaBliss-APiGatewayService-0.0.1-SNAPSHOT.jar /app/ApiGatewayService.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "ApiGatewayService.jar"]
EXPOSE 9090
