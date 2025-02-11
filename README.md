# Credit Microservice

Spring Boot Webflux microservice that handles credit operations (credits, credit cards).

## Stack
- Java 11
- Spring Boot 2.x
- Spring Webflux
- Spring Cloud Config Client
- Reactive Mongodb
- Openapi contract first
- Swagger ui

## Configuration
Service connects to Config Server for properties:
```properties
spring.application.name=ms-credit-service
spring.config.import=optional:configserver:http://localhost:8888
```

## Swagger
http://localhost:8093/swagger-ui.html

![Uploading ms-credit-service-2025-02-11-233656.png…]()
