﻿# mb-tour-guide-service

## ERD of the project
![Alt text](<./assests/package_service_DB_design.drawio.png>)

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 4](https://maven.apache.org) - if you don't have maven on your machine don't worry ignore that

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com\megabliss\tourguideservice\TourGuideServiceApplication.java` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:
 
 *
    ```shell
    mvn spring-boot:run
    ```
* or for windows
    ```shell
    ./mvnw.cmd spring-boot:run
    ```
* or for mac/linux
    ```shell
    ./mvnw spring-boot:run
    ```

To run deferent activation profile follow below commands

* For windows 
    ```shell
    powershell ->
    $env:SPRING_PROFILES_ACTIVE = "prod"; ./mvnw spring-boot:run
    
    command prompt ->
    set SPRING_PROFILES_ACTIVE=prod && ./mvnw spring-boot:run
    ```
* or for mac/linux
    ```shell
    export SPRING_PROFILES_ACTIVE=prod && ./mvnw spring-boot:run
    ```

## Swagger UI Documentation
* You can access the Swagger API documentation using the following URL when the application is up and running in the local environment.

[Swagger UI API Documentation](http://localhost:8080/swagger-ui/index.html#/)

## Guthub rules
Ensure code owners review your code before merging it to the master branch.


## Containerization - Docker
```shell
docker-compose build
```
```shell
docker-compose up
```
### Run postgres container without docker-compose
```shell
docker run -d --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=megadevdb -e POSTGRES_USER=postgres -e <docker_image_id>
```
In above docker constainer, we don't have databases yet. To create Db we can use following commands
* To get container informations
  ```shell
  docker ps -a
  ```
* Login to the container terminals
  ```shell
  docker exec -it <postgres- container name> bash
  ```
* Login to postgres
  ```shell
  psql -h localhost -U postgres
  ```
* To Create a db - run this command in postgres cmd
  ```shell
  CREATE DATABASE <add db name>;
  ```
* To list down existing DB - run this command in postgres cmd
  ```shell
  \l
  ```
### Run springboot image without docker-compose
```shell
docker run -d -p 8080:8080 <docker-image-id>
```
### If you run docker image once, we can re-use same created conatiner again and agian. Todo that,
```shell
docker restart <conatiner-name>
```

Wait until all cotainers are up and runing. Then go to the swagger documentation to use endpoints of the application