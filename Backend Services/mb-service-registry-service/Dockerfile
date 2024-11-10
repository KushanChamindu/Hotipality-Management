FROM eclipse-temurin:17
WORKDIR /app
COPY target/MegaBliss-ServiceRegistry-0.0.1-SNAPSHOT.jar /app/ServiceRegistry.jar
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "ServiceRegistry.jar"]
EXPOSE 8761
